package br.com.jusradar.monitoramento.application;

import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.monitoramento.infra.MonitoramentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonitoramentoService {

    private final MonitoramentoRepository repository;

    public Monitoramento salvar(Monitoramento m) {
        return repository.save(m);
    }

    public List<Monitoramento> listarTodos() {
        return repository.findAll();
    }

    public List<Monitoramento> buscarPorAdvogado(UUID advogadoId) {
        return repository.findByAdvogadoId(advogadoId);
    }
}
