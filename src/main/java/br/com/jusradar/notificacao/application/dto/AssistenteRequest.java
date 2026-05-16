package br.com.jusradar.notificacao.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistenteRequest {
    private String numeroProcesso;
    private String tribunal;
    private String pergunta;
}