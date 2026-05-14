package br.com.jusradar.identity.application.dto;

public record LoginRequest(
    String email,
    String senha
) {
}