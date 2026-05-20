package br.com.jusradar.identity.application;

import br.com.jusradar.identity.application.dto.LoginRequest;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.domain.UsuarioRole;
import br.com.jusradar.identity.infra.jwt.JwtService;
import br.com.jusradar.identity.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginService service;

    @Test
    void deveAutenticarEDevolverTokenJwt() {
        UUID id = UUID.randomUUID();
        var usuario = Usuario.builder()
            .id(id)
            .email("maria@email.com")
            .senha("hash")
            .role(UsuarioRole.ADVOGADO)
            .build();

        when(repository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("12345678", "hash")).thenReturn(true);
        when(jwtService.gerarToken(id, "maria@email.com", "ADVOGADO")).thenReturn("jwt-token");

        var response = service.login(new LoginRequest("maria@email.com", "12345678"));

        assertThat(response.token()).isEqualTo("jwt-token");
        verify(jwtService).gerarToken(id, "maria@email.com", "ADVOGADO");
    }

    @Test
    void deveRejeitarSenhaInvalida() {
        var usuario = Usuario.builder()
            .email("maria@email.com")
            .senha("hash")
            .role(UsuarioRole.ADVOGADO)
            .build();

        when(repository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThatThrownBy(() -> service.login(new LoginRequest("maria@email.com", "errada")))
            .isInstanceOf(ResponseStatusException.class);
    }
}
