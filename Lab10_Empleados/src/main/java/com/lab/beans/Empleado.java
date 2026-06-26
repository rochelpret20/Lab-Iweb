package com.lab.beans;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) para la entidad Empleado.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Renombra esta clase a tu entidad principal.
 *   Ejemplo: Libro.java, Vehiculo.java, Proveedor.java, etc.
 *   Ajusta los atributos según las columnas de tu tabla en BD.
 * ─────────────────────────────────────────────────────────────
 *
 * Contiene todos los datos de un empleado:
 *   - Su clave primaria (idEmpleado)
 *   - Su relación con Departamento (idDepartamento + departamentoNombre)
 *   - Datos personales y laborales
 */
public class Empleado {

    /** Clave primaria del empleado */
    private int idEmpleado;

    /** FK que relaciona al empleado con su departamento */
    private int idDepartamento;

    /**
     * Nombre del departamento al que pertenece.
     * Se carga mediante JOIN en el DAO (no existe en la tabla empleado,
     * pero lo necesitamos para mostrar en la vista).
     */
    private String departamentoNombre;

    /** Nombres del empleado */
    private String nombres;

    /** Apellidos del empleado */
    private String apellidos;

    /** Cargo o puesto que ocupa */
    private String cargo;

    /** Salario mensual del empleado */
    private BigDecimal salario;

    /** Número de proyectos asignados (stock equivalente) */
    private int proyectos;

    // ── Getters y Setters ────────────────────────────────────

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getDepartamentoNombre() {
        return departamentoNombre;
    }

    public void setDepartamentoNombre(String departamentoNombre) {
        this.departamentoNombre = departamentoNombre;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public int getProyectos() {
        return proyectos;
    }

    public void setProyectos(int proyectos) {
        this.proyectos = proyectos;
    }
}
