#!/bin/bash

# Ruta del script de instalación
INSTALL_SCRIPT="./scripts/install_database_linux_mac.sh"

# Validar que el script existe
if [ -f "$INSTALL_SCRIPT" ]; then
    echo "Ejecutando script de instalacion de base de datos..."
    chmod +x "$INSTALL_SCRIPT"
    "$INSTALL_SCRIPT"
else
    echo "Script de instalación no encontrado en $INSTALL_SCRIPT"
fi

# Ejecutar Spring Boot con Maven Wrapper
echo "Iniciando SIGEA..."
./mvnw spring-boot:run