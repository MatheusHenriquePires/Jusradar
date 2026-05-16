package br.com.jusradar.notificacao.application;

import br.com.jusradar.notificacao.application.dto.DocumentoRequest;
import br.com.jusradar.notificacao.application.dto.TipoDocumento;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeradorDocumentoService {

    private final AssistenteJuridicoService assistente;

    public byte[] gerarPdf(DocumentoRequest request, String conteudo) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont fontBold   = PdfFontFactory.createFont("Helvetica-Bold");
            PdfFont fontNormal = PdfFontFactory.createFont("Helvetica");

            // Cabeçalho
            document.add(new Paragraph()
                .add(new Text("EXCELENTÍSSIMO SENHOR DOUTOR JUIZ DE DIREITO\n")
                    .setFont(fontBold).setFontSize(11))
                .add(new Text("DA VARA DO TRIBUNAL DE JUSTIÇA DO " + request.getTribunal())
                    .setFont(fontNormal).setFontSize(10))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

            // Título
            document.add(new Paragraph(resolverTitulo(request.getTipoDocumento()))
                .setFont(fontBold).setFontSize(13)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(20));

            // Número do processo
            document.add(new Paragraph("Processo nº: " + request.getNumeroProcesso())
                .setFont(fontBold).setFontSize(10).setMarginBottom(10));

            // Conteúdo limpo
            document.add(new Paragraph(limparMarkdown(conteudo))
                .setFont(fontNormal).setFontSize(10)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginBottom(30));

            // Data
            String data = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                    new Locale("pt", "BR")));

            document.add(new Paragraph("Teresina - PI, " + data)
                .setFont(fontNormal).setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(40));

            // Assinatura
            document.add(new Paragraph(
                "_______________________________\n" +
                request.getNomeAdvogado() + "\n" +
                "OAB/" + request.getOabNumero())
                .setFont(fontNormal).setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("❌ Erro ao gerar PDF: {}", e.getMessage());
            return new byte[0];
        }
    }

    public byte[] gerarDocx(DocumentoRequest request, String conteudo) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             XWPFDocument docx = new XWPFDocument()) {

            // Cabeçalho
            XWPFParagraph cab = docx.createParagraph();
            cab.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runCab = cab.createRun();
            runCab.setBold(true);
            runCab.setFontSize(12);
            runCab.setText("EXCELENTÍSSIMO SENHOR DOUTOR JUIZ DE DIREITO");
            runCab.addBreak();
            runCab.setText("DA VARA DO TRIBUNAL DE JUSTIÇA DO " + request.getTribunal());

            docx.createParagraph();

            // Título
            XWPFParagraph titulo = docx.createParagraph();
            titulo.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runTitulo = titulo.createRun();
            runTitulo.setBold(true);
            runTitulo.setFontSize(13);
            runTitulo.setText(resolverTitulo(request.getTipoDocumento()));

            docx.createParagraph();

            // Número do processo
            XWPFParagraph proc = docx.createParagraph();
            XWPFRun runProc = proc.createRun();
            runProc.setBold(true);
            runProc.setText("Processo nº: " + request.getNumeroProcesso());

            docx.createParagraph();

            // Conteúdo limpo
            XWPFParagraph corpo = docx.createParagraph();
            corpo.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun runCorpo = corpo.createRun();
            runCorpo.setFontSize(11);
            runCorpo.setText(limparMarkdown(conteudo));

            docx.createParagraph();
            docx.createParagraph();

            // Data
            String data = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                    new Locale("pt", "BR")));

            XWPFParagraph dataP = docx.createParagraph();
            dataP.setAlignment(ParagraphAlignment.RIGHT);
            dataP.createRun().setText("Teresina - PI, " + data);

            docx.createParagraph();
            docx.createParagraph();

            // Assinatura
            XWPFParagraph ass = docx.createParagraph();
            ass.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runAss = ass.createRun();
            runAss.setText("_______________________________");
            runAss.addBreak();
            runAss.setBold(true);
            runAss.setText(request.getNomeAdvogado());
            runAss.addBreak();
            runAss.setBold(false);
            runAss.setText("OAB/" + request.getOabNumero());

            docx.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("❌ Erro ao gerar DOCX: {}", e.getMessage());
            return new byte[0];
        }
    }

    public String gerarConteudo(DocumentoRequest request) {
        return assistente.analisar(
            request.getNumeroProcesso(),
            request.getTribunal(),
            "",
            "",
            montarPerguntaPorTipo(request)
        );
    }

    private String limparMarkdown(String texto) {
        return texto
            .replace("**", "")
            .replace("##", "")
            .replace("# ", "")
            .replace("*", "")
            .replace("---", "")
            .trim();
    }

    private String resolverTitulo(TipoDocumento tipo) {
        return switch (tipo) {
            case PETICAO_SIMPLES  -> "PETIÇÃO";
            case RECURSO          -> "RECURSO DE APELAÇÃO";
            case HABEAS_CORPUS    -> "HABEAS CORPUS";
            case MANIFESTACAO     -> "MANIFESTAÇÃO";
            case REQUERIMENTO_OAB -> "REQUERIMENTO À OAB";
        };
    }

    private String montarPerguntaPorTipo(DocumentoRequest request) {
        return switch (request.getTipoDocumento()) {
            case PETICAO_SIMPLES -> """
                Redija uma petição simples completa para o processo %s no tribunal %s
                em nome do cliente %s, representado pelo advogado %s OAB %s.
                Siga o formato jurídico brasileiro padrão. Não use markdown.
                """.formatted(request.getNumeroProcesso(), request.getTribunal(),
                    request.getNomeCliente(), request.getNomeAdvogado(), request.getOabNumero());

            case RECURSO -> """
                Redija um recurso de apelação completo para o processo %s no tribunal %s
                em nome do cliente %s, representado pelo advogado %s OAB %s.
                Inclua: tempestividade, cabimento, mérito e pedido final. Não use markdown.
                """.formatted(request.getNumeroProcesso(), request.getTribunal(),
                    request.getNomeCliente(), request.getNomeAdvogado(), request.getOabNumero());

            case HABEAS_CORPUS -> """
                Redija um Habeas Corpus completo para o processo %s no tribunal %s
                em nome do paciente %s, impetrado pelo advogado %s OAB %s.
                Inclua: cabimento, constrangimento ilegal, mérito e liminar. Não use markdown.
                """.formatted(request.getNumeroProcesso(), request.getTribunal(),
                    request.getNomeCliente(), request.getNomeAdvogado(), request.getOabNumero());

            case MANIFESTACAO -> """
                Redija uma manifestação processual completa para o processo %s no tribunal %s
                em nome do cliente %s, representado pelo advogado %s OAB %s.
                Não use markdown.
                """.formatted(request.getNumeroProcesso(), request.getTribunal(),
                    request.getNomeCliente(), request.getNomeAdvogado(), request.getOabNumero());

            case REQUERIMENTO_OAB -> """
                Redija um requerimento formal à OAB relacionado ao processo %s no tribunal %s
                em nome do advogado %s OAB %s representando o cliente %s.
                Não use markdown.
                """.formatted(request.getNumeroProcesso(), request.getTribunal(),
                    request.getNomeAdvogado(), request.getOabNumero(), request.getNomeCliente());
        };
    }
}