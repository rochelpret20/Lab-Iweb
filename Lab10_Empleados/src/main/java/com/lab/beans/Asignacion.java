package com.lab.beans;

import java.math.BigDecimal;

/**
 * DTO para la entidad Asignacion.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Este bean es el equivalente al "Carrito" del ejemplo original.
 *   Representa la tabla intermedia / de relación.
 *   Ejemplos de adaptación:
 *     - Tema Biblioteca  → "Prestamo" (usuario + libro + fecha)
 *     - Tema Restaurante → "Pedido"   (usuario + plato + cantidad)
 *     - Tema Hospital    → "Cita"     (usuario + médico + fecha)
 *
 *   Renombra la clase y ajusta sus atributos según tu contexto.
 * ─────────────────────────────────────────────────────────────
 *
 * Registra la asignación de un empleado a un proyecto por un usuario.
 * Guarda la información desnormalizada (nombres) para mostrarla
 * directamente en la vista sin necesidad de nuevos JOINs.
 */
public class Asignacion {

    /** Clave primaria del registro de asignación */
    private int idAsignacion;

    /** FK al usuario que realizó la asignación */
    private int idUsuario;

    /** FK al empleado asignado */
    private int idEmpleado;

    /** Nombre del empleado (cargado desde JOIN) */
    private String nombreEmpleado;

    /** Nombre del usuario que hizo la asignación (cargado desde JOIN) */
    private String nombreUsuario;

    /** Salario del empleado en el momento de la asignación */
    private BigDecimal salarioBase;

    /** Cantidad de meses del proyecto */
    private int meses;

    /** Costo total = salarioBase × meses */
    private BigDecimal costoTotal;

    // ── Getters y Setters ────────────────────────────────────

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
}
