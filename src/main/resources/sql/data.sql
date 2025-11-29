-- 1) Limpiar e insertar roles
-- Usamos DELETE primero para evitar duplicados

DELETE FROM usuario_rol ur
USING usuario u, rol r
WHERE ur.usuario_id = u.id_usuario
  AND ur.rol_id = r.id_rol
  AND (
        (u.correo = 'administrador@sigea.unas.edu.pe' AND r.nombre_rol = 'ADMINISTRADOR') OR
        (u.correo = 'organizador@sigea.unas.edu.pe' AND r.nombre_rol = 'ORGANIZADOR') OR
        (u.correo = 'participante@sigea.unas.edu.pe' AND r.nombre_rol = 'PARTICIPANTE')
      );

DELETE FROM usuario WHERE correo IN (
    'administrador@sigea.unas.edu.pe',
    'organizador@sigea.unas.edu.pe',
    'participante@sigea.unas.edu.pe'
);

DELETE FROM rol WHERE nombre_rol IN ('ADMINISTRADOR', 'ORGANIZADOR', 'PARTICIPANTE');

-- Insertar roles de manera segura
INSERT INTO rol (nombre_rol, descripcion, created_at, updated_at) 
VALUES 
        ('ADMINISTRADOR', 
        'Usuario con mayor nivel de acceso dentro del sistema. Su función principal es gestionar la plataforma en su totalidad, asegurando su correcto funcionamiento y supervisando las actividades registradas.', 
        NOW(), NOW()),

        ('ORGANIZADOR', 
        'Responsable de la planificación, ejecución y seguimiento de las actividades académicas o institucionales (como cursos, talleres, conferencias o diplomados).', 
        NOW(), NOW()),

        ('PARTICIPANTE', 
        'Es el usuario que se inscribe y asiste a las actividades ofrecidas en el sistema. Puede ser alumno, egresado, docente, personal administrativo o público externo.', 
        NOW(), NOW())
ON CONFLICT (nombre_rol) DO NOTHING;

-- 2) Limpiar e insertar usuarios

-- Inserción de usuarios usando SELECT de roles
INSERT INTO usuario (
    nombres, 
    apellidos , 
    correo, 
    password_hash, 
    created_at , 
    updated_at , 
    telefono , 
    extension_telefonica
)
VALUES (
    'Administrador', 
    'Ordoñez' , 
    'administrador@sigea.unas.edu.pe', 
    crypt('12345678', gen_salt('bf', 10)), 
    now(), 
    now(),
    '900800701' , 
    '+51'
)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario (
    nombres, 
    apellidos , 
    correo, 
    password_hash, 
    created_at , 
    updated_at , 
    telefono , 
    extension_telefonica
)
VALUES (
    'Organizador', 
    'Castro' , 
    'organizador@sigea.unas.edu.pe', 
    crypt('12345678', gen_salt('bf', 10)), 
    now(), 
    now(),
    '976879800' , 
    '+51'
)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario (
    nombres, 
    apellidos , 
    correo, 
    password_hash, 
    created_at , 
    updated_at , 
    telefono , 
    extension_telefonica
)
VALUES (
    'Participante', 
    'Escaly' , 
    'participante@sigea.unas.edu.pe', 
    crypt('12345678', gen_salt('bf', 10)), 
    now(), 
    now(),
    '987600799' , 
    '+51'
)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario_rol (usuario_id, rol_id , asignado_en)
SELECT 
    u.id_usuario,
    r.id_rol , 
    NOW()
FROM 
    usuario u
JOIN rol r ON (
    (u.correo = 'administrador@sigea.unas.edu.pe' AND r.nombre_rol = 'ADMINISTRADOR') OR
    (u.correo = 'organizador@sigea.unas.edu.pe' AND r.nombre_rol = 'ORGANIZADOR') OR
    (u.correo = 'participante@sigea.unas.edu.pe' AND r.nombre_rol = 'PARTICIPANTE')
);