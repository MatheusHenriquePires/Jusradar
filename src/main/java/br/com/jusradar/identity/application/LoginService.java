package br.com.jusradar.identity.application;

import br.com.jusradar.identity.application.dto.AuthResponse;
import br.com.jusradar.identity.application.dto.LoginRequest;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.infra.jwt.JwtService;
import br.com.jusradar.identity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {

        Usuario usuario = repository.findByEmail(request.email())
            .orElseThrow();

        boolean senhaValida =
            passwordEncoder.matches(
                request.senha(),
                usuario.getSenha()
            );

        if (!senhaValida) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario);

        return new AuthResponse(token);
    }
}