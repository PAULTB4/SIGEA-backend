package com.zentry.sigea.module_usuarios.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.ITokenUsuarioRepository;

@Service
public class TokenUsuarioService {

    private final ITokenUsuarioRepository tokenUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenUsuarioService(
        ITokenUsuarioRepository tokenUsuarioRepository , 
        PasswordEncoder passwordEncoder
    ){
        this.tokenUsuarioRepository = tokenUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenUsuarioDomainEntity createRefreshToken(String usuarioId , Boolean rememberMe){
        TokenUsuarioDomainEntity tokenUsuarioDomainEntity = new TokenUsuarioDomainEntity();
        
        tokenUsuarioDomainEntity.setUsuarioId(usuarioId);
        tokenUsuarioDomainEntity.setToken(
            passwordEncoder.encode(UUID.randomUUID().toString())
        );

        if (rememberMe) {
            tokenUsuarioDomainEntity.setExpiryDate(Instant.now().plus(24 , ChronoUnit.HOURS));
        } else {
            tokenUsuarioDomainEntity.setExpiryDate(Instant.now().plus(1 , ChronoUnit.HOURS));
        }

        return tokenUsuarioRepository.save(tokenUsuarioDomainEntity);
    }

    public TokenUsuarioDomainEntity verifyExpiration(TokenUsuarioDomainEntity tokenUsuarioDomainEntity){
        if (tokenUsuarioDomainEntity.getExpiryDate().isBefore(Instant.now())) {
            tokenUsuarioRepository.deleteByUsuario_Id(tokenUsuarioDomainEntity.getUsuarioId());
            throw new RuntimeException("Refresh token expirado.");
        }

        return tokenUsuarioDomainEntity;
    }

    public void deleteByUsuario_ID(String usuarioId) { 
        tokenUsuarioRepository.deleteByUsuario_Id(usuarioId);
    }

    public TokenUsuarioDomainEntity findByUnhashedTokenAndId(String unhashedToken , String idToken){
        TokenUsuarioDomainEntity tokenUsuarioDomainEntity = tokenUsuarioRepository.findById(idToken).orElseThrow(
            () -> new RuntimeException("No se pudo entontrar el token para el ID especificado")
        );

        if(passwordEncoder.matches(unhashedToken, tokenUsuarioDomainEntity.getToken())){
            return tokenUsuarioDomainEntity;
        }
        else{
            throw new RuntimeException("El token enviado no coincide con el actual en la BD.");
        }
    }

    public void deleteExpiredTokens() {
        Instant now = Instant.now();

        tokenUsuarioRepository.deleteExpiredTokens(now);
    }
}
