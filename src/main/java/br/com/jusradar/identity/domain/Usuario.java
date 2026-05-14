package br.com.jusradar.identity.domain;

import jakarta.persistence.*;
import lombok.*;
import br.com.jusradar.monitoramento.domain.Monitoramento;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String role;

    @OneToMany(mappedBy = "advogado")
    private List<Monitoramento> monitoramentos;
}