package br.com.jusradar.identity.domain;

import jakarta.persistence.*;
import lombok.*;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @UuidGenerator
    private UUID id;

    private String nome;
    private String email;
    private String senha;
    private String role;

    @Column(name = "criado_em")
    private java.time.LocalDateTime criadoEm;

    @OneToMany(mappedBy = "advogado")
    private List<Monitoramento> monitoramentos;
}