package br.com.jusradar.identity.application;

import br.com.jusradar.identity.application.dto.RegisterRequest;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.domain.UsuarioRole;
import br.com.jusradar.identity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void registrar(RegisterRequest request) {
        repository.findByEmail(request.email()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        });

        Usuario usuario = Usuario.builder()
            .nome(request.nome())
            .email(request.email())
            .senha(passwordEncoder.encode(request.senha()))
            .role(UsuarioRole.ADVOGADO)
            .criadoEm(LocalDateTime.now())
            .build();

        repository.save(usuario);
    }
}
