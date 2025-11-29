package com.zentry.sigea.module_notificaciones.infrastructure.adapters;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.ports.IUsuarioGateway;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

/**
 * Adaptador que implementa el gateway para obtener datos de usuarios
 * Este es el único punto de comunicación con el módulo de usuarios
 */
@Component
public class UsuarioGatewayAdapter implements IUsuarioGateway {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioGatewayAdapter.class);
    
    private final UsuarioJPARepository usuarioRepository;
    
    public UsuarioGatewayAdapter(UsuarioJPARepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<String> obtenerCorreoUsuario(String usuarioId) {
        try {
            return usuarioRepository.findById(UUID.fromString(usuarioId))
                .map(UsuarioEntity::getCorreo);
        } catch (Exception e) {
            logger.error("Error al obtener correo del usuario {}: {}", usuarioId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> obtenerTelefonoUsuario(String usuarioId) {
        try {
            return usuarioRepository.findById(UUID.fromString(usuarioId))
                .map(usuario -> {
                    String extension = usuario.getExtensionTelefonica();
                    String telefono = usuario.getTelefono();
                    
                    if (telefono == null || telefono.trim().isEmpty()) {
                        return null;
                    }
                    
                    // Formato internacional: +código número
                    // Si no tiene extensión, usar +51 por defecto (Perú)
                    String ext = (extension != null && !extension.trim().isEmpty()) ? extension : "+51";
                    
                    // Limpiar el teléfono de espacios y guiones
                    telefono = telefono.replaceAll("[\\s-]", "");
                    
                    // Si el teléfono ya empieza con +, devolverlo tal cual
                    if (telefono.startsWith("+")) {
                        return telefono;
                    }
                    
                    return ext + telefono;
                });
        } catch (Exception e) {
            logger.error("Error al obtener teléfono del usuario {}: {}", usuarioId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> obtenerNombreUsuario(String usuarioId) {
        try {
            return usuarioRepository.findById(UUID.fromString(usuarioId))
                .map(usuario -> {
                    String nombres = usuario.getNombres() != null ? usuario.getNombres() : "";
                    String apellidos = usuario.getApellidos() != null ? usuario.getApellidos() : "";
                    return (nombres + " " + apellidos).trim();
                });
        } catch (Exception e) {
            logger.error("Error al obtener nombre del usuario {}: {}", usuarioId, e.getMessage());
            return Optional.empty();
        }
    }
}
