# Funcionamiento de la Creación y Validación de Certificados en SIGEA

## 1. Creación de Certificado

Cuando se crea un certificado en SIGEA, el proceso sigue estos pasos:

1. **Recepción de la solicitud**: El usuario (o sistema) envía una petición al endpoint `/api/v1/certificaciones/crear`.
2. **Datos requeridos**: Se debe enviar un JSON con los siguientes atributos principales:
   - `asistenciaId`: ID de la asistencia asociada.
   - `tipoCertificado`: Tipo de certificado (`SUBIDO` para archivo adjunto, otro valor para generación automática).
   - `observaciones`: (Opcional) Texto adicional.
   - `nombreArchivo`: (Opcional) Nombre del archivo PDF.
   - (Opcional) Archivo PDF adjunto si el tipo es `SUBIDO`.
3. **Validaciones**: El sistema valida que no exista un certificado previo para la misma asistencia y que los datos sean correctos.
4. **Almacenamiento**: Si es tipo `SUBIDO`, el archivo se almacena en Supabase o el almacenamiento configurado. Si es automático, se genera el PDF.
5. **Notificación**: Se publica un evento y se notifica al usuario sobre la creación del certificado (inicialmente con estado `PENDIENTE`).

## 2. Validación de Certificado

Para validar un certificado, el usuario puede usar el endpoint de validación proporcionando el código de validación único (`codigoValidacion`).

- El sistema busca el certificado asociado a ese código.
- Si existe y es válido, retorna los datos del certificado y su estado.
- Si no existe o está anulado, retorna un error o estado no válido.

## 3. Tipos de Validadores

SIGEA permite distintos tipos de validadores para los certificados:

- **Validador por Código**: El usuario ingresa el código de validación y el sistema verifica la existencia y validez del certificado.
- **Validador por QR**: (Opcional, si está implementado) El usuario escanea un código QR que contiene el código de validación o un enlace directo al validador.
- **Validador por Usuario**: (Opcional) Permite validar certificados asociados a un usuario específico, mostrando todos sus certificados válidos.

> **Nota:** La lógica de validación puede extenderse según las necesidades del sistema, permitiendo validadores personalizados o integración con otros sistemas.

---

**Resumen:**
- La creación de certificados puede ser automática o mediante archivo adjunto.
- La validación se realiza principalmente por código, pero puede ampliarse a otros métodos.
- El sistema notifica automáticamente al usuario en cada paso relevante.

**OJO**
-Al subir el archivo del local que no se muy pesado
-Aqui solo crearas el certificado para su respectiva validacion okeis
**Estrutura de crear certificado**
{
  "asistenciaId": "0e62115a-ee1e-4ade-8980-2fdf58382ff4",--obligatorio
  "tipoCertificado": "SUBIDO",----obligatorio mantener 'SUBIDO'
  "observaciones": "Texto opcional",
  "nombreArchivo": "certificadofiis.pdf"
  }

-Cuando valides ricien podras descargar tu certificado 
**Estrutura de validar certificado**
{
  "codigoValidacion": "CERT-62883F2D",--codigo del certificado creado el de arriva, solo se valida una vez
  "tipoValidador": "QR",--hay tipos de validador(ADMIN,ETC--revisar)
  "detalle": "string"
}



------
**.env**

# Supabase Storage
SUPABASE_URL=https://yciwaigyxnhwxoqlelfx.supabase.co
SUPABASE_KEY=sb_secret_PeaQqTauNh9QrUcq-in7Qg_2xCtvsS3
-para supabase