# 🔔 Sistema de Notificaciones de Pagos

## 📋 Descripción General

Este documento explica cómo implementar las notificaciones automáticas de pagos en el módulo de pagos. El sistema enviará notificaciones por dos canales cuando se complete un pago:

- **📊 SISTEMA**: Notificación guardada en la base de datos
- **📧 EMAIL**: Correo electrónico enviado al usuario vía Brevo SMTP

---

## 🎯 Conceptos de Pago Soportados

El sistema soporta **6 tipos de conceptos de pago**:

| Concepto | Descripción | Ejemplo de Uso |
|----------|-------------|----------------|
| `INSCRIPCION` | Inscripción a actividad | Pago inicial para inscribirse a un curso |
| `SESION` | Pago por sesión individual | Pago por asistir a una sesión específica |
| `CERTIFICADO` | Emisión de certificado | Pago por la generación del certificado |
| `MATERIAL` | Material del curso | Pago por materiales físicos o digitales |
| `MEMBRESIA` | Membresía o suscripción | Pago recurrente de membresía |
| `OTRO` | Otro concepto | Cualquier otro tipo de pago |

---

## 💻 Implementación en PagoService

### 1. Inyectar el ApplicationEventPublisher

```java
@Service
@RequiredArgsConstructor
public class PagoService {
    
    private final PagoRepository pagoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ActividadRepository actividadRepository;
    private final SesionRepository sesionRepository;
    private final ApplicationEventPublisher eventPublisher; // ← Inyectar esto
    
    // ... resto del código
}
```

### 2. Publicar el Evento después de Confirmar el Pago

#### Ejemplo 1: Pago por Inscripción a Actividad

```java
public PagoEntity confirmarPagoInscripcion(String pagoId) {
    // 1. Obtener el pago y marcar como completado
    PagoEntity pago = pagoRepository.findById(pagoId)
        .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    
    pago.setEstadoPago(estadoPagoCompletado);
    pago.setFechaPago(LocalDateTime.now());
    pago = pagoRepository.save(pago);
    
    // 2. Obtener información de la inscripción y actividad
    InscripcionEntity inscripcion = pago.getInscripcion();
    ActividadEntity actividad = actividadRepository.findById(inscripcion.getActividadId())
        .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    
    // 3. Publicar el evento de pago completado
    eventPublisher.publishEvent(new PagoCompletadoEvent(
        inscripcion.getUsuarioId(),                    // ID del usuario que pagó
        pago.getId().toString(),                       // ID del pago
        pago.getMonto(),                               // Monto del pago (BigDecimal)
        pago.getMoneda(),                              // "PEN", "USD", "EUR"
        actividad.getId().toString(),                  // ID de la actividad
        actividad.getTitulo(),                         // Título de la actividad
        PagoCompletadoEvent.ConceptoPago.INSCRIPCION, // Concepto: INSCRIPCION
        null,                                          // No hay sesión específica
        pago.getComprobante(),                         // Número de comprobante
        pago.getMetodoPago().getNombreModelo(),        // Ej: "Tarjeta de Crédito", "Yape"
        pago.getFechaPago()                            // Fecha del pago
    ));
    
    return pago;
}
```

#### Ejemplo 2: Pago por Sesión Individual

```java
public PagoEntity confirmarPagoSesion(String pagoId, String sesionId) {
    // 1. Obtener el pago
    PagoEntity pago = pagoRepository.findById(pagoId)
        .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    
    pago.setEstadoPago(estadoPagoCompletado);
    pago.setFechaPago(LocalDateTime.now());
    pago = pagoRepository.save(pago);
    
    // 2. Obtener información de la sesión y actividad
    SesionEntity sesion = sesionRepository.findById(sesionId)
        .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
    
    ActividadEntity actividad = actividadRepository.findById(sesion.getActividadId())
        .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    
    // 3. Publicar el evento con concepto SESION
    eventPublisher.publishEvent(new PagoCompletadoEvent(
        pago.getInscripcion().getUsuarioId(),          // Usuario que pagó
        pago.getId().toString(),
        pago.getMonto(),
        pago.getMoneda(),
        actividad.getId().toString(),
        actividad.getTitulo(),
        PagoCompletadoEvent.ConceptoPago.SESION,       // Concepto: SESION
        sesion.getTitulo(),                            // ← Título de la sesión
        pago.getComprobante(),
        pago.getMetodoPago().getNombreModelo(),
        pago.getFechaPago()
    ));
    
    return pago;
}
```

#### Ejemplo 3: Pago por Certificado

```java
public PagoEntity confirmarPagoCertificado(String pagoId) {
    // 1. Obtener y completar el pago
    PagoEntity pago = pagoRepository.findById(pagoId)
        .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    
    pago.setEstadoPago(estadoPagoCompletado);
    pago.setFechaPago(LocalDateTime.now());
    pago = pagoRepository.save(pago);
    
    // 2. Obtener actividad relacionada (puede venir de la inscripción)
    InscripcionEntity inscripcion = pago.getInscripcion();
    ActividadEntity actividad = actividadRepository.findById(inscripcion.getActividadId())
        .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    
    // 3. Publicar evento de pago completado
    eventPublisher.publishEvent(new PagoCompletadoEvent(
        inscripcion.getUsuarioId(),
        pago.getId().toString(),
        pago.getMonto(),
        pago.getMoneda(),
        actividad.getId().toString(),
        actividad.getTitulo(),
        PagoCompletadoEvent.ConceptoPago.CERTIFICADO,  // Concepto: CERTIFICADO
        null,                                          // No hay sesión
        pago.getComprobante(),
        pago.getMetodoPago().getNombreModelo(),
        pago.getFechaPago()
    ));
    
    // Usuario recibe: "💳 ¡Pago recibido exitosamente! - Concepto: Emisión de certificado"
    
    // 4. OPCIONAL: Generar certificado automáticamente después del pago
    // certificadoService.generarCertificado(inscripcion.getAsistenciaId());
    // Usuario recibiría segunda notificación: "🎓 ¡Felicidades! Tu certificado está listo"
    
    return pago;
}
```

**Nota**: Si el certificado se genera automáticamente después del pago, el usuario recibirá **2 notificaciones**:
1. **Primera**: Confirmación de pago por certificado
2. **Segunda**: Certificado generado y listo para descargar

---

## 📧 Contenido del Email que Recibirá el Usuario

Cuando se publique el evento, el usuario recibirá un email con el siguiente formato:

### Para Pago de Inscripción:
```
💳 ¡Pago recibido exitosamente!

══════════════════════════════════════════

📋 Concepto: Inscripción a actividad
🎯 Actividad: Programación Web Avanzada

📊 Detalles de la transacción:
──────────────────────────────────────────
💰 Monto pagado: S/. 150.00
💳 Método de pago: Tarjeta de Crédito
📄 Nº Comprobante: COMP-2024-001234
📅 Fecha y hora: 15/11/2024 10:30
──────────────────────────────────────────

✅ Tu transacción ha sido procesada correctamente.
📧 Conserva este comprobante para futuras referencias.

---
SIGEA - Sistema de Gestión
```

### Para Pago de Sesión Individual:
```
💳 ¡Pago recibido exitosamente!

══════════════════════════════════════════

📋 Concepto: Pago por sesión individual
🎯 Actividad: Programación Web Avanzada
📅 Sesión: Introducción a Spring Boot

📊 Detalles de la transacción:
──────────────────────────────────────────
💰 Monto pagado: S/. 50.00
💳 Método de pago: Yape
📄 Nº Comprobante: YAPE-78945612
📅 Fecha y hora: 15/11/2024 14:20
──────────────────────────────────────────

✅ Tu transacción ha sido procesada correctamente.
📧 Conserva este comprobante para futuras referencias.

---
SIGEA - Sistema de Gestión
```

### Para Pago de Certificado:
```
💳 ¡Pago recibido exitosamente!

══════════════════════════════════════════

📋 Concepto: Emisión de certificado
🎯 Actividad: Programación Web Avanzada

📊 Detalles de la transacción:
──────────────────────────────────────────
💰 Monto pagado: S/. 30.00
💳 Método de pago: Transferencia Bancaria
📄 Nº Comprobante: TRANS-2024-567890
📅 Fecha y hora: 15/11/2024 16:45
──────────────────────────────────────────

✅ Tu transacción ha sido procesada correctamente.
📧 Conserva este comprobante para futuras referencias.

---
SIGEA - Sistema de Gestión
```

**Nota**: Después de pagar el certificado, el usuario recibirá una segunda notificación cuando el certificado esté listo para descargar.

---

## 🔧 Configuración Requerida

### 1. Dependencia en pom.xml

Ya está configurada en el proyecto, no necesitas agregar nada.

### 2. Import Requerido

```java
import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
import org.springframework.context.ApplicationEventPublisher;
```

---

## ✅ Checklist de Implementación

- [ ] Inyectar `ApplicationEventPublisher` en tu servicio de pagos
- [ ] Identificar el método donde se confirma/completa el pago
- [ ] Después de guardar el pago completado, publicar el evento
- [ ] Elegir el `ConceptoPago` correcto según el tipo de pago
- [ ] Proporcionar todos los datos requeridos (usuario, monto, actividad, etc.)
- [ ] Para pagos de sesión, incluir el `sesionTitulo`
- [ ] Para otros conceptos, pasar `null` en `sesionTitulo`
- [ ] Probar enviando un pago y verificar que llegue el email

---

## 🎨 Formato de Monedas

El sistema automáticamente formatea las monedas:

| Código | Símbolo Mostrado |
|--------|------------------|
| `PEN` | S/. |
| `USD` | $ |
| `EUR` | € |
| Otros | Código original |

**Ejemplo**: Si envías `monto=150.50` y `moneda="PEN"`, el email mostrará: **S/. 150.50**

---

## 🚨 Errores Comunes

### ❌ Error: "No suitable constructor found"
**Causa**: Faltan parámetros al crear el evento

**Solución**: Asegúrate de pasar los **11 parámetros** en el orden correcto:
1. usuarioId (String)
2. pagoId (String)
3. monto (BigDecimal)
4. moneda (String)
5. actividadId (String)
6. actividadTitulo (String)
7. concepto (ConceptoPago)
8. sesionTitulo (String - puede ser null)
9. comprobante (String)
10. metodoPago (String)
11. fechaPago (LocalDateTime)

### ❌ Error: "Cannot resolve symbol ConceptoPago"
**Causa**: Falta el import

**Solución**:
```java
import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
```

---

## 📞 Soporte

Si tienes dudas sobre la implementación:
- Revisa el código de `PagoEventListener.java` para ver cómo se procesa el evento
- Verifica la configuración de email en `application.properties`
- Revisa los logs con el emoji 💰 para debugging de pagos

---

## 📝 Notas Importantes

1. **Canal Dual**: Siempre se envían 2 notificaciones:
   - Una se guarda en la base de datos (SISTEMA) - **siempre funciona**
   - Otra se envía por email (EMAIL) - **puede fallar si hay problemas de SMTP**

2. **Asincronía**: El envío de notificaciones es asíncrono, no bloquea tu flujo de pago

3. **Tolerancia a Fallos**: Si el email falla, el pago se procesa igual

4. **Emails en Spam**: Es normal que los primeros emails caigan en spam. Los usuarios deben:
   - Marcar como "No es spam"
   - Agregar `sigea@nilver.store` a sus contactos

---

## 🎯 Ejemplo Completo de Integración

```java
@Service
@RequiredArgsConstructor
public class PagoService {
    
    private final PagoRepository pagoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ActividadRepository actividadRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public PagoEntity procesarPago(String pagoId) {
        // 1. Obtener el pago
        PagoEntity pago = pagoRepository.findById(pagoId)
            .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado"));
        
        // 2. Cambiar estado a COMPLETADO
        EstadoPagoEntity estadoCompletado = estadoPagoRepository
            .findByNombreEstado("COMPLETADO")
            .orElseThrow(() -> new RuntimeException("Estado COMPLETADO no encontrado"));
        
        pago.setEstadoPago(estadoCompletado);
        pago.setFechaPago(LocalDateTime.now());
        pago = pagoRepository.save(pago);
        
        // 3. Obtener datos relacionados
        InscripcionEntity inscripcion = pago.getInscripcion();
        ActividadEntity actividad = actividadRepository
            .findById(inscripcion.getActividadId())
            .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
        
        // 4. Publicar evento de notificación
        eventPublisher.publishEvent(new PagoCompletadoEvent(
            inscripcion.getUsuarioId(),
            pago.getId().toString(),
            pago.getMonto(),
            pago.getMoneda(),
            actividad.getId().toString(),
            actividad.getTitulo(),
            PagoCompletadoEvent.ConceptoPago.INSCRIPCION,
            null,
            pago.getComprobante(),
            pago.getMetodoPago().getNombreModelo(),
            pago.getFechaPago()
        ));
        
        // 5. Retornar el pago actualizado
        return pago;
    }
}
```

---

**Fecha**: 15 de noviembre de 2025  
**Módulo**: Notificaciones de Pagos  
**Estado**: ✅ Implementado y Funcional
