package br.com.jusradar.shared.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public class AuthUtils {

    public static String getEmailUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UsuarioAutenticado usuario) {
            return usuario.email();
        }

        if (principal instanceof UserDetails user) {
            return user.getUsername();
        }

        return principal.toString();
    }

    public static UUID getUsuarioIdLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UsuarioAutenticado usuario) {
            return usuario.id();
        }

        throw new IllegalStateException("Usuário autenticado sem identificador");
    }
}
