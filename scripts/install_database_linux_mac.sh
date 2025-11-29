#!/bin/bash
set -e  # Salir si hay cualquier error
set -u  # Salir si se usa una variable no definida

# Cargar variables de entorno desde archivo .env
export $(grep -v '^#' .env | xargs)

DB_NAME="$DB_NAME"
DB_USERNAME="$DB_USERNAME"
DB_PASSWORD="$DB_PASSWORD"
DB_ADMIN_USERNAME="$DB_ADMIN_USERNAME"
DB_ADMIN_USER_PASSWORD="$DB_ADMIN_USER_PASSWORD"
DB_HOST="$DB_HOST"
DB_PORT="$DB_PORT"

echo "=== Creando archivo de credenciales de PostgreSQL ==="

PGPASS_FILE="$HOME/.pgpass"
printf "%s\n" "$DB_HOST:$DB_PORT:*:$DB_ADMIN_USERNAME:$DB_ADMIN_USER_PASSWORD" > "$PGPASS_FILE"
chmod 600 "$PGPASS_FILE"
echo "Archivo de credenciales de PostgreSQL creado."

# Validar conexión a PostgreSQL
echo "=== Probando conexion a PostgreSQL ==="
if ! psql -U "$DB_ADMIN_USERNAME" -h "$DB_HOST" -p "$DB_PORT" -d postgres -c "\conninfo" >/dev/null 2>&1; then
    echo "ERROR: No se pudo conectar a PostgreSQL con las configuraciones actuales."
    exit 1
fi
echo "Conexion exitosa."

echo "=== Verificando y creando base de datos si no existe ==="

EXISTS=$(psql -U "$DB_ADMIN_USERNAME" -h "$DB_HOST" -p "$DB_PORT" -tAc "SELECT 1 FROM pg_database WHERE datname='$DB_NAME';" | tr -d '[:space:]')

if [ "$EXISTS" == "1" ]; then
    echo "La base de datos ya existe."
else
    psql -U "$DB_ADMIN_USERNAME" -h "$DB_HOST" -p "$DB_PORT" -c "CREATE DATABASE $DB_NAME;"
    echo "Base de datos creada."
fi

echo "=== Creando usuario si no existe ==="
psql -v ON_ERROR_STOP=1 -U "$DB_ADMIN_USERNAME" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -c "
DO \$\$ 
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = '$DB_USERNAME') THEN 
        CREATE USER $DB_USERNAME WITH ENCRYPTED PASSWORD '$DB_PASSWORD';
    END IF; 
END 
\$\$;
"
echo "Usuario de base de datos listo."

echo "=== Asignando permisos ==="
psql -v ON_ERROR_STOP=1 -U "$DB_ADMIN_USERNAME" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" <<EOF
CREATE EXTENSION IF NOT EXISTS pgcrypto;
GRANT CONNECT ON DATABASE $DB_NAME TO $DB_USERNAME;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO $DB_USERNAME;
GRANT INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO $DB_USERNAME;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO $DB_USERNAME;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO $DB_USERNAME;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO $DB_USERNAME;
EOF

echo "Permisos asignados correctamente."
echo "=== Instalacion de base de datos completada ==="
