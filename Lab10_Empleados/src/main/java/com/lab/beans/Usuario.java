package com.lab.beans;

/**
 * DTO para la entidad Usuario (sesión / autenticación).
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Esta clase casi no necesita cambios; solo asegúrate de que
 *   los campos coincidan con tu tabla "usuario" en la BD.
 *   Si tu tabla usa "username" en vez de "correo", ajusta aquí.
 * ─────────────────────────────────────────────────────────────
 *
 * Se guarda en la sesión HTTP cuando el usuario inicia sesión.
 * Permite saber quién está conectado en cada request.
 */
public class Usuario {

    /** Clave primaria del usuario */
    private int idUsuario;

    /** Nombre completo (nombres + apellidos concatenados) */
    private String nombre;

    /** Correo electrónico usado para el login */
    private String correo;

    // ── Getters y Setters ────────────────────────────────────

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
