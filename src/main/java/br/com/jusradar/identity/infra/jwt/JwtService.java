package br.com.jusradar.identity.infra.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(UUID advogadoId, String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("advogadoId", advogadoId.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String getRole(String token) {
        Object role = getClaims(token).get("role");
        return role != null ? role.toString() : "ADVOGADO";
    }

    public UUID getAdvogadoId(String token) {
        Object advogadoId = getClaims(token).get("advogadoId");
        if (advogadoId == null) {
            throw new JwtException("Token sem identificador do advogado");
        }
        return UUID.fromString(advogadoId.toString());
    }

    public boolean validToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration() == null || claims.getExpiration().after(new Date());
        } catch (JwtException exception) {
            return false;
        }
    }
}
