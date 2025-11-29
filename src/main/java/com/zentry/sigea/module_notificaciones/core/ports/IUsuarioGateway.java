package com.zentry.sigea.module_notificaciones.core.ports;

import java.util.Optional;

/**
 * Puerto de salida para obtener información de usuarios
 * Sin acoplamiento directo al módulo de usuarios
 */
public interface IUsuarioGateway {
    
    /**
     * Obtiene el correo electrónico de un usuario
     * @param usuarioId ID del usuario
     * @return Optional con el correo si existe
     */
    Optional<String> obtenerCorreoUsuario(String usuarioId);
    
    /**
     * Obtiene el teléfono completo de un usuario (con extensión)
     * @param usuarioId ID del usuario
     * @return Optional con el teléfono en formato +código número (ej: +51987654321)
     */
    Optional<String> obtenerTelefonoUsuario(String usuarioId);
    
    /**
     * Obtiene el nombre completo del usuario
     * @param usuarioId ID del usuario
     * @return Optional con el nombre completo
     */
    Optional<String> obtenerNombreUsuario(String usuarioId);
}
