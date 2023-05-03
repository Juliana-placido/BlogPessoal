package com.generation.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    public String gerarToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Date dataCriacao = Date.from(now);
        Date dataExpiracao = Date.from(now.plus(expiration, ChronoUnit.SECONDS));
        return Jwts.builder()
                .setIssuer("Api de exemplo")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(dataCriacao)
                .setExpiration(dataExpiracao)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean tokenValido(String token, UserDetails userDetails) {
        String usuario = obterUsuarioDoToken(token);
        return (usuario.equals(userDetails.getUsername()) && !tokenExpirado(token));
    }

    public String obterUsuarioDoToken(String token) {
        return extrairClaimDoToken(token, Claims::getSubject);
    }

    public Boolean tokenExpirado(String token) {
        Date expirationDate = extrairClaimDoToken(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public <T> T extrairClaimDoToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extrairClaimsDoToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extrairClaimsDoToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
