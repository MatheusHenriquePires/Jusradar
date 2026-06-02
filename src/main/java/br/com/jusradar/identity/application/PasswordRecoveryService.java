package br.com.jusradar.identity.application;

import br.com.jusradar.identity.domain.PasswordResetToken;
import br.com.jusradar.identity.domain.Usuario;
import br.com.jusradar.identity.repository.PasswordResetTokenRepository;
import br.com.jusradar.identity.repository.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private static final int TOKEN_EXPIRATION_MINUTES = 30;

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void solicitarRecuperacao(String email) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String rawToken = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
            String tokenHash = hashToken(rawToken);

            tokenRepository.save(PasswordResetToken.builder()
                .usuario(usuario)
                .tokenHash(tokenHash)
                .expiraEm(LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES))
                .criadoEm(LocalDateTime.now())
                .build());

            enviarEmailRecuperacao(usuario, rawToken);
        });
    }

    public void redefinirSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = tokenRepository.findByTokenHash(hashToken(token))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalido ou expirado"));

        if (resetToken.getUsadoEm() != null || resetToken.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalido ou expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        resetToken.setUsadoEm(LocalDateTime.now());
        tokenRepository.save(resetToken);
    }

    private void enviarEmailRecuperacao(Usuario usuario, String rawToken) {
        try {
            String link = frontendUrl + "/reset-password?token=" + rawToken;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(usuario.getEmail());
            message.setSubject("JusRadar - Recuperacao de senha");
            message.setText("""
                Ola, %s.

                Recebemos uma solicitacao para redefinir sua senha.

                Clique no link abaixo para criar uma nova senha:
                %s

                Este link expira em %d minutos.

                Se voce nao solicitou isso, pode ignorar este email.
                """.formatted(usuario.getNome(), link, TOKEN_EXPIRATION_MINUTES));

            mailSender.send(message);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Nao foi possivel enviar o email de recuperacao");
        }
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Falha ao gerar hash do token", exception);
        }
    }
}
