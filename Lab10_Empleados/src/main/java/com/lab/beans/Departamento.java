package com.lab.beans;

/**
 * DTO (Data Transfer Object) para la entidad Departamento.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Renombra esta clase y sus atributos según tu entidad.
 *   Ejemplo: si el tema es "Proveedores", renombra a Proveedor.java
 *   y cambia los campos (ej: idProveedor, ruc, razonSocial, etc.)
 * ─────────────────────────────────────────────────────────────
 *
 * Representa un departamento dentro de la empresa.
 * Se usa como "categoría" o "agrupador" de los empleados.
 */
public class Departamento {

    /** Clave primaria del departamento en la BD */
    private int idDepartamento;

    /** Nombre del departamento (ej: "Recursos Humanos") */
    private String nombre;

    // ── Getters y Setters ────────────────────────────────────

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
