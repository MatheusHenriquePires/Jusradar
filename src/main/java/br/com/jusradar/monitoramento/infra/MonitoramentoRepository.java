package br.com.jusradar.monitoramento.infra;

import br.com.jusradar.monitoramento.domain.Monitoramento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MonitoramentoRepository extends JpaRepository<Monitoramento, UUID> {

    List<Monitoramento> findByAdvogadoId(UUID advogadoId);
}
