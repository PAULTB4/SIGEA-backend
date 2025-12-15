-- Insertar tipos de validador para certificados
INSERT INTO tipo_validador (codigo, etiqueta)
VALUES 
    ('QR', 'Validación por código QR'),
    ('HASH', 'Validación por hash criptográfico'),
    ('URL_PUBLICA', 'Validación por URL pública'),
    ('OCSP', 'Validación por OCSP'),
    ('ADMIN', 'Validación administrativa')
ON CONFLICT (codigo) DO NOTHING;
