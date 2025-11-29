package com.zentry.sigea.module_notificaciones.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zentry.sigea.module_notificaciones.core.entities.CanalNotificacion;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.ports.IEmailService;
import com.zentry.sigea.module_notificaciones.core.ports.IUsuarioGateway;
import com.zentry.sigea.module_notificaciones.core.ports.IWhatsAppService;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.ActualizarNotificacionRequest;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.presentation.models.responseDTO.NotificacionResponse;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.ActualizarEstadoNotificacionUseCase;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.ActualizarNotificacionUseCase;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.CrearNotificacionUseCase;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.EliminarNotificacionUseCase;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.ListarNotificacionesUseCase;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.ObtenerNotificacionPorIdUseCase;

/**
 * Servicio de aplicación para notificaciones
 * Orquesta los casos de uso, envíos por canales externos y convierte entre dominio y DTOs
 */
@Service
public class NotificacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    
    private final CrearNotificacionUseCase crearNotificacionUseCase;
    private final ObtenerNotificacionPorIdUseCase obtenerNotificacionPorIdUseCase;
    private final ListarNotificacionesUseCase listarNotificacionesUseCase;
    private final ActualizarNotificacionUseCase actualizarNotificacionUseCase;
    private final ActualizarEstadoNotificacionUseCase actualizarEstadoNotificacionUseCase;
    private final EliminarNotificacionUseCase eliminarNotificacionUseCase;
    
    // Servicios externos
    private final IEmailService emailService;
    private final IWhatsAppService whatsAppService;
    private final IUsuarioGateway usuarioGateway;

    public NotificacionService(
        CrearNotificacionUseCase crearNotificacionUseCase,
        ObtenerNotificacionPorIdUseCase obtenerNotificacionPorIdUseCase,
        ListarNotificacionesUseCase listarNotificacionesUseCase,
        ActualizarNotificacionUseCase actualizarNotificacionUseCase,
        ActualizarEstadoNotificacionUseCase actualizarEstadoNotificacionUseCase,
        EliminarNotificacionUseCase eliminarNotificacionUseCase,
        IEmailService emailService,
        IWhatsAppService whatsAppService,
        IUsuarioGateway usuarioGateway
    ) {
        this.crearNotificacionUseCase = crearNotificacionUseCase;
        this.obtenerNotificacionPorIdUseCase = obtenerNotificacionPorIdUseCase;
        this.listarNotificacionesUseCase = listarNotificacionesUseCase;
        this.actualizarNotificacionUseCase = actualizarNotificacionUseCase;
        this.actualizarEstadoNotificacionUseCase = actualizarEstadoNotificacionUseCase;
        this.eliminarNotificacionUseCase = eliminarNotificacionUseCase;
        this.emailService = emailService;
        this.whatsAppService = whatsAppService;
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Crea una notificación y SIEMPRE la envía por SISTEMA (BD) + EMAIL
     * Tolerante a fallos: Si email falla, la notificación queda en SISTEMA
     */
    public String crearNotificacion(CrearNotificacionRequest request) {
        try {
            // 1. SIEMPRE guardar en BD primero (SISTEMA)
            NotificacionDomainEntity notificacion = crearNotificacionUseCase.execute(request);
            logger.info("Notificación creada con ID: {} para usuario: {}", 
                notificacion.getId(), notificacion.getUsuarioId());
            
            StringBuilder resultado = new StringBuilder("SISTEMA: OK");
            
            // 2. SIEMPRE intentar enviar por EMAIL (tolerante a fallos)
            try {
                logger.info("=== INICIANDO ENVÍO DE EMAIL para notificación ID: {} ===", notificacion.getId());
                boolean emailEnviado = enviarPorEmail(notificacion);
                if (emailEnviado) {
                    resultado.append(" | EMAIL: OK");
                    logger.info("✅ Notificación enviada exitosamente por SISTEMA + EMAIL ID: {}", 
                        notificacion.getId());
                } else {
                    resultado.append(" | EMAIL: FALLÓ");
                    logger.warn("❌ Email falló para notificación ID: {}, pero se guardó en SISTEMA", 
                        notificacion.getId());
                }
            } catch (Exception e) {
                resultado.append(" | EMAIL: ERROR - ").append(e.getMessage());
                logger.error("💥 ERROR CRÍTICO enviando email para notificación ID: {}, pero se guardó en SISTEMA. Error: {}", 
                    notificacion.getId(), e.getMessage(), e);
            }
            
            // 3. Si el canal especificado es WHATSAPP, también intentar por ahí
            if (notificacion.getCanal() == CanalNotificacion.WHATSAPP) {
                try {
                    boolean whatsappEnviado = enviarPorWhatsApp(notificacion);
                    if (whatsappEnviado) {
                        resultado.append(" | WHATSAPP: OK");
                    } else {
                        resultado.append(" | WHATSAPP: FALLÓ");
                    }
                } catch (Exception e) {
                    resultado.append(" | WHATSAPP: ERROR - ").append(e.getMessage());
                    logger.error("Error enviando WhatsApp para notificación ID: {}: {}", 
                        notificacion.getId(), e.getMessage());
                }
            }
            
            return "Notificación creada y enviada con éxito por canal SISTEMA";
            
        } catch (Exception e) {
            logger.error("Error al crear notificación: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear notificación: " + e.getMessage());
        }
    }
    
    /**
     * Envía la notificación por email
     */
    private boolean enviarPorEmail(NotificacionDomainEntity notificacion) {
        try {
            logger.info("📧 Intentando enviar notificación {} por EMAIL", notificacion.getId());
            
            // Obtener correo del usuario
            logger.info("🔍 Buscando correo para usuario ID: {}", notificacion.getUsuarioId());
            Optional<String> correoOpt = usuarioGateway.obtenerCorreoUsuario(notificacion.getUsuarioId());
            
            if (correoOpt.isEmpty()) {
                logger.error("❌ No se encontró correo para usuario ID: {}", notificacion.getUsuarioId());
                return false;
            }
            
            String correo = correoOpt.get();
            logger.info("✅ Correo encontrado: {} para usuario ID: {}", correo, notificacion.getUsuarioId());
            
            // Obtener nombre del usuario para personalizar
            String nombreUsuario = usuarioGateway.obtenerNombreUsuario(notificacion.getUsuarioId())
                .orElse("Usuario");
            logger.info("👤 Nombre usuario: {}", nombreUsuario);
            
            // Enviar email
            logger.info("📤 Llamando a emailService.enviar() con destino: {}", correo);
            boolean enviado = emailService.enviar(notificacion, correo, nombreUsuario);
            
            if (enviado) {
                logger.info("✅ Email enviado exitosamente a {} para notificación ID: {}", 
                    correo, notificacion.getId());
            } else {
                logger.error("❌ emailService.enviar() retornó FALSE para correo: {}", correo);
            }
            
            return enviado;
            
        } catch (Exception e) {
            logger.error("💥 EXCEPCIÓN al enviar notificación {} por email: {}", 
                notificacion.getId(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Envía la notificación por WhatsApp
     */
    private boolean enviarPorWhatsApp(NotificacionDomainEntity notificacion) {
        try {
            logger.info("Intentando enviar notificación {} por WHATSAPP", notificacion.getId());
            
            // Obtener teléfono del usuario
            Optional<String> telefonoOpt = usuarioGateway.obtenerTelefonoUsuario(notificacion.getUsuarioId());
            
            if (telefonoOpt.isEmpty()) {
                logger.error("No se encontró teléfono para usuario ID: {}", notificacion.getUsuarioId());
                return false;
            }
            
            String telefono = telefonoOpt.get();
            
            // Obtener nombre del usuario para personalizar
            String nombreUsuario = usuarioGateway.obtenerNombreUsuario(notificacion.getUsuarioId())
                .orElse("Usuario");
            
            // Enviar WhatsApp
            boolean enviado = whatsAppService.enviar(notificacion, telefono, nombreUsuario);
            
            if (enviado) {
                logger.info("WhatsApp enviado exitosamente a {} para notificación ID: {}", 
                    telefono, notificacion.getId());
            }
            
            return enviado;
            
        } catch (Exception e) {
            logger.error("Error al enviar notificación {} por WhatsApp: {}", 
                notificacion.getId(), e.getMessage(), e);
            return false;
        }
    }

    public NotificacionResponse obtenerNotificacionPorId(String id) {
        return NotificacionResponse.fromEntity(
            obtenerNotificacionPorIdUseCase.execute(id)
        );
    }

    public List<NotificacionResponse> listarNotificaciones() {
        return listarNotificacionesUseCase.execute()
            .stream()
            .map(NotificacionResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public List<NotificacionResponse> obtenerNotificacionesPorUsuario(String usuarioId) {
        return listarNotificacionesUseCase.executeByUsuarioId(usuarioId)
            .stream()
            .map(NotificacionResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public List<NotificacionResponse> obtenerNotificacionesPorActividad(String actividadId) {
        return listarNotificacionesUseCase.executeByActividadId(actividadId)
            .stream()
            .map(NotificacionResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public NotificacionResponse actualizarNotificacion(String id, ActualizarNotificacionRequest request) {
        return NotificacionResponse.fromEntity(
            actualizarNotificacionUseCase.execute(id, request)
        );
    }

    public void eliminarNotificacion(String id) {
        eliminarNotificacionUseCase.execute(id);
    }

    /**
     * Obtener notificaciones filtradas por tipo de evento
     */
    public List<NotificacionResponse> obtenerNotificacionesPorTipo(String tipoEvento) {
        return listarNotificacionesUseCase.executeByTipoEvento(tipoEvento)
            .stream()
            .map(NotificacionResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Obtener notificaciones de un usuario filtradas por tipo de evento
     */
    public List<NotificacionResponse> obtenerNotificacionesPorUsuarioYTipo(String usuarioId, String tipoEvento) {
        return listarNotificacionesUseCase.executeByUsuarioIdAndTipoEvento(usuarioId, tipoEvento)
            .stream()
            .map(NotificacionResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Marcar una notificación como leída
     */
    public NotificacionResponse marcarComoLeida(String id) {
        return NotificacionResponse.fromEntity(
            actualizarEstadoNotificacionUseCase.marcarComoLeida(id)
        );
    }

    /**
     * Marcar todas las notificaciones de un usuario como leídas
     */
    public void marcarTodasComoLeidas(String usuarioId) {
        actualizarEstadoNotificacionUseCase.marcarTodasComoLeidas(usuarioId);
    }

    /**
     * Eliminar todas las notificaciones de un usuario
     */
    public void eliminarNotificacionesPorUsuario(String usuarioId) {
        eliminarNotificacionUseCase.eliminarPorUsuario(usuarioId);
    }
}
