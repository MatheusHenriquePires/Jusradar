package br.com.jusradar.notificacao.application;

import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.notificacao.application.dto.DocumentoRequest;
import br.com.jusradar.notificacao.application.dto.TipoDocumento;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final JavaMailSender mailSender;
    private final AssistenteJuridicoService assistente;
    private final GeradorDocumentoService geradorDocumento;

    @Value("${spring.mail.username}")
    private String remetente;

    public void notificarMovimentacao(Monitoramento m) {
        notificarCliente(m);
        notificarAdvogado(m);
    }

    private void notificarCliente(Monitoramento m) {
        try {
            String destinatario = m.getCliente() != null && m.getCliente().getEmail() != null
                ? m.getCliente().getEmail()
                : "becaatelierm@gmail.com";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(destinatario);
            message.setSubject("⚖️ Atualização no seu processo");
            message.setText(montarCorpoCliente(m));

            mailSender.send(message);
            log.info("📧 Email cliente enviado para: {}", destinatario);

        } catch (Exception e) {
            log.error("❌ Erro ao enviar email para cliente: {}", e.getMessage());
        }
    }

    private void notificarAdvogado(Monitoramento m) {
        try {
            String destinatario = m.getAdvogado() != null
                ? m.getAdvogado().getEmail()
                : "matheustv124@gmail.com";

            // Análise da IA
            String analise = assistente.analisar(
                m.getNumeroProcesso(),
                m.getTribunal(),
                "",
                m.getUltimaMovimentacao(),
                "Analise a última movimentação e indique os próximos passos urgentes."
            );

            // Monta request para gerar documentos
            DocumentoRequest docRequest = new DocumentoRequest();
            docRequest.setNumeroProcesso(m.getNumeroProcesso());
            docRequest.setTribunal(m.getTribunal());
            docRequest.setNomeAdvogado(m.getAdvogado() != null
                ? m.getAdvogado().getNome() : "Advogado Responsável");
            docRequest.setOabNumero("PI-XXXXX");
            docRequest.setNomeCliente(m.getCliente() != null
                ? m.getCliente().getNome() : "Cliente");
            docRequest.setTipoDocumento(TipoDocumento.MANIFESTACAO);

            byte[] pdf  = geradorDocumento.gerarPdf(docRequest, analise);
            byte[] docx = geradorDocumento.gerarDocx(docRequest, analise);

            // Email com anexos
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject("⚖️ JusRadar — Movimentação detectada + Documentos");
            helper.setText(montarCorpoAdvogado(m, analise));

            helper.addAttachment(
                "manifestacao_" + m.getNumeroProcesso() + ".pdf",
                new ByteArrayResource(pdf),
                "application/pdf"
            );
            helper.addAttachment(
                "manifestacao_" + m.getNumeroProcesso() + ".docx",
                new ByteArrayResource(docx),
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

            mailSender.send(message);
            log.info("📧 Email advogado com documentos enviado para: {}", destinatario);

        } catch (Exception e) {
            log.error("❌ Erro ao enviar email para advogado: {}", e.getMessage());
        }
    }

    private String montarCorpoCliente(Monitoramento m) {
        return """
            Olá!

            Seu processo teve uma nova movimentação.

            📋 Detalhes:
            • Processo:      %s
            • Tribunal:      %s
            • Movimentação:  %s
            • Data:          %s

            Entre em contato com seu advogado para mais informações.

            —
            JusRadar · Monitoramento Judicial Automático
            """.formatted(
                m.getNumeroProcesso(),
                m.getTribunal(),
                m.getUltimaMovimentacao(),
                m.getUltimaConsulta()
            );
    }

    private String montarCorpoAdvogado(Monitoramento m, String analise) {
        return """
            Olá, Dr(a)!

            O JusRadar detectou uma nova movimentação em um processo que você monitora.

            📋 Dados do processo:
            • Número:        %s
            • Tribunal:      %s
            • Movimentação:  %s
            • Detectado em:  %s

            ─────────────────────────────────────
            🤖 ANÁLISE JURÍDICA — Assistente JusRadar
            ─────────────────────────────────────

            %s

            ─────────────────────────────────────
            📎 Em anexo: Manifestação em PDF e DOCX pronta para protocolar.

            —
            JusRadar · Monitoramento Judicial com Inteligência Artificial
            """.formatted(
                m.getNumeroProcesso(),
                m.getTribunal(),
                m.getUltimaMovimentacao(),
                m.getUltimaConsulta(),
                analise
            );
    }
}