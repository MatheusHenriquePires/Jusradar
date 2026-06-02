package br.com.jusradar.monitoramento.domain;

import jakarta.persistence.*;
import lombok.*;
import br.com.jusradar.identity.domain.Usuario;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "monitoramentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Monitoramento {

    @Id
    @UuidGenerator
    private UUID id;

    private String numeroProcesso;
    private String tribunal;

    @Column(name = "documento")
    private String documentoCliente;

    private String ultimaMovimentacao;
    private LocalDateTime ultimaConsulta;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @ManyToOne
<<<<<<< HEAD
=======
    @JsonIgnoreProperties({"monitoramentos"})
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
    @JoinColumn(name = "advogado_id")
    private Usuario advogado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
<<<<<<< HEAD
}
=======
}
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
