package br.com.jusradar.identity.application;

import br.com.jusradar.identity.application.dto.RegisterRequest;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void registrar(RegisterRequest request) {

        Usuario usuario = Usuario.builder()
            .nome(request.nome())
            .email(request.email())
            .senha(passwordEncoder.encode(request.senha()))
            .role("USER")
            .build();

        repository.save(usuario);
    }
}