package br.com.jusradar.monitoramento.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarMonitoramentoRequest {
    private String numeroProcesso;
    private String tribunal;
    private String documentoCliente;
}