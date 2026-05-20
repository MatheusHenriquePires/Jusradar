package br.com.jusradar.shared.security;

import java.util.UUID;

public record UsuarioAutenticado(
    UUID id,
    String email
) {
}
