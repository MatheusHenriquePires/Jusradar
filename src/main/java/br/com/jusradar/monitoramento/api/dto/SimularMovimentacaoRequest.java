package br.com.jusradar.monitoramento.api.dto;

public record SimularMovimentacaoRequest(
    String numeroProcesso,
    String tribunal,
    String documentoCliente,
    String clienteNome,
    String clienteEmail,
    String movimentacao
) {
}
