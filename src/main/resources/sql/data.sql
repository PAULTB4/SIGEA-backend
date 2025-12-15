-- ============================================
-- SCRIPT DE DATOS INICIALES - SIN DELETE
-- ============================================
-- Este script es SEGURO: solo inserta datos nuevos
-- Si ya existen, NO hace nada (ON CONFLICT DO NOTHING)

--- ############################################################ INSERTAR ROLES ############################################################ 
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
--- ############################################################ INSERTAR ROLES ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR USUARIOS ############################################################ 
INSERT INTO usuario (
    nombres, 
    apellidos, 
    correo, 
    password_hash, 
    dni,
    correo_verificado,
    created_at , 
    updated_at , 
    telefono , 
    extension_telefonica
)
VALUES (
    'Administrador', 
    'Ordoñez', 
    'administrador@sigea.unas.edu.pe', 
    crypt('12345678', gen_salt('bf', 10)), 
    '80787898' , 
    TRUE , 
    now(), 
    now(),
    '900800701' , 
    '+51'
)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario (
    nombres, 
    apellidos, 
    correo, 
    password_hash, 
    dni , 
    correo_verificado,
    created_at , 
    updated_at , 
    telefono , 
    extension_telefonica
)
VALUES (
    'Organizador', 
    'Castro', 
    'organizador@sigea.unas.edu.pe', 
    crypt('12345678', gen_salt('bf', 10)), 
    '67987989' , 
    TRUE , 
    now(), 
    now(),
    '976879800' , 
    '+51'
)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario (
    nombres, 
    apellidos, 
    correo, 
    password_hash, 
    dni , 
    correo_verificado,
    created_at , 
    updated_at , 
    telefono , 
    extension_telefonica
)
VALUES (
    'Participante', 
    'Escaly', 
    'brennisbenjaminn@gmail.com', 
    crypt('12345678', gen_salt('bf', 10)), 
    '70897889' , 
    TRUE , 
    now(), 
    now(), 
    '987600799' , 
    '+51'
)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO usuario (
    nombres,
    apellidos,
    correo,
    password_hash,
    dni,
    correo_verificado,
    telefono,
    extension_telefonica,
    created_at,
    updated_at
)
VALUES
    ('Carlos', 'Ramírez', 'carlos.ramirez@test.com',
    crypt('12345678', gen_salt('bf', 10)),
    '70000001', TRUE, '900000001', '+51', NOW(), NOW()),

    ('María', 'López', 'maria.lopez@test.com',
    crypt('12345678', gen_salt('bf', 10)),
    '70000002', TRUE, '900000002', '+51', NOW(), NOW()),

    ('José', 'Pérez', 'jose.perez@test.com',
    crypt('12345678', gen_salt('bf', 10)),
    '70000003', TRUE, '900000003', '+51', NOW(), NOW()),

    ('Lucía', 'Gómez', 'lucia.gomez@test.com',
    crypt('12345678', gen_salt('bf', 10)),
    '70000004', TRUE, '900000004', '+51', NOW(), NOW()),

    ('Andrés', 'Torres', 'andres.torres@test.com',
    crypt('12345678', gen_salt('bf', 10)),
    '70000005', TRUE, '900000005', '+51', NOW(), NOW())
ON CONFLICT (correo) DO NOTHING;
--- ############################################################ INSERTAR USUARIOS ############################################################ 
--- ================
--- ================
--- ############################################################ ASIGNAR ROL-USUARIO ############################################################ 
INSERT INTO usuario_rol (usuario_id, rol_id, asignado_en)
SELECT 
    u.id_usuario,
    r.id_rol, 
    NOW()
FROM usuario u
JOIN rol r ON (
    (u.correo = 'administrador@sigea.unas.edu.pe' AND r.nombre_rol = 'ADMINISTRADOR') OR
    (u.correo = 'organizador@sigea.unas.edu.pe' AND r.nombre_rol = 'ORGANIZADOR') OR
    (u.correo = 'brennisbenjaminn@gmail.com' AND r.nombre_rol = 'PARTICIPANTE') OR

    (u.correo IN (
        'carlos.ramirez@test.com',
        'maria.lopez@test.com',
        'jose.perez@test.com',
        'lucia.gomez@test.com',
        'andres.torres@test.com',
        'usuario6@test.com',
        'usuario7@test.com'
    ) AND r.nombre_rol = 'PARTICIPANTE')

    OR

    -- 20% ORGANIZADOR
    (u.correo IN (
        'usuario8@test.com',
        'usuario9@test.com'
    ) AND r.nombre_rol = 'ORGANIZADOR')

    OR

    -- 10% ADMINISTRADOR
    (u.correo IN (
        'usuario10@test.com'
    ) AND r.nombre_rol = 'ADMINISTRADOR')
)
WHERE NOT EXISTS (
    SELECT 1 FROM usuario_rol ur
    WHERE ur.usuario_id = u.id_usuario
    AND ur.rol_id = r.id_rol
)
ON CONFLICT DO NOTHING;
--- ############################################################ ASIGNAR ROL-USUARIO ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR TIPOS DE ACTIVIDAD ############################################################ 
INSERT INTO tipo_actividad (nombre_actividad, descripcion, created_at, updated_at)
VALUES 
    ('CURSO', 'Programa de formación estructurado con sesiones programadas y objetivos de aprendizaje definidos.', NOW(), NOW()),
    ('TALLER', 'Actividad práctica de corta duración enfocada en desarrollar habilidades específicas.', NOW(), NOW()),
    ('CONFERENCIA', 'Evento donde expertos presentan temas de interés académico o profesional.', NOW(), NOW()),
    ('SEMINARIO', 'Reunión especializada para el estudio y discusión de temas específicos.', NOW(), NOW()),
    ('DIPLOMADO', 'Programa de formación continua de larga duración con certificación.', NOW(), NOW()),
    ('CONGRESO', 'Evento académico de gran escala con múltiples ponencias y actividades.', NOW(), NOW()),
    ('CHARLA', 'Presentación informal sobre un tema específico de corta duración.', NOW(), NOW()),
    ('WEBINAR', 'Seminario o conferencia realizada en línea.', NOW(), NOW())
ON CONFLICT (nombre_actividad) DO NOTHING;
--- ############################################################ INSERTAR TIPOS DE ACTIVIDAD ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR ESTADOS DE ACTIVIDAD ############################################################ 
INSERT INTO estado_actividad (codigo, etiqueta)
VALUES 
    ('BORRADOR', 'Borrador'),
    ('PUBLICADO', 'Publicado'),
    ('EN_CURSO', 'En Curso'),
    ('FINALIZADO', 'Finalizado'),
    ('CANCELADO', 'Cancelado'),
    ('SUSPENDIDO', 'Suspendido')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR ESTADOS DE ACTIVIDAD ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR TIPOS DE NOTIFICACIONES ############################################################ 
INSERT INTO tipo_notificacion (codigo, etiqueta)
VALUES 
    ('INSCRIPCION', 'Inscripción'),
    ('COMUNICACION', 'Comunicación'),
    ('PAGO', 'Pago'),
    ('CERTIFICADO', 'Certificado'),
    ('ACTIVIDAD', 'Actividad'),
    ('RECORDATORIO', 'Recordatorio')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR TIPOS DE NOTIFICACIONES ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR ESTADOS DE INSCRIPCION ############################################################ 
INSERT INTO estado_inscripcion (codigo, etiqueta)
VALUES 
    ('PENDIENTE', 'Pendiente'),
    ('CONFIRMADA', 'Confirmada'),
    ('CANCELADA', 'Cancelada'),
    ('COMPLETADA', 'Completada')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR ESTADOS DE INSCRIPCION ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR ESTADOS DE CERTIFICADO ############################################################ 
INSERT INTO estado_certificado (codigo, etiqueta)
VALUES 
    ('EMITIDO', 'Emitido'),
    ('PENDIENTE_EMISION', 'Pendiente de Emisión'),
    ('REVOCADO', 'Revocado'),
    ('SUSPENDIDO', 'Suspendido')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR ESTADOS DE CERTIFICADO ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR TIPOS DE VALIDACIONES ############################################################ 
INSERT INTO tipo_validador (codigo, etiqueta)
VALUES 
    ('QR', 'Validación por código QR'),
    ('HASH', 'Validación por hash criptográfico'),
    ('URL_PUBLICA', 'Validación por URL pública'),
    ('OCSP', 'Validación por OCSP'),
    ('ADMIN', 'Validación administrativa')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR TIPOS DE VALIDACIONES ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR ESTADOS DE PAGO ############################################################ 
INSERT INTO estado_pago (codigo , etiqueta)
VALUES
    ('PENDIENTE' , 'Pendiente'),
    ('COMPLETADO' , 'Completado'),
    ('ANULADO' , 'Anulado')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR ESTADOS DE PAGO ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR METODOS DE PAGO ############################################################ 
INSERT INTO metodo_pago(codigo , etiqueta)
VALUES 
    ('YAPE', 'Yape'),
    ('PAYPAL', 'Paypal'),
    ('EFECTIVO', 'Efectivo')
ON CONFLICT (codigo) DO NOTHING;
--- ############################################################ INSERTAR METODOS DE PAGO ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR ACTIVIDADES ############################################################ 
INSERT INTO actividad (
    titulo, descripcion, fecha_inicio, fecha_fin, hora_inicio, hora_fin,
    estado_actividad_id, id_usuario, tipo_actividad_id, lugar, 
    co_organizador, sponsor, banner_url, created_at, updated_at
)
SELECT 
    'Curso de Introducción a la Programación',
    'Curso básico para aprender los fundamentos de la programación con ejemplos prácticos.',
    CURRENT_DATE + INTERVAL '7 days',
    CURRENT_DATE + INTERVAL '30 days',
    '09:00:00'::TIME,
    '12:00:00'::TIME,
    ea.id_estado_actividad,
    u.id_usuario,
    ta.id_tipo_actividad,
    'Aula Virtual 101',
    'Co-Organizador Ejemplo',
    'Sponsor Ejemplo',
    'https://example.com/banner.jpg',
    NOW(),
    NOW()
FROM estado_actividad ea
CROSS JOIN usuario u
CROSS JOIN tipo_actividad ta
WHERE ea.codigo = 'BORRADOR'
  AND ta.nombre_actividad = 'CURSO'
  AND u.correo = 'organizador@sigea.unas.edu.pe'
  AND NOT EXISTS (
      SELECT 1 FROM actividad a
      WHERE a.titulo = 'Curso de Introducción a la Programación'
        AND a.id_usuario = u.id_usuario
  )
ON CONFLICT DO NOTHING;

INSERT INTO actividad (
    titulo, descripcion, fecha_inicio, fecha_fin, hora_inicio, hora_fin,
    estado_actividad_id, id_usuario, tipo_actividad_id, lugar,
    co_organizador, sponsor, banner_url, created_at, updated_at
)
SELECT
    -- Título variado
    CASE (gs % 8)
        WHEN 0 THEN 'Curso de Python desde Cero ' || gs
        WHEN 1 THEN 'Taller de Excel para Gestión ' || gs
        WHEN 2 THEN 'Conferencia de IA Aplicada ' || gs
        WHEN 3 THEN 'Seminario de Investigación ' || gs
        WHEN 4 THEN 'Webinar de Seguridad Informática ' || gs
        WHEN 5 THEN 'Charla de Emprendimiento ' || gs
        WHEN 6 THEN 'Congreso de Innovación ' || gs
        ELSE       'Diplomado en Analítica de Datos ' || gs
    END,
    -- Descripción variada
    CASE (gs % 8)
        WHEN 0 THEN 'Aprende fundamentos y práctica con ejercicios guiados.'
        WHEN 1 THEN 'Funciones, tablas dinámicas y reportes automatizados.'
        WHEN 2 THEN 'Tendencias, casos reales y herramientas actuales.'
        WHEN 3 THEN 'Metodología, redacción y publicación científica.'
        WHEN 4 THEN 'Buenas prácticas y herramientas para proteger sistemas.'
        WHEN 5 THEN 'Ideas, modelo de negocio y validación con usuarios.'
        WHEN 6 THEN 'Ponencias, paneles y networking académico.'
        ELSE       'Programa intensivo con evaluación y proyecto final.'
    END,
    -- Fechas variadas (duración distinta según tipo)
    CURRENT_DATE + ((gs * 2) || ' days')::INTERVAL,
    CURRENT_DATE + ((gs * 2) || ' days')::INTERVAL
        + (CASE WHEN (gs % 8) IN (7) THEN '45 days'::INTERVAL
                WHEN (gs % 8) IN (0) THEN '20 days'::INTERVAL
                WHEN (gs % 8) IN (6) THEN '3 days'::INTERVAL
                ELSE '2 days'::INTERVAL
           END),
    -- Horarios variados
    CASE (gs % 4)
        WHEN 0 THEN '08:00'::TIME
        WHEN 1 THEN '10:00'::TIME
        WHEN 2 THEN '14:00'::TIME
        ELSE       '18:30'::TIME
    END,
    CASE (gs % 4)
        WHEN 0 THEN '11:00'::TIME
        WHEN 1 THEN '12:30'::TIME
        WHEN 2 THEN '17:00'::TIME
        ELSE       '20:30'::TIME
    END,
    -- Estado variado
    (SELECT ea2.id_estado_actividad
     FROM estado_actividad ea2
     WHERE ea2.codigo = CASE (gs % 3)
        WHEN 0 THEN 'PUBLICADO'
        WHEN 1 THEN 'BORRADOR'
        ELSE       'EN_CURSO'
     END),
    -- Organizador (si existe el usuario organizador base)
    u.id_usuario,
    -- Tipo de actividad variado (debe existir en tipo_actividad)
    (SELECT ta2.id_tipo_actividad
     FROM tipo_actividad ta2
     WHERE ta2.nombre_actividad = CASE (gs % 8)
        WHEN 0 THEN 'CURSO'
        WHEN 1 THEN 'TALLER'
        WHEN 2 THEN 'CONFERENCIA'
        WHEN 3 THEN 'SEMINARIO'
        WHEN 4 THEN 'WEBINAR'
        WHEN 5 THEN 'CHARLA'
        WHEN 6 THEN 'CONGRESO'
        ELSE       'DIPLOMADO'
     END),
    -- Lugar variado
    CASE (gs % 5)
        WHEN 0 THEN 'Auditorio Central'
        WHEN 1 THEN 'Aula Virtual 101'
        WHEN 2 THEN 'Laboratorio de Cómputo 2'
        WHEN 3 THEN 'Sala de Conferencias'
        ELSE       'Campus UNAS - Pabellón A'
    END,
    -- co_organizador/sponsor/banner variados
    CASE (gs % 4)
        WHEN 0 THEN 'Facultad de Ingeniería'
        WHEN 1 THEN 'Oficina de Innovación'
        WHEN 2 THEN 'Dirección Académica'
        ELSE       'Centro de Investigación'
    END,
    CASE (gs % 4)
        WHEN 0 THEN 'UNAS'
        WHEN 1 THEN 'Zentrycorp'
        WHEN 2 THEN 'Comunidad Tech'
        ELSE       'Patrocinador Local'
    END,
    'https://picsum.photos/seed/sigea-' || gs || '/1200/400',
    NOW(), NOW()
FROM generate_series(1, 20) gs
JOIN usuario u ON u.correo = 'organizador@sigea.unas.edu.pe'
WHERE NOT EXISTS (
    SELECT 1 FROM actividad a
    WHERE a.titulo = CASE (gs % 8)
        WHEN 0 THEN 'Curso de Python desde Cero ' || gs
        WHEN 1 THEN 'Taller de Excel para Gestión ' || gs
        WHEN 2 THEN 'Conferencia de IA Aplicada ' || gs
        WHEN 3 THEN 'Seminario de Investigación ' || gs
        WHEN 4 THEN 'Webinar de Seguridad Informática ' || gs
        WHEN 5 THEN 'Charla de Emprendimiento ' || gs
        WHEN 6 THEN 'Congreso de Innovación ' || gs
        ELSE       'Diplomado en Analítica de Datos ' || gs
    END
);
--- ############################################################ INSERTAR ACTIVIDADES ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR SESIONES ############################################################ 
INSERT INTO sesion (
    titulo,
    descripcion,
    fecha_sesion,
    hora_inicio,
    hora_fin,
    modalidad,
    lugar_sesion,
    actividad_id,
    created_at,
    updated_at
)
SELECT
    s.titulo,
    s.descripcion,
    s.fecha_sesion,
    s.hora_inicio,
    s.hora_fin,
    s.modalidad,
    s.lugar_sesion,
    a.id_actividad,
    NOW(),
    NOW()
FROM actividad a
JOIN (
    -- ===== Curso de Introducción a la Programación =====
    SELECT
        'Curso de Introducción a la Programación'::TEXT AS titulo_actividad,
        'Sesión 1'::TEXT AS titulo,
        'Fundamentos de programación y variables'::TEXT AS descripcion,
        (CURRENT_DATE + INTERVAL '7 days')::DATE AS fecha_sesion,
        '09:00'::TIME AS hora_inicio,
        '12:00'::TIME AS hora_fin,
        'VIRTUAL'::VARCHAR AS modalidad,
        'Aula Virtual 101'::TEXT AS lugar_sesion
    UNION ALL
    SELECT
        'Curso de Introducción a la Programación',
        'Sesión 2',
        'Condicionales y estructuras de control',
        (CURRENT_DATE + INTERVAL '14 days')::DATE,
        '09:00'::TIME,
        '12:00'::TIME,
        'VIRTUAL',
        'Aula Virtual 101'
    UNION ALL
    SELECT
        'Curso de Introducción a la Programación',
        'Sesión 3',
        'Funciones y ejercicios prácticos',
        (CURRENT_DATE + INTERVAL '21 days')::DATE,
        '09:00'::TIME,
        '12:00'::TIME,
        'VIRTUAL',
        'Aula Virtual 101'

    -- ===== Curso de Python desde Cero 1 =====
    UNION ALL
    SELECT
        'Curso de Python desde Cero 1',
        'Sesión 1',
        'Instalación de Python y primeros scripts',
        (CURRENT_DATE + INTERVAL '2 days')::DATE,
        '08:00'::TIME,
        '11:00'::TIME,
        'PRESENCIAL',
        'Laboratorio de Cómputo 2'
    UNION ALL
    SELECT
        'Curso de Python desde Cero 1',
        'Sesión 2',
        'Tipos de datos y estructuras básicas',
        (CURRENT_DATE + INTERVAL '4 days')::DATE,
        '08:00'::TIME,
        '11:00'::TIME,
        'PRESENCIAL',
        'Laboratorio de Cómputo 2'
    UNION ALL
    SELECT
        'Curso de Python desde Cero 1',
        'Sesión 3',
        'Funciones y módulos',
        (CURRENT_DATE + INTERVAL '6 days')::DATE,
        '08:00'::TIME,
        '11:00'::TIME,
        'HIBRIDA',
        'Aula Virtual 202'
    UNION ALL
    SELECT
        'Curso de Python desde Cero 1',
        'Sesión 4',
        'Mini proyecto final',
        (CURRENT_DATE + INTERVAL '8 days')::DATE,
        '08:00'::TIME,
        '11:00'::TIME,
        'HIBRIDA',
        'Aula Virtual 202'

    -- ===== Taller de Excel para Gestión 1 =====
    UNION ALL
    SELECT
        'Taller de Excel para Gestión 1',
        'Sesión 1',
        'Funciones básicas y hojas de cálculo',
        (CURRENT_DATE + INTERVAL '3 days')::DATE,
        '15:00'::TIME,
        '17:00'::TIME,
        'PRESENCIAL',
        'Sala de Conferencias'
    UNION ALL
    SELECT
        'Taller de Excel para Gestión 1',
        'Sesión 2',
        'Tablas dinámicas y reportes',
        (CURRENT_DATE + INTERVAL '5 days')::DATE,
        '15:00'::TIME,
        '17:00'::TIME,
        'PRESENCIAL',
        'Sala de Conferencias'

    -- ===== Webinar =====
    UNION ALL
    SELECT
        'Webinar de Seguridad Informática 1',
        'Sesión única',
        'Buenas prácticas de seguridad',
        (CURRENT_DATE + INTERVAL '6 days')::DATE,
        '19:00'::TIME,
        '21:00'::TIME,
        'VIRTUAL',
        'Aula Virtual 101'

    -- ===== Conferencia =====
    UNION ALL
    SELECT
        'Conferencia de IA Aplicada 1',
        'Ponencia',
        'Aplicaciones de IA en educación',
        (CURRENT_DATE + INTERVAL '10 days')::DATE,
        '18:30'::TIME,
        '20:30'::TIME,
        'PRESENCIAL',
        'Auditorio Central'
) s
ON a.titulo = s.titulo_actividad
WHERE NOT EXISTS (
    SELECT 1
    FROM sesion se
    WHERE se.actividad_id = a.id_actividad
      AND se.fecha_sesion = s.fecha_sesion
      AND se.hora_inicio = s.hora_inicio
      AND se.hora_fin = s.hora_fin
);
--- ############################################################ INSERTAR SESIONES ############################################################ 
--- ================
--- ================
--- ############################################################ INSERTAR ASISTENCIAS ############################################################ 
INSERT INTO asistencia (
    presente,
    registrado_en,
    inscripcion_id,
    sesion_id
)
SELECT
    -- Presente solo si la inscripción está CONFIRMADA o COMPLETADA
    CASE
        WHEN ei.codigo IN ('CONFIRMADA', 'COMPLETADA') THEN TRUE
        ELSE FALSE
    END AS presente,

    NOW() AS registrado_en,

    i.id_inscripcion,      -- FK -> inscripción ya insertada
    s.id_sesion            -- FK -> sesión ya insertada

FROM inscripcion i
JOIN estado_inscripcion ei
    ON ei.id_estado_inscripcion = i.estado_inscripcion_id
JOIN sesion s
    ON s.actividad_id = i.actividad_id
WHERE NOT EXISTS (
    SELECT 1
    FROM asistencia a
    WHERE a.inscripcion_id = i.id_inscripcion
        AND a.sesion_id = s.id_sesion
);
--- ############################################################ INSERTAR ASISTENCIAS ############################################################ 