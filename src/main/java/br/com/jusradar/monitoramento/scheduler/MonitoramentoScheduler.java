package br.com.jusradar.monitoramento.scheduler;

import br.com.jusradar.monitoramento.application.MonitoramentoService;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.consulta.infra.datajud.DataJudClient;
import br.com.jusradar.notificacao.application.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoramentoScheduler {

    private final MonitoramentoService monitoramentoService;
    private final DataJudClient dataJudClient;
    private final NotificacaoService notificacaoService;

    @Scheduled(cron = "0 0 3 * * *") // todo dia às 3h da manhã
    public void executar() {
        log.info("🔎 [SCHEDULER] Iniciando verificação — {}", LocalDateTime.now());

        List<Monitoramento> lista = monitoramentoService.listarTodos();
        log.info("📋 {} monitoramento(s) encontrado(s)", lista.size());

        for (Monitoramento m : lista) {
            try {
                log.info("🔍 Verificando: {} | Tribunal: {}", m.getDocumentoCliente(), m.getTribunal());

                var processos = dataJudClient.buscar(
                    m.getNumeroProcesso() != null ? m.getNumeroProcesso() : m.getDocumentoCliente(),
                    m.getTribunal()
                );

                m.setUltimaConsulta(LocalDateTime.now());

                if (processos.isEmpty()) {
                    log.info("⚪ Nenhum processo encontrado para: {}", m.getDocumentoCliente());
                    monitoramentoService.salvar(m);
                    continue;
                }

                var novoStatus = processos.get(0).getSituacao();

                if (m.getUltimaMovimentacao() == null ||
                    !m.getUltimaMovimentacao().equals(novoStatus)) {

                    log.info("🟡 MUDANÇA DETECTADA para {} | Antes: {} | Agora: {}",
                        m.getDocumentoCliente(),
                        m.getUltimaMovimentacao(),
                        novoStatus);

                    m.setUltimaMovimentacao(novoStatus);
                    notificacaoService.notificarMovimentacao(m);
                }

                monitoramentoService.salvar(m);

            } catch (Exception e) {
                log.error("❌ Erro ao verificar {}: {}", m.getDocumentoCliente(), e.getMessage());
            }
        }

        log.info("✅ [SCHEDULER] Verificação concluída");
    }
}