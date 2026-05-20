package br.com.jusradar.notificacao.application;

import br.com.jusradar.consulta.domain.Processo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssistenteJuridicoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String model;

    public String analisar(String numeroProcesso, String tribunal,
                           String classe, String ultimaMovimentacao,
                           String pergunta) {
        Processo processo = Processo.builder()
            .numero(numeroProcesso)
            .tribunal(tribunal)
            .classe(classe)
            .situacao(ultimaMovimentacao)
            .build();

        return analisar(processo, pergunta);
    }

    public String analisar(Processo processo, String pergunta) {
        try {
            String prompt = montarPrompt(processo, pergunta);
            String body = montarRequisicao(prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            ResponseEntity<String> response = restTemplate.exchange(
                groqUrl,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText();

        } catch (Exception e) {
            log.error("Erro ao consultar Groq: {}", e.getMessage());
            return "Nao foi possivel gerar analise no momento.";
        }
    }

    String montarPrompt(Processo processo, String pergunta) {
        return """
            Analise o processo judicial abaixo usando apenas os dados informados.
            Quando uma informacao estiver ausente, deixe isso claro e nao invente fatos.

            Dados reais do processo:
            - Numero: %s
            - Tribunal: %s
            - Classe: %s
            - Orgao julgador: %s
            - Situacao ou ultima movimentacao: %s
            - Data da ultima atualizacao: %s

            Pergunta do usuario:
            %s

            Responda em portugues, com linguagem clara e profissional, seguindo esta estrutura:
            1. Resumo objetivo da situacao atual
            2. Pontos juridicos relevantes a partir dos dados do processo
            3. Riscos, urgencias e prazos que devem ser verificados
            4. Proximos passos recomendados
            5. Sugestao de peticao ou providencia, se aplicavel
            """.formatted(
            valor(processo.getNumero()),
            valor(processo.getTribunal()),
            valor(processo.getClasse()),
            valor(processo.getOrgaoJulgador()),
            valor(processo.getSituacao()),
            valor(processo.getDataUltimaMovimentacao()),
            valor(pergunta)
        );
    }

    private String montarRequisicao(String prompt) throws Exception {
        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
                Map.of(
                    "role", "system",
                    "content", "Voce e um advogado especialista brasileiro com experiencia em direito civil, penal e trabalhista. Responda sempre em portugues, de forma clara, objetiva e profissional."
                ),
                Map.of(
                    "role", "user",
                    "content", prompt
                )
            ),
            "temperature", 0.3,
            "max_tokens", 2048
        );

        return objectMapper.writeValueAsString(body);
    }

    private String valor(String valor) {
        return valor == null || valor.isBlank() ? "Nao informado" : valor;
    }
}
