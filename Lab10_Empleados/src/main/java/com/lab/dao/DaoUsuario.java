package com.lab.dao;

import com.lab.beans.Usuario;
import com.lab.config.ConexionDB;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DAO para autenticar usuarios del sistema.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Esta clase raramente necesita cambios entre proyectos.
 *   Solo ajusta los nombres de columnas en el SQL si tu tabla
 *   "usuario" usa nombres distintos (ej: "user", "email", "pwd").
 * ─────────────────────────────────────────────────────────────
 *
 * Seguridad:
 *   Las contraseñas se almacenan como hash SHA-256 en la BD.
 *   Nunca se guarda la contraseña en texto plano.
 *   El método generarSHA256() convierte el password ingresado
 *   al mismo hash para compararlo con el almacenado.
 */
public class DaoUsuario {

    // ════════════════════════════════════════════════════════
    // Validar usuario al hacer login
    // ════════════════════════════════════════════════════════

    /**
     * Verifica si el email y contraseña corresponden a un usuario activo.
     *
     * Flujo:
     *   1. Busca en BD un usuario con ese email y estado ACTIVO.
     *   2. Si lo encuentra, genera el hash SHA-256 del password ingresado.
     *   3. Compara el hash con el almacenado en BD.
     *   4. Si coinciden, construye y retorna el objeto Usuario.
     *   5. Si no coinciden, retorna null (login fallido).
     *
     * @param email    correo ingresado en el formulario
     * @param password contraseña en texto plano ingresada en el formulario
     * @return Usuario autenticado, o null si las credenciales son inválidas
     */
    public Usuario validarUsuario(String email, String password) {

        Usuario usuario = null; // null = credenciales inválidas

        // Solo buscamos usuarios ACTIVOS (no suspendidos/eliminados)
        String sql = """
                SELECT *
                FROM usuario
                WHERE email = ?
                  AND estado = 'ACTIVO'
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // Hash almacenado en la base de datos
                String hashBD = rs.getString("password_hash");

                // Hash del password que el usuario acaba de escribir
                String hashIngresado = generarSHA256(password);

                // Comparación segura de hashes
                if (hashBD.equals(hashIngresado)) {

                    // Credenciales válidas → construimos el objeto de sesión
                    usuario = new Usuario();

                    usuario.setIdUsuario(rs.getInt("id_usuario"));

                    // Concatenamos nombres + apellidos para mostrarlo en navbar
                    usuario.setNombre(
                            rs.getString("nombres") + " " +
                            rs.getString("apellidos"));

                    usuario.setCorreo(rs.getString("email"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // ════════════════════════════════════════════════════════
    // Utilidad: generar hash SHA-256
    // ════════════════════════════════════════════════════════

    /**
     * Convierte un texto en su representación hexadecimal SHA-256.
     *
     * SHA-256 es un hash de 256 bits → 32 bytes → 64 caracteres hex.
     * "%02x" formatea cada byte como 2 dígitos hexadecimales en minúscula.
     *
     * Ejemplo:
     *   "admin123" → "240be518..."  (64 chars)
     *
     * @param texto texto a hashear (la contraseña en texto plano)
     * @return cadena hexadecimal de 64 caracteres
     */
    private String generarSHA256(String texto) {

        try {
            // Obtenemos la implementación de SHA-256 del JDK
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convertimos el texto a bytes y calculamos el hash
            byte[] hash = md.digest(texto.getBytes());

            // Convertimos el array de bytes a cadena hexadecimal
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                // %02x: mínimo 2 dígitos hex en minúscula
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar SHA-256", e);
        }
    }
}
