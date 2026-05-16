package br.com.jusradar.monitoramento.application;

import br.com.jusradar.monitoramento.api.dto.CriarMonitoramentoRequest;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.monitoramento.infra.MonitoramentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarMonitoramentoService {

    private final MonitoramentoRepository monitoramentoRepository;

    public Monitoramento criar(CriarMonitoramentoRequest request) {
        Monitoramento monitoramento = Monitoramento.builder()
                .numeroProcesso(request.getNumeroProcesso())
                .tribunal(request.getTribunal())
                .documentoCliente(request.getDocumentoCliente())
                .criadoEm(LocalDateTime.now())
                .build();

        return monitoramentoRepository.save(monitoramento);
    }
}