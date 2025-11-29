@echo off
setlocal enabledelayedexpansion

REM === Cargar .env ===
for /f "usebackq tokens=1,2 delims==" %%a in (".env") do (
    if not "%%a"=="" (
        set %%a=%%b
    )
)

echo === Creando archivo pgpass.conf ===

set PGPASSFILE=%APPDATA%\postgresql\pgpass.conf
set PGPASSDIR=%APPDATA%\postgresql

if exist "%PGPASSFILE%" (
    del "%PGPASSFILE%"
    echo Archivo de credenciales de PostgreSQL anterior eliminado.
)

if not exist "%PGPASSDIR%" (
    mkdir "%PGPASSDIR%"
    echo Directorio de archivo de credenciales de PostgreSQL creado.
)

<nul set /p=%DB_HOST%:%DB_PORT%:*:%DB_ADMIN_USERNAME%:%DB_ADMIN_USER_PASSWORD%>"%PGPASSFILE%"
echo Archivo pgpass creado.

echo Probando conexion con psql:
psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d postgres -c "\conninfo"
if %ERRORLEVEL% neq 0 (
    echo ERROR: No se pudo conectar a PostgreSQL con la configuracion actual.
    exit /b 1
) else (
    echo Conexion exitosa a PostgreSQL.
)

echo === Verificando base de datos ===

for /f %%i in ('psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -tAc "SELECT 1 FROM pg_database WHERE datname='%DB_NAME%';"') do set EXISTS=%%i

if "%EXISTS%"=="1" (
    echo La base de datos ya existe.
) else (
    psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -c "CREATE DATABASE %DB_NAME%;"
    echo Base de datos creada.
)

echo === Creando usuario si no existe ===

psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = '%DB_USERNAME%') THEN CREATE USER %DB_USERNAME% WITH ENCRYPTED PASSWORD '%DB_PASSWORD%'; END IF; END $$;"

echo Usuario de base de datos listo.

psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "CREATE EXTENSION IF NOT EXISTS pgcrypto;"
echo === Extension pgcrypto instalada correctamente ===

echo === Asignando permisos ===

psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "GRANT CONNECT ON DATABASE %DB_NAME% TO %DB_USERNAME%;"
psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "GRANT SELECT ON ALL TABLES IN SCHEMA public TO %DB_USERNAME%;"
psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "GRANT INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO %DB_USERNAME%;"
psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO %DB_USERNAME%;"
psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO %DB_USERNAME%;"
psql -U %DB_ADMIN_USERNAME% -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO %DB_USERNAME%;"

echo Permisos asignados.
echo === Instalacion de base de datos completada ===