# 🔔 Guía de Integración - Sistema de Notificaciones Automáticas

## 📋 Descripción

Este documento explica cómo integrar el sistema de notificaciones automáticas en los módulos del proyecto SIGEA.

Las notificaciones se envían **automáticamente** mediante un sistema de **eventos** cuando ocurren acciones importantes en el sistema (pagos, inscripciones, certificados, comunicaciones).

---

## 🎯 Arquitectura

```
Módulo (Pagos/Certificados/etc.)
    ↓
Publica Evento (1 línea de código)
    ↓
Event Listener (en module_notificaciones)
    ↓
Envío Automático (WhatsApp/Email/Sistema)
```

**Ventajas:**
- ✅ Desacoplamiento total entre módulos
- ✅ Notificaciones automáticas sin llamadas HTTP
- ✅ Procesamiento asíncrono (no bloquea operaciones principales)
- ✅ Fácil mantenimiento y extensibilidad

---

## 📦 Eventos Disponibles

### 1. InscripcionCreadaEvent

**Cuándo usarlo:** Al crear una inscripción exitosamente

**Ubicación:** `com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent`

**Parámetros:**
- `usuarioId` (String): ID del usuario inscrito
- `actividadId` (String): ID de la actividad
- `inscripcionId` (String): ID de la inscripción creada
- `fechaInscripcion` (LocalDateTime): Fecha de la inscripción

**Ejemplo de uso:**
```java
import org.springframework.context.ApplicationEventPublisher;
import com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent;
import java.time.LocalDateTime;

@Service
public class InscripcionService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public InscripcionService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public String crearInscripcion(CrearInscripcionRequest request) {
        // 1. Guardar la inscripción
        String inscripcionId = inscripcionRepository.save(inscripcion);
        
        // 2. Publicar evento (notificación automática)
        eventPublisher.publishEvent(new InscripcionCreadaEvent(
            request.getUsuarioId(),
            request.getActividadId(),
            inscripcionId,
            LocalDateTime.now()
        ));
        
        return inscripcionId;
    }
}
```

**Notificación enviada:**
- 📱 Canal: WhatsApp
- 📝 Título: "✅ Inscripción confirmada"
- 📄 Mensaje: "Te has inscrito exitosamente a la actividad. ¡Te esperamos!"

---

### 2. PagoCompletadoEvent

**Cuándo usarlo:** Al completar un pago exitosamente

**Ubicación:** `com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent`

**Parámetros:**
- `usuarioId` (String): ID del usuario que pagó
- `pagoId` (String): ID del pago
- `monto` (BigDecimal): Monto pagado
- `actividadId` (String): ID de la actividad relacionada
- `fechaPago` (LocalDateTime): Fecha del pago

**Ejemplo de uso:**
```java
import org.springframework.context.ApplicationEventPublisher;
import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagoService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public PagoService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public String procesarPago(PagoRequest request) {
        // 1. Procesar y guardar el pago
        Pago pago = pagoRepository.save(...);
        
        // 2. Publicar evento (notificación automática)
        eventPublisher.publishEvent(new PagoCompletadoEvent(
            pago.getUsuarioId(),
            pago.getId(),
            pago.getMonto(),
            pago.getActividadId(),
            LocalDateTime.now()
        ));
        
        return pago.getId();
    }
}
```

**Notificación enviada:**
- 📧 Canal: Email (para comprobantes)
- 📝 Título: "💰 Pago confirmado"
- 📄 Mensaje: "Tu pago de S/. 50.00 ha sido procesado exitosamente. ¡Gracias!"

---

### 3. CertificadoGeneradoEvent

**Cuándo usarlo:** Al generar un certificado para un usuario

**Ubicación:** `com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent`

**Parámetros:**
- `usuarioId` (String): ID del usuario
- `certificadoId` (String): ID del certificado generado
- `actividadId` (String): ID de la actividad
- `fechaGeneracion` (LocalDateTime): Fecha de generación

**Ejemplo de uso:**
```java
import org.springframework.context.ApplicationEventPublisher;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import java.time.LocalDateTime;

@Service
public class CertificadoService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public CertificadoService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public String generarCertificado(String usuarioId, String actividadId) {
        // 1. Generar el certificado
        Certificado certificado = certificadoRepository.save(...);
        
        // 2. Publicar evento (notificación automática)
        eventPublisher.publishEvent(new CertificadoGeneradoEvent(
            usuarioId,
            certificado.getId(),
            actividadId,
            LocalDateTime.now()
        ));
        
        return certificado.getId();
    }
}
```

**Notificación enviada:**
- 📧 Canal: Email (para documentos oficiales)
- 📝 Título: "🎓 ¡Certificado disponible!"
- 📄 Mensaje: "Tu certificado ha sido generado exitosamente. Ya puedes descargarlo desde tu perfil."

---

### 4. ComunicacionPublicadaEvent

**Cuándo usarlo:** Para enviar comunicaciones masivas (nueva sesión, anuncios, cambios, recordatorios)

**Ubicación:** `com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent`

**Parámetros:**
- `usuariosIds` (List<String>): Lista de IDs de usuarios a notificar
- `actividadId` (String): ID de la actividad relacionada
- `titulo` (String): Título de la comunicación
- `mensaje` (String): Mensaje de la comunicación
- `tipo` (TipoComunicacion): Tipo de comunicación
- `fechaPublicacion` (LocalDateTime): Fecha de publicación

**Tipos de comunicación disponibles:**
```java
public enum TipoComunicacion {
    NUEVA_SESION,           // Nueva sesión programada
    CAMBIO_SESION,          // Cambio de fecha/hora de sesión
    CANCELACION_SESION,     // Cancelación de sesión
    ANUNCIO_ACTIVIDAD,      // Anuncio general de la actividad
    RECORDATORIO,           // Recordatorio de sesión próxima
    COMUNICADO_GENERAL      // Comunicado general
}
```

**Ejemplo de uso:**
```java
import org.springframework.context.ApplicationEventPublisher;
import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent;
import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent.TipoComunicacion;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SesionService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public SesionService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public void crearSesion(SesionRequest request) {
        // 1. Crear la sesión
        Sesion sesion = sesionRepository.save(...);
        
        // 2. Obtener usuarios inscritos en la actividad
        List<String> usuariosIds = inscripcionRepository
            .findByActividadId(request.getActividadId())
            .stream()
            .map(Inscripcion::getUsuarioId)
            .toList();
        
        // 3. Publicar evento (notificaciones automáticas)
        eventPublisher.publishEvent(new ComunicacionPublicadaEvent(
            usuariosIds,
            request.getActividadId(),
            "Nueva sesión programada",
            "Se ha programado una nueva sesión para el " + sesion.getFecha(),
            TipoComunicacion.NUEVA_SESION,
            LocalDateTime.now()
        ));
    }
}
```

**Notificación enviada:**
- 📱 Canal: Sistema (notificación en la app)
- 📝 Título: "📅 Nueva sesión programada"
- 📄 Mensaje: "Se ha programado una nueva sesión para el 15/11/2025"

---

## 🔧 Pasos de Integración

### Paso 1: Inyectar ApplicationEventPublisher

En tu servicio, agrega `ApplicationEventPublisher` como dependencia:

```java
@Service
public class TuServicio {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public TuServicio(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
```

### Paso 2: Importar el Evento

Importa el evento que necesites:

```java
import com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent;
// o
import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
// o
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
// o
import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent;
```

### Paso 3: Publicar el Evento

Después de completar tu operación principal (guardar pago, crear inscripción, etc.), publica el evento:

```java
eventPublisher.publishEvent(new EventoQueNecesites(...));
```

### Paso 4: ¡Listo!

La notificación se enviará **automáticamente** en segundo plano sin bloquear tu operación principal.

---

## ✅ Ejemplo Completo: Módulo de Pagos

```java
package com.zentry.sigea.module_pagos.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
import com.zentry.sigea.module_pagos.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pagos.core.repositories.IPagoRepository;

@Service
public class PagoService {
    
    private final IPagoRepository pagoRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public PagoService(
        IPagoRepository pagoRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.pagoRepository = pagoRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Transactional
    public String procesarPago(PagoRequest request) {
        // 1. Validar el pago
        validarPago(request);
        
        // 2. Crear y guardar el pago
        PagoDomainEntity pago = new PagoDomainEntity(
            request.getUsuarioId(),
            request.getMonto(),
            request.getActividadId()
        );
        
        String pagoId = pagoRepository.save(pago);
        
        // 3. Publicar evento para notificación automática
        // ¡SOLO ESTA LÍNEA! El resto se hace automáticamente
        eventPublisher.publishEvent(new PagoCompletadoEvent(
            request.getUsuarioId(),
            pagoId,
            request.getMonto(),
            request.getActividadId(),
            LocalDateTime.now()
        ));
        
        return pagoId;
    }
    
    private void validarPago(PagoRequest request) {
        // Lógica de validación...
    }
}
```

---

## 📊 Canales de Notificación por Tipo de Evento

| Evento | Canal Predeterminado | Razón |
|--------|---------------------|-------|
| InscripcionCreadaEvent | WhatsApp | Confirmación rápida e inmediata |
| PagoCompletadoEvent | Email | Comprobante formal y auditable |
| CertificadoGeneradoEvent | Email | Documento oficial descargable |
| ComunicacionPublicadaEvent | Sistema | Comunicación masiva en la app |

---

## 🔍 Troubleshooting

### No se envían las notificaciones

1. **Verificar que el evento se está publicando:**
   ```java
   logger.info("Publicando evento de inscripción para usuario {}", usuarioId);
   eventPublisher.publishEvent(new InscripcionCreadaEvent(...));
   ```

2. **Verificar logs del listener:**
   Busca en los logs: `"Evento recibido: Nueva inscripción..."`

3. **Verificar configuración de servicios externos:**
   - Email: Variables `EMAIL_ENABLED`, `EMAIL_HOST`, `EMAIL_USERNAME`, `EMAIL_PASSWORD`
   - WhatsApp: Variables `WHATSAPP_ENABLED`, `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN`

### Las notificaciones bloquean la operación principal

**Solución:** Verifica que el listener tenga la anotación `@Async`:
```java
@EventListener
@Async  // ← Importante para procesamiento en segundo plano
public void onPagoCompletado(PagoCompletadoEvent event) {
    // ...
}
```

---

## 📝 Notas Importantes

1. **No afecta transacciones:** Los eventos se publican dentro de la transacción, pero el procesamiento asíncrono ocurre después del commit.

2. **Manejo de errores:** Si falla el envío de la notificación, NO afecta la operación principal (pago, inscripción, etc.). Los errores se registran en los logs.

3. **Desacoplamiento total:** Tu módulo NO necesita conocer nada sobre notificaciones, emails o WhatsApp. Solo publicas eventos.

4. **Extensible:** Agregar un nuevo tipo de notificación solo requiere:
   - Crear un nuevo evento
   - Crear un nuevo listener en `module_notificaciones`
   - NO tocar los otros módulos

---

## 🚀 Resumen

**Para enviar notificaciones automáticas:**

1. Inyecta `ApplicationEventPublisher` en tu servicio
2. Publica el evento apropiado después de tu operación
3. ¡Listo! La notificación se envía automáticamente

**Código mínimo:**
```java
eventPublisher.publishEvent(new EventoApropriado(...));
```

**Resultado:**
- ✅ Notificación guardada en BD
- ✅ Enviada por WhatsApp/Email/Sistema según configuración
- ✅ Procesamiento asíncrono (no bloquea tu operación)
- ✅ Manejo de errores automático

---

**Documentación actualizada:** 14 de noviembre de 2025  
**Versión:** 1.0.0
