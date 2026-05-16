package br.com.jusradar.notificacao.application;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssistenteJuridicoService {

    private final RestTemplate restTemplate;

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String model;

 public String analisar(String numeroProcesso, String tribunal,
                       String classe, String ultimaMovimentacao,
                       String pergunta) {
    try {
        String prompt = montarPrompt(numeroProcesso, tribunal,
            classe, ultimaMovimentacao, pergunta);

        String body = """
            {
              "model": "%s",
              "messages": [
                {
                  "role": "system",
                  "content": "Você é um advogado especialista brasileiro com 20 anos de experiência em direito civil, penal e trabalhista. Responda sempre em português, de forma clara, objetiva e profissional."
                },
                {
                  "role": "user",
                  "content": "%s"
                }
              ],
              "temperature": 0.7,
              "max_tokens": 2048
            }
            """.formatted(model, prompt.replace("\"", "'").replace("\n", "\\n"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Recebe como String em vez de JsonNode
        ResponseEntity<String> response = restTemplate.exchange(
            groqUrl,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            String.class
        );

        // Parseia manualmente
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        return root.path("choices").get(0)
            .path("message")
            .path("content").asText();

    } catch (Exception e) {
        log.error("❌ Erro ao consultar Groq: {}", e.getMessage());
        return "Não foi possível gerar análise no momento.";
    }
}

    private String montarPrompt(String numeroProcesso, String tribunal,
                                String classe, String ultimaMovimentacao,
                                String pergunta) {
        return """
            Analise o processo judicial abaixo e responda como um advogado experiente.

            Dados do processo:
            - Número: %s
            - Tribunal: %s
            - Classe: %s
            - Última movimentação: %s

            Pergunta: %s

            Responda com:
            1. Resumo da situação atual
            2. Análise jurídica
            3. Próximos passos recomendados
            4. Prazos importantes
            5. Sugestão de petição se aplicável
            """.formatted(numeroProcesso, tribunal, classe,
                          ultimaMovimentacao, pergunta);
    }
}