@echo off
SET INSTALL_SCRIPT=scripts\install_database_windows.bat

REM Validar que el script existe
IF EXIST "%INSTALL_SCRIPT%" (
    echo Ejecutando script de instalacion de base de datos...
    CALL "%INSTALL_SCRIPT%"
) ELSE (
    echo Script de instalacion no encontrado en %INSTALL_SCRIPT%
)

REM Ejecutar Spring Boot con Maven Wrapper
echo Iniciando SIGEA...
CALL mvnw spring-boot:run