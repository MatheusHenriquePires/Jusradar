package br.com.jusradar.notificacao.application;

import br.com.jusradar.consulta.domain.Processo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class AssistenteJuridicoServiceTest {

    private final AssistenteJuridicoService service = new AssistenteJuridicoService(
        new RestTemplate(),
        new ObjectMapper()
    );

    @Test
    void deveMontarPromptComDadosReaisDoProcesso() {
        Processo processo = Processo.builder()
            .numero("0801234-56.2024.8.18.0140")
            .tribunal("TJPI")
            .classe("Procedimento Comum Civel")
            .situacao("Conclusos para decisao")
            .dataUltimaMovimentacao("2026-05-19T10:15:00")
            .orgaoJulgador("2a Vara Civel de Teresina")
            .build();

        String prompt = service.montarPrompt(
            processo,
            "Existe alguma providencia urgente?"
        );

        assertThat(prompt)
            .contains("0801234-56.2024.8.18.0140")
            .contains("TJPI")
            .contains("Procedimento Comum Civel")
            .contains("Conclusos para decisao")
            .contains("2026-05-19T10:15:00")
            .contains("2a Vara Civel de Teresina")
            .contains("Existe alguma providencia urgente?")
            .contains("nao invente fatos");
    }

    @Test
    void deveIndicarQuandoAlgumDadoDoProcessoNaoFoiInformado() {
        Processo processo = Processo.builder()
            .numero("0801234-56.2024.8.18.0140")
            .tribunal("TJPI")
            .build();

        String prompt = service.montarPrompt(processo, "");

        assertThat(prompt)
            .contains("- Classe: Nao informado")
            .contains("- Orgao julgador: Nao informado")
            .contains("- Situacao ou ultima movimentacao: Nao informado")
            .contains("- Data da ultima atualizacao: Nao informado")
            .contains("Pergunta do usuario:")
            .contains("Nao informado");
    }
}
