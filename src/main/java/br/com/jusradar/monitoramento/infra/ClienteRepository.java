package br.com.jusradar.monitoramento.infra;

import br.com.jusradar.monitoramento.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
=======
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByDocumento(String documento);
}
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
