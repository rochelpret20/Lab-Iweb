--. CREAR BASE DE DATOS
CREATE DATABASE empresa;

USE empresa;
--CREAR TABLA
CREATE TABLE empleados(

    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    departamento VARCHAR(100),
    salario DOUBLE

);
-- INSERTAR DATOS
INSERT INTO empleados(nombre,departamento,salario)
VALUES

('Ana','Sistemas',3500),
('Luis','Contabilidad',6000),
('Carlos','Sistemas',8000),
('Maria','Marketing',4500),
('Pedro','Sistemas',9000);

--OBJETIVO
--Mostrar empleados que ganan MÁS
--que el promedio salarial de todos
--los empleados
