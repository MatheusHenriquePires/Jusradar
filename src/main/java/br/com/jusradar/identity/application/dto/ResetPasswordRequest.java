package br.com.jusradar.identity.application.dto;

public record ResetPasswordRequest(
    String token,
    String senha
) {
}
