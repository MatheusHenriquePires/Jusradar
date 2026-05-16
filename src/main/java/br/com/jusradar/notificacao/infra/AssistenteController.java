package br.com.jusradar.notificacao.infra;

import br.com.jusradar.consulta.application.ConsultaProcessoService;
import br.com.jusradar.notificacao.application.AssistenteJuridicoService;
import br.com.jusradar.notificacao.application.GeradorDocumentoService;
import br.com.jusradar.notificacao.application.dto.AssistenteRequest;
import br.com.jusradar.notificacao.application.dto.DocumentoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/assistente")
@RequiredArgsConstructor
public class AssistenteController {

    private final AssistenteJuridicoService assistente;
    private final ConsultaProcessoService consultaService;
    private final GeradorDocumentoService geradorDocumento;

    @PostMapping("/analisar")
    public ResponseEntity<Map<String, String>> analisar(
            @RequestBody AssistenteRequest request) {

        var consulta = consultaService.consultar(
            request.getNumeroProcesso(),
            request.getTribunal()
        );

        String classe = "";
        String ultimaMovimentacao = "";

        if (!consulta.processos().isEmpty()) {
            var processo = consulta.processos().get(0);
            classe = processo.getClasse();
            ultimaMovimentacao = processo.getSituacao();
        }

        String analise = assistente.analisar(
            request.getNumeroProcesso(),
            request.getTribunal(),
            classe,
            ultimaMovimentacao,
            request.getPergunta()
        );

        return ResponseEntity.ok(Map.of(
            "numeroProcesso", request.getNumeroProcesso(),
            "tribunal", request.getTribunal(),
            "analise", analise
        ));
    }

    @PostMapping("/gerar-documento")
    public ResponseEntity<byte[]> gerarDocumento(
            @RequestBody DocumentoRequest request,
            @RequestParam(defaultValue = "pdf") String formato) {

        String conteudo = geradorDocumento.gerarConteudo(request);

        if (formato.equalsIgnoreCase("docx")) {
            byte[] docx = geradorDocumento.gerarDocx(request, conteudo);
            return ResponseEntity.ok()
                .header("Content-Disposition",
                    "attachment; filename=documento.docx")
                .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(docx);
        }

        byte[] pdf = geradorDocumento.gerarPdf(request, conteudo);
        return ResponseEntity.ok()
            .header("Content-Disposition",
                "attachment; filename=documento.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}