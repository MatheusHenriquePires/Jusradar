package br.com.jusradar.monitoramento.infra;

import br.com.jusradar.monitoramento.domain.Monitoramento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoramentoRepository extends JpaRepository<Monitoramento, Long> {

    List<Monitoramento> findByAdvogadoEmail(String email);
}