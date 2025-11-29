package com.zentry.sigea.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    @Value("${security.jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;

    public String generateToken(
        String email , 
        String usuarioId ,
        List<?> roles
    ){
        return Jwts.builder()
            .setSubject(email)
            .claim("roles", roles.stream().map(Object::toString).collect(Collectors.toList()))
            .claim("usuarioId", usuarioId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)) // El token expira en 1 hora
            .signWith(getSigningKey())
            .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean isExpired(String token) {
        try {
            Date exp = extractClaims(token).getExpiration();
            return exp.before(new Date());
        } catch (Exception e) {
            return true;
        } 
    }

    // Detectar si el token está por expirar
    public boolean willExpireSoon(String token) {
        Date exp = extractClaims(token).getExpiration();
        long diff = exp.getTime() - System.currentTimeMillis();
        return diff < (2 * 60 * 1000); // 2 minutos antes de expirar
    }

    public Claims extractClaimsFromExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); 
        } catch (ExpiredJwtException e) {
            // Si el token está expirado, igual puedes obtener los claims desde el exception
            return e.getClaims();
        }
    }

}
