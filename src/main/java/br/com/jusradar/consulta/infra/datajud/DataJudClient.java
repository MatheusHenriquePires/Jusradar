package br.com.jusradar.consulta.infra.datajud;

import br.com.jusradar.consulta.domain.Processo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataJudClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${datajud.base-url}")
    private String baseUrl;

    @Value("${datajud.api-key}")
    private String apiKey;

    public List<Processo> buscar(String numeroProcesso, String tribunal) {

        String alias = resolverAlias(tribunal);
        String url = baseUrl + "/" + alias + "/_search";

        String numeroLimpo = numeroProcesso.replaceAll("\\D", "");

        String body = """
            {
              "size": 10,
              "query": {
                "match": {
                  "numeroProcesso": "%s"
                }
              }
            }
            """.formatted(numeroLimpo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "APIKey " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
            );

            JsonNode json = objectMapper.readTree(response.getBody());

            return parsear(json);

        } catch (Exception e) {

            log.error("Erro ao consultar DataJud", e);

            return List.of();
        }
    }

    private List<Processo> parsear(JsonNode body) {

        List<Processo> resultado = new ArrayList<>();

        if (body == null) {
            return resultado;
        }

        JsonNode hits = body.path("hits").path("hits");

        for (JsonNode hit : hits) {

            JsonNode source = hit.path("_source");

            JsonNode movimentos = source.path("movimentos");

            String ultimoMovimento = "";

            if (movimentos.isArray() && movimentos.size() > 0) {

                ultimoMovimento = movimentos
                    .get(movimentos.size() - 1)
                    .path("nome")
                    .asText();
            }

            resultado.add(Processo.builder()
                .numero(source.path("numeroProcesso").asText())
                .tribunal(source.path("tribunal").asText())
                .classe(source.path("classe").path("nome").asText())
                .situacao(ultimoMovimento)
                .dataUltimaMovimentacao(
                    source.path("dataHoraUltimaAtualizacao").asText()
                )
                .orgaoJulgador(
                    source.path("orgaoJulgador").path("nome").asText()
                )
                .build());
        }

        return resultado;
    }

    private String resolverAlias(String tribunal) {

        return switch (tribunal.toUpperCase()) {

            case "TRF1" -> "api_publica_trf1";
            case "TRF2" -> "api_publica_trf2";

            default -> "api_publica_tjpi";
        };
    }
}