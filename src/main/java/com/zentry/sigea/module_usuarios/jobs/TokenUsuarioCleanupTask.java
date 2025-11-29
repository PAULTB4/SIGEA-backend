package com.zentry.sigea.module_usuarios.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.services.TokenUsuarioService;

@Component
public class TokenUsuarioCleanupTask {
    private final TokenUsuarioService tokenUsuarioService;

    public TokenUsuarioCleanupTask(TokenUsuarioService tokenUsuarioService) {
        this.tokenUsuarioService = tokenUsuarioService;
    }

    // Ejecuta cada hora
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void limpiarTokensExpirados() {
        tokenUsuarioService.deleteExpiredTokens();
    }
}
