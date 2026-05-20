package br.com.jusradar.identity.application;

import br.com.jusradar.identity.application.dto.RegisterRequest;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.domain.UsuarioRole;
import br.com.jusradar.identity.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterService service;

    @Test
    void deveCadastrarAdvogadoComSenhaCriptografada() {
        var request = new RegisterRequest("Maria Silva", "maria@email.com", "12345678");
        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.senha())).thenReturn("senha-criptografada");

        service.registrar(request);

        var captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());

        Usuario usuario = captor.getValue();
        assertThat(usuario.getNome()).isEqualTo("Maria Silva");
        assertThat(usuario.getEmail()).isEqualTo("maria@email.com");
        assertThat(usuario.getSenha()).isEqualTo("senha-criptografada");
        assertThat(usuario.getRole()).isEqualTo(UsuarioRole.ADVOGADO);
        assertThat(usuario.getCriadoEm()).isNotNull();
    }

    @Test
    void deveBloquearEmailJaCadastrado() {
        var request = new RegisterRequest("Maria Silva", "maria@email.com", "12345678");
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(new Usuario()));

        assertThatThrownBy(() -> service.registrar(request))
            .isInstanceOf(ResponseStatusException.class);

        verify(repository, never()).save(any());
    }
}
