package br.com.jusradar.identity.infra.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService service = new JwtService();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "secret", "01234567890123456789012345678901");
        ReflectionTestUtils.setField(service, "expiration", 3600000L);
    }

    @Test
    void deveGerarTokenComEmailRoleEAdvogadoId() {
        UUID advogadoId = UUID.randomUUID();

        String token = service.gerarToken(advogadoId, "maria@email.com", "ADVOGADO");

        assertThat(service.validToken(token)).isTrue();
        assertThat(service.getEmail(token)).isEqualTo("maria@email.com");
        assertThat(service.getRole(token)).isEqualTo("ADVOGADO");
        assertThat(service.getAdvogadoId(token)).isEqualTo(advogadoId);
    }
}
