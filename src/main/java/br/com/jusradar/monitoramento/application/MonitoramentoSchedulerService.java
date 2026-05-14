package br.com.jusradar.monitoramento.application;

import br.com.jusradar.consulta.infra.datajud.DataJudClient;
import br.com.jusradar.consulta.domain.Processo;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.monitoramento.infra.MonitoramentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoramentoSchedulerService {

    private final MonitoramentoRepository repository;
    private final DataJudClient dataJudClient;

    // roda a cada 60 segundos (ajuste depois para 5 min / 1h)
    @Scheduled(fixedDelay = 60000)
    public void executarMonitoramento() {

        log.info("Iniciando ciclo de monitoramento...");

        List<Monitoramento> lista = repository.findAll();

        for (Monitoramento m : lista) {

            try {
                List<Processo> processos =
                        dataJudClient.buscar(m.getNumeroProcesso(), m.getTribunal());

                if (processos.isEmpty()) {
                    continue;
                }

                Processo atual = processos.get(0);

                boolean mudou = mudouMovimentacao(m, atual);

                if (mudou) {
                    log.info("Mudança detectada no processo {}", m.getNumeroProcesso());

                    m.setUltimaMovimentacao(atual.getSituacao());
                    m.setUltimaConsulta(LocalDateTime.now());

                    repository.save(m);

                    // futuramente: disparar notificação aqui
                }

            } catch (Exception e) {
                log.error("Erro no monitoramento {}: {}", m.getNumeroProcesso(), e.getMessage());
            }
        }

        log.info("Ciclo de monitoramento finalizado.");
    }

    private boolean mudouMovimentacao(Monitoramento m, Processo atual) {
        if (m.getUltimaMovimentacao() == null) return true;

        return !m.getUltimaMovimentacao().equals(atual.getSituacao());
    }
}