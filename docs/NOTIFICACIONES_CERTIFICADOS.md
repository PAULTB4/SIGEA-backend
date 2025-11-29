# 🎓 Sistema de Notificaciones de Certificados

## 📋 Descripción General

Este documento explica cómo implementar las notificaciones automáticas de certificados en el módulo de certificados. El sistema enviará notificaciones por dos canales cuando se genere un certificado:

- **📊 SISTEMA**: Notificación guardada en la base de datos
- **📧 EMAIL**: Correo electrónico enviado al usuario vía Brevo SMTP

---

## 🏗️ Estructura del Certificado

El evento está basado en la estructura real de `CertificadoEntity`:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | ID del certificado |
| `asistencia` | AsistenciaEntity | Relación con la asistencia del usuario |
| `codigoValidacion` | String (max 50) | Código único para validar el certificado |
| `fechaEmision` | LocalDate | Fecha de emisión del certificado |
| `estadoCertificado` | EstadoCertificadoEntity | Estado actual del certificado |
| `urlPdf` | String (max 300) | URL para descargar el PDF |

---

## 🎯 Estados del Certificado

El sistema soporta **4 estados de certificado**:

| Estado | Descripción | Cuándo Usar |
|--------|-------------|-------------|
| `GENERADO` | Certificado generado | Certificado recién creado en el sistema |
| `EMITIDO` | Certificado emitido | Certificado oficialmente emitido al usuario |
| `VALIDADO` | Certificado validado | Certificado verificado y validado |
| `ANULADO` | Certificado anulado | Certificado cancelado o revocado |

---

## 💻 Implementación en CertificadoService

### 1. Inyectar el ApplicationEventPublisher

```java
@Service
@RequiredArgsConstructor
public class CertificadoService {
    
    private final CertificadoRepository certificadoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final ActividadRepository actividadRepository;
    private final EstadoCertificadoRepository estadoCertificadoRepository;
    private final ApplicationEventPublisher eventPublisher; // ← Inyectar esto
    
    // ... resto del código
}
```

### 2. Publicar el Evento después de Generar el Certificado

#### Ejemplo Completo: Generar Certificado

```java
@Transactional
public CertificadoEntity generarCertificado(String asistenciaId) {
    // 1. Obtener la asistencia
    AsistenciaEntity asistencia = asistenciaRepository.findById(asistenciaId)
        .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada"));
    
    // 2. Obtener la actividad relacionada
    ActividadEntity actividad = actividadRepository.findById(asistencia.getActividadId())
        .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
    
    // 3. Generar código de validación único
    String codigoValidacion = generarCodigoValidacionUnico();
    
    // 4. Obtener estado GENERADO
    EstadoCertificadoEntity estadoGenerado = estadoCertificadoRepository
        .findByCodigo("GENERADO")
        .orElseThrow(() -> new RuntimeException("Estado GENERADO no encontrado"));
    
    // 5. Crear el certificado
    CertificadoEntity certificado = new CertificadoEntity();
    certificado.setAsistencia(asistencia);
    certificado.setCodigoValidacion(codigoValidacion);
    certificado.setFechaEmision(LocalDate.now());
    certificado.setEstadoCertificado(estadoGenerado);
    certificado.setUrlPdf(null); // Se genera después
    certificado.setCreatedAt(LocalDateTime.now());
    certificado.setUpdatedAt(LocalDateTime.now());
    
    certificado = certificadoRepository.save(certificado);
    
    // 6. Generar PDF (proceso asíncrono)
    String urlPdf = generarPdfCertificado(certificado);
    certificado.setUrlPdf(urlPdf);
    certificado = certificadoRepository.save(certificado);
    
    // 7. Publicar el evento de notificación
    eventPublisher.publishEvent(new CertificadoGeneradoEvent(
        asistencia.getUsuarioId(),                              // Usuario que recibe el certificado
        certificado.getId().toString(),                         // ID del certificado
        actividad.getId().toString(),                           // ID de la actividad
        actividad.getTitulo(),                                  // Título de la actividad
        certificado.getCodigoValidacion(),                      // Código de validación
        certificado.getFechaEmision(),                          // Fecha de emisión
        CertificadoGeneradoEvent.EstadoCertificado.GENERADO,   // Estado
        certificado.getUrlPdf(),                                // URL del PDF
        asistencia.getId().toString(),                          // ID de asistencia
        LocalDateTime.now()                                     // Fecha de generación
    ));
    
    return certificado;
}

/**
 * Genera un código de validación único de 20 caracteres
 */
private String generarCodigoValidacionUnico() {
    return UUID.randomUUID().toString()
        .replace("-", "")
        .substring(0, 20)
        .toUpperCase();
}
```

#### Ejemplo: Emitir Certificado (cambio de estado)

```java
@Transactional
public CertificadoEntity emitirCertificado(String certificadoId) {
    // 1. Obtener el certificado
    CertificadoEntity certificado = certificadoRepository.findById(certificadoId)
        .orElseThrow(() -> new EntityNotFoundException("Certificado no encontrado"));
    
    // 2. Cambiar estado a EMITIDO
    EstadoCertificadoEntity estadoEmitido = estadoCertificadoRepository
        .findByCodigo("EMITIDO")
        .orElseThrow(() -> new RuntimeException("Estado EMITIDO no encontrado"));
    
    certificado.setEstadoCertificado(estadoEmitido);
    certificado.setUpdatedAt(LocalDateTime.now());
    certificado = certificadoRepository.save(certificado);
    
    // 3. Obtener datos para el evento
    AsistenciaEntity asistencia = certificado.getAsistencia();
    ActividadEntity actividad = actividadRepository.findById(asistencia.getActividadId())
        .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
    
    // 4. Publicar evento de emisión
    eventPublisher.publishEvent(new CertificadoGeneradoEvent(
        asistencia.getUsuarioId(),
        certificado.getId().toString(),
        actividad.getId().toString(),
        actividad.getTitulo(),
        certificado.getCodigoValidacion(),
        certificado.getFechaEmision(),
        CertificadoGeneradoEvent.EstadoCertificado.EMITIDO,    // Estado EMITIDO
        certificado.getUrlPdf(),
        asistencia.getId().toString(),
        LocalDateTime.now()
    ));
    
    return certificado;
}
```

---

## 📧 Contenido del Email que Recibirá el Usuario

Cuando se publique el evento, el usuario recibirá un email con el siguiente formato:

```
🎓 ¡Felicidades! Tu certificado está listo

══════════════════════════════════════════

🎯 Actividad: Programación Web Avanzada
📋 Estado: Certificado generado
📅 Fecha de emisión: 15/11/2024

🔐 Código de validación:
   ABC123XYZ456789QWERT

📥 Descarga tu certificado:
   https://sigea.nilver.store/certificados/download/12345

══════════════════════════════════════════

💡 Puedes validar tu certificado usando el código de validación.
📌 Guarda este código para futuras referencias.

¡Felicitaciones por tu logro! 🎉

---
SIGEA - Sistema de Gestión
```

---

## 🔧 Configuración Requerida

### 1. Import Requerido

```java
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import org.springframework.context.ApplicationEventPublisher;
```

### 2. Estados en Base de Datos

Asegúrate de tener los siguientes registros en la tabla `estado_certificado`:

```sql
INSERT INTO estado_certificado (codigo, etiqueta) VALUES 
('GENERADO', 'Certificado Generado'),
('EMITIDO', 'Certificado Emitido'),
('VALIDADO', 'Certificado Validado'),
('ANULADO', 'Certificado Anulado');
```

---

## ✅ Checklist de Implementación

- [ ] Inyectar `ApplicationEventPublisher` en tu servicio de certificados
- [ ] Identificar el método donde se genera/crea el certificado
- [ ] Generar un código de validación único (max 50 caracteres)
- [ ] Crear el certificado con todos los campos requeridos
- [ ] Generar el PDF y obtener la URL (puede ser asíncrono)
- [ ] Después de guardar el certificado, publicar el evento
- [ ] Elegir el estado correcto según el proceso (GENERADO, EMITIDO, VALIDADO)
- [ ] Proporcionar todos los datos requeridos (10 parámetros)
- [ ] Probar generando un certificado y verificar que llegue el email

---

## 🎨 Parámetros del Evento

El constructor de `CertificadoGeneradoEvent` requiere **10 parámetros en orden**:

| # | Parámetro | Tipo | Descripción | Origen |
|---|-----------|------|-------------|---------|
| 1 | usuarioId | String | ID del usuario | `asistencia.getUsuarioId()` |
| 2 | certificadoId | String | ID del certificado | `certificado.getId().toString()` |
| 3 | actividadId | String | ID de la actividad | `actividad.getId().toString()` |
| 4 | actividadTitulo | String | Nombre de la actividad | `actividad.getTitulo()` |
| 5 | codigoValidacion | String | Código único | `certificado.getCodigoValidacion()` |
| 6 | fechaEmision | LocalDate | Fecha de emisión | `certificado.getFechaEmision()` |
| 7 | estado | EstadoCertificado | Estado del certificado | Enum: GENERADO, EMITIDO, VALIDADO, ANULADO |
| 8 | urlPdf | String | URL del PDF | `certificado.getUrlPdf()` (puede ser null) |
| 9 | asistenciaId | String | ID de asistencia | `asistencia.getId().toString()` |
| 10 | fechaGeneracion | LocalDateTime | Fecha/hora generación | `LocalDateTime.now()` |

---

## 🚨 Errores Comunes

### ❌ Error: "Cannot resolve symbol EstadoCertificado"
**Causa**: Falta el import o no usas el enum correcto

**Solución**:
```java
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent.EstadoCertificado;

// Usar el enum:
CertificadoGeneradoEvent.EstadoCertificado.GENERADO
```

### ❌ Error: "No suitable constructor found"
**Causa**: Faltan parámetros o están en orden incorrecto

**Solución**: Asegúrate de pasar los **10 parámetros** en el orden exacto mostrado arriba.

### ❌ Error: "String index out of range: 20"
**Causa**: UUID sin guiones tiene 32 caracteres, substring(0, 20) es correcto

**Solución**: Usa el método `generarCodigoValidacionUnico()` del ejemplo.

---

## 🔐 Generación del Código de Validación

### Método 1: UUID Corto (20 caracteres)
```java
private String generarCodigoValidacionUnico() {
    return UUID.randomUUID().toString()
        .replace("-", "")
        .substring(0, 20)
        .toUpperCase();
}
// Resultado: ABC123XYZ456789QWERT
```

### Método 2: Código Alfanumérico (16 caracteres)
```java
private String generarCodigoValidacionUnico() {
    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder codigo = new StringBuilder(16);
    
    for (int i = 0; i < 16; i++) {
        codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
    }
    
    return codigo.toString();
}
// Resultado: A3X9K7M2P5Q8R1N4
```

### Método 3: Código con Prefijo (máx 50 caracteres)
```java
private String generarCodigoValidacionUnico() {
    String prefijo = "CERT";
    String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String aleatorio = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    
    return String.format("%s-%s-%s", prefijo, fecha, aleatorio).toUpperCase();
}
// Resultado: CERT-20241115-A3X9K7M2P5Q8
```

---

## 📞 Validación de Certificados

Para permitir que los usuarios validen certificados con el código:

```java
@GetMapping("/validar/{codigoValidacion}")
public ResponseEntity<CertificadoDTO> validarCertificado(@PathVariable String codigoValidacion) {
    CertificadoEntity certificado = certificadoRepository
        .findByCodigoValidacion(codigoValidacion)
        .orElseThrow(() -> new EntityNotFoundException("Certificado no encontrado"));
    
    // Verificar que no esté anulado
    if ("ANULADO".equals(certificado.getEstadoCertificado().getCodigo())) {
        throw new BadRequestException("Este certificado ha sido anulado");
    }
    
    return ResponseEntity.ok(convertirADTO(certificado));
}
```

---

## 💳 Integración con Pagos de Certificados

Si el certificado requiere un pago previo, el flujo completo sería:

### Paso 1: Usuario Completa Pago por Certificado
```java
// En PagoService
eventPublisher.publishEvent(new PagoCompletadoEvent(
    usuario.getId(),
    pago.getId().toString(),
    pago.getMonto(),
    pago.getMoneda(),
    actividad.getId().toString(),
    actividad.getTitulo(),
    PagoCompletadoEvent.ConceptoPago.CERTIFICADO,  // ← Concepto: Pago por certificado
    null,                                           // No hay sesión específica
    pago.getComprobante(),
    pago.getMetodoPago().getNombreModelo(),
    pago.getFechaPago()
));

// Usuario recibe notificación: "💳 ¡Pago recibido exitosamente! - Concepto: Emisión de certificado"
```

### Paso 2: Sistema Genera el Certificado
```java
// En CertificadoService - después de validar el pago
eventPublisher.publishEvent(new CertificadoGeneradoEvent(
    usuario.getId(),
    certificado.getId().toString(),
    actividad.getId().toString(),
    actividad.getTitulo(),
    certificado.getCodigoValidacion(),
    certificado.getFechaEmision(),
    CertificadoGeneradoEvent.EstadoCertificado.GENERADO,
    certificado.getUrlPdf(),
    asistencia.getId().toString(),
    LocalDateTime.now()
));

// Usuario recibe notificación: "🎓 ¡Felicidades! Tu certificado está listo"
```

**Resultado**: El usuario recibe **2 notificaciones**:
1. Confirmación de pago por el certificado
2. Certificado listo para descargar

---

## 📝 Notas Importantes

1. **Canal Dual**: Siempre se envían 2 notificaciones:
   - Una se guarda en la base de datos (SISTEMA) - **siempre funciona**
   - Otra se envía por email (EMAIL) - **puede fallar si hay problemas de SMTP**

2. **Asincronía**: El envío de notificaciones es asíncrono, no bloquea la generación del certificado

3. **URL del PDF**: Puede ser `null` si el PDF se genera después. La notificación mostrará un mensaje alternativo.

4. **Código de Validación**: Es ÚNICO e IRREPETIBLE. Considera agregar constraint UNIQUE en la base de datos:
   ```sql
   ALTER TABLE certificado ADD CONSTRAINT uk_codigo_validacion UNIQUE (codigo_validacion);
   ```

5. **Relación con Asistencia**: El certificado se genera basado en la asistencia del usuario a las sesiones de la actividad.

6. **Pago por Certificado**: Si tu sistema cobra por certificados, usa el concepto `CERTIFICADO` en `PagoCompletadoEvent`. El usuario recibirá 2 notificaciones: una del pago y otra del certificado generado.

---

## 🎯 Ejemplo Completo de Integración

```java
@Service
@RequiredArgsConstructor
public class CertificadoService {
    
    private final CertificadoRepository certificadoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final ActividadRepository actividadRepository;
    private final EstadoCertificadoRepository estadoCertificadoRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PdfGeneratorService pdfGeneratorService;
    
    @Transactional
    public CertificadoEntity generarCertificado(String asistenciaId) {
        // 1. Validar asistencia
        AsistenciaEntity asistencia = asistenciaRepository.findById(asistenciaId)
            .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada"));
        
        // Validar que el usuario cumple con requisitos (ej: 80% asistencia)
        if (!cumpleRequisitos(asistencia)) {
            throw new BadRequestException("El usuario no cumple los requisitos para el certificado");
        }
        
        // 2. Verificar si ya existe certificado para esta asistencia
        if (certificadoRepository.existsByAsistenciaId(asistenciaId)) {
            throw new BadRequestException("Ya existe un certificado para esta asistencia");
        }
        
        // 3. Obtener actividad
        ActividadEntity actividad = actividadRepository.findById(asistencia.getActividadId())
            .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
        
        // 4. Generar código único
        String codigoValidacion = generarCodigoValidacionUnico();
        
        // 5. Obtener estado
        EstadoCertificadoEntity estadoGenerado = estadoCertificadoRepository
            .findByCodigo("GENERADO")
            .orElseThrow(() -> new RuntimeException("Estado GENERADO no encontrado"));
        
        // 6. Crear certificado
        CertificadoEntity certificado = new CertificadoEntity();
        certificado.setAsistencia(asistencia);
        certificado.setCodigoValidacion(codigoValidacion);
        certificado.setFechaEmision(LocalDate.now());
        certificado.setEstadoCertificado(estadoGenerado);
        certificado.setCreatedAt(LocalDateTime.now());
        certificado.setUpdatedAt(LocalDateTime.now());
        
        certificado = certificadoRepository.save(certificado);
        
        // 7. Generar PDF (puede ser asíncrono)
        try {
            String urlPdf = pdfGeneratorService.generarCertificadoPdf(certificado, actividad, asistencia);
            certificado.setUrlPdf(urlPdf);
            certificado = certificadoRepository.save(certificado);
        } catch (Exception e) {
            logger.error("Error al generar PDF del certificado: {}", e.getMessage());
            // Continuar sin PDF, se generará después
        }
        
        // 8. Publicar evento de notificación
        eventPublisher.publishEvent(new CertificadoGeneradoEvent(
            asistencia.getUsuarioId(),
            certificado.getId().toString(),
            actividad.getId().toString(),
            actividad.getTitulo(),
            certificado.getCodigoValidacion(),
            certificado.getFechaEmision(),
            CertificadoGeneradoEvent.EstadoCertificado.GENERADO,
            certificado.getUrlPdf(),
            asistencia.getId().toString(),
            LocalDateTime.now()
        ));
        
        logger.info("Certificado generado exitosamente: {} para usuario: {}", 
            certificado.getId(), asistencia.getUsuarioId());
        
        return certificado;
    }
    
    private boolean cumpleRequisitos(AsistenciaEntity asistencia) {
        // Implementar lógica de requisitos (ej: 80% de asistencia)
        return asistencia.getPorcentajeAsistencia() >= 80.0;
    }
    
    private String generarCodigoValidacionUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 20)
                .toUpperCase();
        } while (certificadoRepository.existsByCodigoValidacion(codigo));
        
        return codigo;
    }
}
```

---

**Fecha**: 15 de noviembre de 2025  
**Módulo**: Notificaciones de Certificados  
**Estado**: ✅ Implementado y Funcional
