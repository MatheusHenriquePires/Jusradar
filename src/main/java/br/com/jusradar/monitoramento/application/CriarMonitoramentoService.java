package br.com.jusradar.monitoramento.application;

import br.com.jusradar.monitoramento.api.dto.CriarMonitoramentoRequest;
import br.com.jusradar.monitoramento.domain.Cliente;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.monitoramento.infra.ClienteRepository;
import br.com.jusradar.monitoramento.infra.MonitoramentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarMonitoramentoService {

    private final MonitoramentoRepository monitoramentoRepository;
    private final ClienteRepository clienteRepository;

    public Monitoramento criar(CriarMonitoramentoRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Monitoramento monitoramento = Monitoramento.builder()
                .numeroProcesso(request.getNumeroProcesso())
                .tribunal(request.getTribunal())
                .cliente(cliente)
                .ultimaConsulta(LocalDateTime.now())
                .build();

        return monitoramentoRepository.save(monitoramento);
    }
}