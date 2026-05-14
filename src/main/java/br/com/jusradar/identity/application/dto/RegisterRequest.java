package br.com.jusradar.identity.application.dto;

public record RegisterRequest(
    String nome,
    String email,
    String senha
) {
}