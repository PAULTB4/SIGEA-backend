package com.zentry.sigea.security;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class JwtBlacklistService {
    // Token -> Expiration
    private final ConcurrentHashMap<String, Date> blacklist = new ConcurrentHashMap<>();

    public JwtBlacklistService() {
        // Programar limpieza automática cada minuto
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            this::removeExpiredTokens,
            1, 1, TimeUnit.MINUTES
        );
    }

    // Agregar token a la blacklist
    public void add(String token, Date expiration) {
        blacklist.put(token, expiration);
    }

    // Verificar si el token está en blacklist
    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token);
    }

    // Eliminar tokens expirados de la blacklist
    private void removeExpiredTokens() {
        Date now = new Date();
        blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}
