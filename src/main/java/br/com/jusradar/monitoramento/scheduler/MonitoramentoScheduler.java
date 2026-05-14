package br.com.jusradar.monitoramento.scheduler;

import br.com.jusradar.monitoramento.application.MonitoramentoService;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.consulta.infra.datajud.DataJudClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoramentoScheduler {

    private final MonitoramentoService monitoramentoService;
    private final DataJudClient dataJudClient;

    // roda a cada 1 minuto (teste)
    @Scheduled(fixedDelay = 60000)
    public void executar() {

        log.info("🔎 Iniciando monitoramento automático...");

        // simulação multi-tenant (depois vem do JWT)
        Long advogadoId = 1L;

        List<Monitoramento> lista =
                monitoramentoService.buscarPorAdvogado(advogadoId);

        for (Monitoramento m : lista) {

            String documento = String.valueOf(m.getDocumento());
            var processos = dataJudClient.buscar(
                    documento,
                    m.getTribunal()
            );

            if (processos.isEmpty()) continue;

            var novoStatus = processos.get(0).getSituacao();

            if (m.getUltimaMovimentacao() == null ||
                !m.getUltimaMovimentacao().equals(novoStatus)) {

                log.info("📢 Mudança detectada: {}", m.getDocumento());

                m.setUltimaMovimentacao(novoStatus);

                monitoramentoService.salvar(m);

                // FUTURO: disparar notificação aqui
            }
        }
    }
}