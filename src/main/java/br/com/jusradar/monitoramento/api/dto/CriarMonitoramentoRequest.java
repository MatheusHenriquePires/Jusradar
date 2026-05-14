package br.com.jusradar.monitoramento.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarMonitoramentoRequest {

    private String numeroProcesso;
    private String tribunal;
    private Long clienteId;
}
