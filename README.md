# SIGEA

# Tabla de Contenido

- [SIGEA](#sigea)
- [Tabla de Contenido](#tabla-de-contenido)
- [I. Descripcion](#i-descripcion)
- [II. Arquitectura](#ii-arquitectura)
- [III. Patrones de Diseño](#iii-patrones-de-diseño)
- [IV. Tecnologias](#iv-tecnologias)
- [V. Estructura de Carpetas del Proyecto](#v-estructura-de-carpetas-del-proyecto)
- [VI. Instalacion y Ejecucion del Codigo Fuente](#vi-instalacion-y-ejecucion-del-codigo-fuente)
  - [6.1. Clonar Repositorio](#61-clonar-repositorio)
  - [6.2. Antes de Ejecutar el Proyecto](#62-antes-de-ejecutar-el-proyecto)
    - [6.2.1. Configuracion de Archivo `.env`](#621-configuracion-de-archivo-env)
    - [6.2.2. Configuracion de `psql`](#622-configuracion-de-psql)
      - [6.2.2.1. En Windows](#6221-en-windows)
      - [6.2.2.2. En Linux (Ubuntu, Debian, etc.)](#6222-en-linux-ubuntu-debian-etc)
      - [6.2.2.3. En macOS](#6223-en-macos)
  - [6.3. Ejecutar el proyecto](#63-ejecutar-el-proyecto)
- [VII. Uso de las API´s](#vii-uso-de-las-apis)
- [VIII. Colaboradores](#viii-colaboradores)
- [IX. Licencia](#ix-licencia)


# I. Descripcion

# II. Arquitectura

# III. Patrones de Diseño

# IV. Tecnologias

# V. Estructura de Carpetas del Proyecto

# VI. Instalacion y Ejecucion del Codigo Fuente

## 6.1. Clonar Repositorio

Para clonar con `GIT` ejecuta en tu terminal:

```bash
git clone https://github.com/PAULTB4/SIGEA-backend.git
```

Las ramas principales son:

- `main` (estable / produccion):
- `develop` (menos estable / desarrollo):

## 6.2. Antes de Ejecutar el Proyecto

Antes de poder ejecutar el proyecto debes realizar unas configuraciones iniciales.

### 6.2.1. Configuracion de Archivo `.env`

Antes de ejecutar el proyecto, crea un archivo `.env` junto al archivo `README.md` y `pom.xml`.

Dentro debes especificar lo siguiente:

```ini
# Miscelaneous Variables
APPLICATION_NAME=sigea
SERVER_PORT={Puerto del servidor. def: 16001}
SPRING_PROFILES_ACTIVE=local # Cambiar a prod cuando este desplegado

# Hibernate Variables
HIBERNATE_DDL_AUTO={update / create-drop}

# PostgreSQL Variables
DB_URL=jdbc:postgresql://{database host}:{database port: def: 5432}/{Nombre de la base de datos}
DB_HOST={database host}
DB_PORT={database port: def: 5432}
DB_NAME={Nombre de la base de datos}
DB_USERNAME={Nombre del usuario de la base de datos}
DB_PASSWORD={Contraseña del usuario de la base de datos}
DB_ADMIN_USERNAME={Nombre de un usuario sysdba en postgreSQL. def: postgres}
DB_ADMIN_USER_PASSWORD={Contraseña del usuario sysdba de postgreSQL}

# OpenAPI Variables
API_DOCS_PATH={def: /v3/api-docs}
API_UI_PATH={Ruta para la documentacion OpenAPI. def: /swagger-ui/index.html}

# Configuracion de .sql
# hay 3: always, never, embedded
SQL_INIT_MODE={always / never / embedded}
SQL_INIT_DATA_LOCATIONS=classpath:{ruta a tu archivo data.sql desde resources/, def: }
SQL_INIT_SCHEMA_LOCATIONS=classpath:{ruta a tu archivo schema.sql desde resources/, def: }

# SecretKey para validacion de tokens
SECURITY_JWT_SECRET={Coloca aqui un string de minimo 32 caracteres}

# Configuracion de notificaciones
EMAIL_ENABLED=true
EMAIL_FROM={Email que enviara los correos}
EMAIL_NOMBRE_REMITENTE={Nombre con que el que se enviara el email}

MAIL_HOST={Nombre del host mail}
MAIL_PORT={Puerto del host mail}

# Puedes obtener estas credenciales en: https://app.brevo.com/settings/keys/smtp
SMTP_USER={Usuario SMTP}
SMTP_PASS={Contraseña de usuario SMTP}
SMTP_AUTH={true / false}
SMTP_STARTTLS_ENABLE={true / false}
SMTP_STARTTLS_REQUIRED={true / false}
SMTP_CONNECTIONTIMEOUT={Tiempo en ms}
SMTP_TIMEOUT={Tiempo en ms}
SMTP_WRITETIMEOUT={Tiempo en ms}

SMTP_SSL_TRUST={}
SMTP_SSL_CHECKSERVERIDENTITY={true / false}

MAIL_DEFAULT_ENCODING={def: UTF-8}
MAIL_MIME_CHARSET={def: UTF-8}

# Almacenamiento de Medios

# Configuración de Almacenamiento Externo (Supabase, S3, etc)
# Si deseas usar Supabase u otro almacenamiento externo para certificados o archivos, agrega las siguientes variables:

# SUPABASE
SUPABASE_URL={URL de tu proyecto Supabase}
SUPABASE_KEY={Clave secreta de tu proyecto Supabase}
SUPABASE_BUCKET={Nombre del bucket donde se almacenarán los archivos}


# Ejemplo de configuración para Supabase:
# SUPABASE_URL=https://xxxx.supabase.co
# SUPABASE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
# SUPABASE_BUCKET=certificados


MEDIA_LOCATION={Ubicacion de almacenamiento de medios local. def: ./uploads}

# Dominios o Enlaces Publicos
SIGEA_PUBLIC_BACKEND_DOMAIN={Dominio publico del Backend del Proyecto para configuración CORS}
SIGEA_PUBLIC_FRONTEND_DOMAIN={Dominio publico del Frondent del Proyecto para configuración CORS}
SIGEA_ALLOWED_ORIGIN_BACKEND_LOCALHOST_PATH={Dominio localhost del backend para configuracion CORS}
SIGEA_ALLOWED_ORIGIN_FRONTEND_LOCALHOST_PATH={Dominio localhost del frontend para configuracion CORS}

SIGEA_PUBLIC_BACKEND_REPOSITORY={Enlace al repositorio del proyecto}

# Configuracion GCP recomendada para trabajar en local
SPRING_CLOUD_GCP_SQL_ENABLED=false
SPRING_CLOUD_GCP_PUBSUB_ENABLED=false
SPRING_CLOUD_GCP_CORE_ENABLED=false

# MecadoPago Variables
MERCADO_PAGO_PUBLIC_KEY={Llave publica de MercadoPago}
MERCADO_PAGO_ACCESS_TOKEN={Token de MercadoPago}
```

Recuerda reemplazar con tus variables entre los  `{}`  .

### 6.2.2. Configuracion de `psql`

#### 6.2.2.1. En Windows

1. Ubica el directorio donde está psql. Si instalaste PostgreSQL con el instalador oficial, suele estar en:

```bash
C:\Program Files\PostgreSQL\<version>\bin
```

Ejemplo:

```bash
C:\Program Files\PostgreSQL\16\bin
```

2. Abre Panel de control → Sistema → Configuración avanzada del sistema
3. Clic en Variables de entorno
4. En Variables del sistema, selecciona Path → Editar
5. Agrega la ruta:

```bash
C:\Program Files\PostgreSQL\<version>\bin
```

6. Acepta todo y abre una nueva terminal (`cmd` o `PowerShell`).

7. Verificar dentro de la terminal (si no funciona en VSCode cierra y vuelve a abrir):

```bash
psql --version
```

Deberia mostrarte:

```bash
psql (PostgreSQL) <version>
```

#### 6.2.2.2. En Linux (Ubuntu, Debian, etc.)

Cuando instalas PostgreSQL vía apt, psql suele quedar ya en el PATH.
Si no lo está, puedes hacerlo manualmente.

1. Encontrar psql:

```bash
which psql
```

Si no aparece, busca:

```bash
sudo find / -name psql 2>/dev/null
```

Suele estar en:

```bash
/usr/bin/psql
```

2. Añadir al PATH (si fuera necesario)

Edita tu archivo `~/.bashrc` o `~/.zshrc`, ejecutando:

```bash
nano ~/.bashrc
```

Agrega (ajusta la ruta si es distinta):

```bash
export PATH="$PATH:/usr/bin"
```

Luego recarga:

```bash
source ~/.bashrc
```

3. Verifica:

```bash
psql --version
```

#### 6.2.2.3. En macOS

1. Instalado con Homebrew. Si usas Homebrew, generalmente ya queda en el PATH, pero si no:

Ubicación clásica:

```bash
/usr/local/bin/psql        ← Intel Macs
/opt/homebrew/bin/psql     ← Apple Silicon (M1/M2/M3)
```

2. Añadir al PATH

```bash
Edita ~/.zshrc (zsh es el shell por defecto):
```

```bash
nano ~/.zshrc
```

3. Agrega según tu arquitectura:

    3.1. Apple Silicon (M1/M2/M3):

    ```bash
    export PATH="/opt/homebrew/bin:$PATH"
    ```

    3.2. Intel:

    ```bash
    export PATH="/usr/local/bin:$PATH"
    ```

4. Recarga:

```bash
source ~/.zshrc
```

5. Verifica:

```bash
psql --version
```

## 6.3. Ejecutar el proyecto

Para ejecutar el proyecto debes usar los siguientes comandos dependiendo de tu Sistema Operativo:

- `Windows`:
  
```bash
.\scripts\run_sigea_windows.bat
```

- `Linux / MacOS`: 

    1. Asegúrate de que el script sea ejecutable:

    ```bash
    chmod +x run.sh
    ```

    2. Ejecuta:

    ```bash
    ./scripts/run_sigea_linux_mac.sh
    ```

Una vez ejecutado el proyecto accedemos al siguiente link para poder ver la documentacion de la API:

```http
http://{host}:{port}/swagger-ui/swagger-ui/index.html#/
```

Ajusta la ruta modificando por tu host y puerto

# VII. Uso de las API´s

# VIII. Colaboradores

# IX. Licencia

Este proyecto esta bajo la licencia del 