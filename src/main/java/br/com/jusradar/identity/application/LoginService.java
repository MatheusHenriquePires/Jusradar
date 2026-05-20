package br.com.jusradar.identity.application;

import br.com.jusradar.identity.application.dto.AuthResponse;
import br.com.jusradar.identity.application.dto.LoginRequest;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.infra.jwt.JwtService;
import br.com.jusradar.identity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {

        Usuario usuario = repository.findByEmail(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos"));

        boolean senhaValida = passwordEncoder.matches(request.senha(), usuario.getSenha());

        if (!senhaValida) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmail(), usuario.getRole().name());

        return new AuthResponse(token);
    }
}
