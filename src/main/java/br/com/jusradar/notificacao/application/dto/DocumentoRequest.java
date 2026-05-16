package br.com.jusradar.notificacao.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentoRequest {
    private String numeroProcesso;
    private String tribunal;
    private String nomeAdvogado;
    private String oabNumero;
    private String nomeCliente;
    private String pergunta;
    private TipoDocumento tipoDocumento;
}