package br.com.jusradar.monitoramento.domain;

import br.com.jusradar.identity.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Monitoramento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroProcesso;
    private String tribunal;

    private String ultimaMovimentacao;
    private LocalDateTime ultimaConsulta;

    @ManyToOne
    @JoinColumn(name = "advogado_id", nullable = false)
    private Usuario advogado;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Object getDocumento() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDocumento'");
    }
}