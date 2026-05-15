package br.com.jusradar.monitoramento.domain;

import jakarta.persistence.*;
import lombok.*;
import br.com.jusradar.identity.domain.Usuario;
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

    @ManyToOne
    @JoinColumn(name = "advogado_id")
    private Usuario advogado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
