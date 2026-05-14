package br.com.jusradar.monitoramento.infra;

import br.com.jusradar.monitoramento.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}