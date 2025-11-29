# 🔔 Sistema de Notificaciones Automáticas - SIGEA

## 📌 Resumen Rápido

El sistema de notificaciones de SIGEA envía **automáticamente** WhatsApp y Emails cuando ocurren eventos importantes en el sistema, utilizando un patrón de **eventos desacoplados**.

### ✨ Características

- ✅ **Notificaciones automáticas** al pagar, inscribirse, generar certificado, etc.
- ✅ **Multi-canal**: WhatsApp (Twilio) + Email (SMTP) + Sistema (interno)
- ✅ **Asíncrono**: No bloquea operaciones principales
- ✅ **Desacoplado**: Los módulos no se conocen entre sí
- ✅ **API Manual disponible**: Para pruebas y casos especiales

---

## 🚀 Cómo Funciona

### Sistema Automático (Eventos)

```java
// En tu servicio (Pagos, Inscripciones, etc.)
// Solo 1 línea de código:

eventPublisher.publishEvent(new PagoCompletadoEvent(
    usuarioId, 
    pagoId, 
    monto, 
    actividadId, 
    LocalDateTime.now()
));

// ¡Y ya! El usuario recibe automáticamente un email o WhatsApp
```

### Sistema Manual (API REST)

```bash
# Para pruebas o casos especiales
POST /api/notificaciones
{
  "usuarioId": "123",
  "titulo": "Pago confirmado",
  "mensaje": "Tu pago fue procesado",
  "canal": "WHATSAPP"
}
```

---

## 📦 Eventos Disponibles

| Evento | Cuándo se dispara | Canal | Ejemplo |
|--------|-------------------|-------|---------|
| **InscripcionCreadaEvent** | Al inscribirse a una actividad | Sistema | "✅ Inscripción confirmada" |
| **PagoCompletadoEvent** | Al completar un pago | Sistema | "💰 Pago de S/. 50.00 confirmado" |
| **CertificadoGeneradoEvent** | Al generar certificado | Sistema | "🎓 Certificado disponible" |
| **ComunicacionPublicadaEvent** | Nueva sesión/anuncio | Sistema | "📅 Nueva sesión programada" |

---

## 🔧 Integración Rápida

### Paso 1: Inyecta el publisher

```java
@Service
public class TuServicio {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public TuServicio(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
```

### Paso 2: Publica el evento

```java
public String crearInscripcion(Request request) {
    // 1. Tu lógica de negocio
    String id = repository.save(inscripcion);
    
    // 2. Publicar evento (1 línea)
    eventPublisher.publishEvent(new InscripcionCreadaEvent(
        usuarioId, actividadId, id, LocalDateTime.now()
    ));
    
    return id;
}
```

### Paso 3: ¡Listo!

La notificación se envía **automáticamente** en segundo plano por WhatsApp/Email sin bloquear tu operación.

---

## 📚 Documentación Completa

- **[Guía de Integración](INTEGRACION_EVENTOS_NOTIFICACIONES.md)** - Ejemplos detallados de todos los eventos
- **[Arquitectura](ARQUITECTURA_NOTIFICACIONES.md)** - Diagramas y patrones aplicados
- **[Manual de Uso](NOTIFICACIONES_MULTICANAL.md)** - Configuración y troubleshooting

---

## 🎯 Módulos Integrados

| Módulo | Estado | Funcionalidad |
|--------|--------|---------------|
| **Inscripciones** | ✅ Integrado | Notificación automática al inscribirse |
| **Actividades** | ✅ Integrado | Método para enviar comunicaciones masivas |
| **Pagos** | 📝 Pendiente | Ver guía de integración |
| **Certificados** | 📝 Pendiente | Ver guía de integración |
| **Sesiones** | 📝 Pendiente | Ver guía de integración |

---

## ⚙️ Configuración

### Variables de Entorno Requeridas

```bash
# Email
EMAIL_ENABLED=false  # true en producción
EMAIL_HOST=smtp.gmail.com
EMAIL_USERNAME=tu_email@gmail.com
EMAIL_PASSWORD=tu_app_password

# WhatsApp (Twilio)
WHATSAPP_ENABLED=false  # true en producción
TWILIO_ACCOUNT_SID=ACxxxxx
TWILIO_AUTH_TOKEN=xxxxx
TWILIO_WHATSAPP_FROM=whatsapp:+14155238886
```

### Desarrollo Local

Para desarrollo, deja los servicios deshabilitados:
```bash
EMAIL_ENABLED=false
WHATSAPP_ENABLED=false
```

Las notificaciones se guardarán en BD pero NO se enviarán por WhatsApp/Email.

---

## 🏗️ Arquitectura

```
┌──────────────────────────────────────────────────────────┐
│                      MÓDULOS                              │
│  Inscripciones │ Pagos │ Certificados │ Actividades      │
└──────────────────────────────────────────────────────────┘
                           │
                    Publican eventos
                           ↓
┌──────────────────────────────────────────────────────────┐
│              MODULE_NOTIFICACIONES                        │
│  ┌──────────────────────────────────────────────────┐   │
│  │          Event Listeners (Automático)             │   │
│  │  • InscripcionEventListener                       │   │
│  │  • PagoEventListener                              │   │
│  │  • CertificadoEventListener                       │   │
│  │  • ComunicacionEventListener                      │   │
│  └──────────────────────────────────────────────────┘   │
│                           ↓                               │
│  ┌──────────────────────────────────────────────────┐   │
│  │         NotificacionService                       │   │
│  │  1. Guarda en BD                                  │   │
│  │  2. Obtiene datos del usuario (Gateway)          │   │
│  │  3. Envía por canal (Email/WhatsApp/Sistema)     │   │
│  └──────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────┘
                           ↓
        ┌──────────────────┼──────────────────┐
        ↓                  ↓                  ↓
   PostgreSQL      Gmail/SMTP         Twilio WhatsApp
```

---

## 🔒 Principios SOLID Aplicados

- **S** (Single Responsibility): Cada listener maneja UN tipo de notificación
- **O** (Open/Closed): Agregar nuevos eventos no requiere modificar código existente
- **L** (Liskov Substitution): Implementaciones de servicios son intercambiables
- **I** (Interface Segregation): Interfaces pequeñas y específicas (ports)
- **D** (Dependency Inversion): Dependencia de abstracciones (eventos, ports)

---

## 🧪 Testing

### Probar Eventos Automáticos

```java
@Test
public void debeEnviarNotificacionAlCrearInscripcion() {
    // Arrange
    CrearInscripcionRequest request = new CrearInscripcionRequest(...);
    
    // Act
    String inscripcionId = inscripcionService.crearInscripcion(request);
    
    // Assert
    verify(eventPublisher).publishEvent(any(InscripcionCreadaEvent.class));
}
```

### Probar Listeners

```java
@Test
public void debeCrearNotificacionAlRecibirEvento() {
    // Arrange
    InscripcionCreadaEvent event = new InscripcionCreadaEvent(...);
    
    // Act
    listener.onInscripcionCreada(event);
    
    // Assert
    verify(notificacionService).crearNotificacion(any());
}
```

---

## 📊 Métricas y Monitoreo

Los logs incluyen información detallada:

```
INFO  InscripcionService - Publicando evento de inscripción para usuario 123
INFO  InscripcionEventListener - Evento recibido: Nueva inscripción para usuario 123
INFO  WhatsAppService - Enviando WhatsApp a +51999999999
INFO  WhatsAppService - WhatsApp enviado exitosamente. SID: SMxxxxxxx
```

---

## 🆘 Troubleshooting

### No se envían notificaciones

1. Verificar que el evento se publica: `eventPublisher.publishEvent(...)`
2. Verificar logs del listener: Buscar "Evento recibido"
3. Verificar configuración: `EMAIL_ENABLED=true`, `WHATSAPP_ENABLED=true`
4. Verificar credenciales: Twilio SID, Email password

### Notificaciones bloquean la operación

Verificar que el listener tenga `@Async`:
```java
@EventListener
@Async  // ← Importante
public void onPagoCompletado(PagoCompletadoEvent event) { ... }
```

---

## 🎓 Ejemplos Completos

Ver **[INTEGRACION_EVENTOS_NOTIFICACIONES.md](INTEGRACION_EVENTOS_NOTIFICACIONES.md)** para:
- ✅ Ejemplo completo de módulo de Pagos
- ✅ Ejemplo completo de módulo de Certificados
- ✅ Ejemplo completo de módulo de Sesiones
- ✅ Casos de uso avanzados

---

**Última actualización:** 14 de noviembre de 2025  
**Versión:** 1.0.0  
**Autor:** Sistema SIGEA
