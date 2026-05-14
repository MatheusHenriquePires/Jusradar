package br.com.jusradar.consulta.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Processo {
    private String numero;
    private String tribunal;
    private String classe;
    private String situacao;
    private String dataUltimaMovimentacao;
    private String orgaoJulgador;
}