-- ============================================================
-- Script SQL: lab9_empleados
-- ============================================================
-- Ejecutar en MySQL Workbench o en la línea de comandos:
--   mysql -u root -p < script_bd.sql
-- ============================================================

-- 1. Crear la base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS lab9_empleados
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE lab9_empleados;

-- ============================================================
-- 2. Tabla: usuario
--    Almacena los usuarios que pueden iniciar sesión.
--    password_hash: contraseña cifrada con SHA-256 (hex, 64 chars)
-- ============================================================
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario    INT AUTO_INCREMENT PRIMARY KEY,
    nombres       VARCHAR(100) NOT NULL,
    apellidos     VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash CHAR(64)     NOT NULL,       -- SHA-256 en hex
    estado        ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 3. Tabla: departamento
--    Agrupa a los empleados por área (equivalente a "categoria")
-- ============================================================
CREATE TABLE IF NOT EXISTS departamento (
    id_departamento INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL
);

-- ============================================================
-- 4. Tabla: empleado
--    Entidad principal del sistema.
-- ============================================================
CREATE TABLE IF NOT EXISTS empleado (
    id_empleado     INT AUTO_INCREMENT PRIMARY KEY,
    id_departamento INT           NOT NULL,
    nombres         VARCHAR(100)  NOT NULL,
    apellidos       VARCHAR(100)  NOT NULL,
    cargo           VARCHAR(100)  NOT NULL,
    salario         DECIMAL(10,2) NOT NULL,
    proyectos       INT           NOT NULL DEFAULT 0,  -- total de proyectos posibles
    FOREIGN KEY (id_departamento) REFERENCES departamento(id_departamento)
);

-- ============================================================
-- 5. Tabla: asignacion_item
--    Relación entre usuario y empleado (equivalente a carrito_item)
--    Un usuario puede "asignar" N empleados a sus proyectos.
--    Si repite el mismo empleado, se acumulan los meses.
-- ============================================================
CREATE TABLE IF NOT EXISTS asignacion_item (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario    INT NOT NULL,
    id_empleado   INT NOT NULL,
    meses         INT NOT NULL DEFAULT 1,   -- meses de asignación acumulados
    FOREIGN KEY (id_usuario)  REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

-- ============================================================
-- 6. Datos de prueba: Usuarios
--    password_hash = SHA-256("admin123")
--    Para obtener el hash: SELECT SHA2('admin123', 256);
-- ============================================================
INSERT INTO usuario (nombres, apellidos, email, password_hash, estado)
VALUES
    ('Admin',    'Sistema',   'admin@empresa.com',   SHA2('admin123', 256),  'ACTIVO'),
    ('María',    'González',  'maria@empresa.com',   SHA2('maria2025', 256), 'ACTIVO'),
    ('Carlos',   'Ramírez',   'carlos@empresa.com',  SHA2('carlos2025',256), 'ACTIVO');

-- ============================================================
-- 7. Datos de prueba: Departamentos
-- ============================================================
INSERT INTO departamento (nombre) VALUES
    ('Tecnología'),
    ('Recursos Humanos'),
    ('Finanzas'),
    ('Operaciones'),
    ('Marketing');

-- ============================================================
-- 8. Datos de prueba: Empleados
-- ============================================================
INSERT INTO empleado (id_departamento, nombres, apellidos, cargo, salario, proyectos)
VALUES
    (1, 'Juan',    'Pérez',    'Desarrollador Senior',  4500.00, 5),
    (1, 'Ana',     'Torres',   'Analista QA',           3200.00, 3),
    (2, 'Luis',    'Mendoza',  'Jefe de RRHH',          5000.00, 4),
    (3, 'Sofía',   'Castro',   'Contadora',             3800.00, 2),
    (1, 'Pedro',   'Quispe',   'DevOps Engineer',       4200.00, 6),
    (4, 'Laura',   'Vega',     'Supervisora',           3500.00, 3),
    (5, 'Miguel',  'Flores',   'Diseñador Gráfico',     2900.00, 4),
    (1, 'Carla',   'Díaz',     'Desarrolladora Junior', 2800.00, 5),
    (2, 'Roberto', 'Salinas',  'Reclutador',            3100.00, 2),
    (3, 'Elena',   'Vargas',   'Analista Financiero',   4100.00, 3);

-- ============================================================
-- Verificación: consultas de comprobación
-- ============================================================
SELECT 'Usuarios creados:' AS info, COUNT(*) AS total FROM usuario;
SELECT 'Departamentos:'    AS info, COUNT(*) AS total FROM departamento;
SELECT 'Empleados:'        AS info, COUNT(*) AS total FROM empleado;
