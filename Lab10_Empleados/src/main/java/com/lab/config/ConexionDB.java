package com.lab.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para obtener conexiones a la base de datos MySQL.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Solo cambia las constantes URL, USER y PASSWORD para apuntar
 *   a tu base de datos.  El resto del código no necesita cambios.
 *
 *   Para AWS RDS, reemplaza "localhost:3306" con el endpoint de tu
 *   instancia RDS, por ejemplo:
 *   "jdbc:mysql://mi-db.c9x3xr.us-east-1.rds.amazonaws.com:3306/lab9_empleados..."
 * ─────────────────────────────────────────────────────────────
 *
 * Patrón utilizado: método estático factory (getConnection).
 * Cada DAO abre y cierra su propia conexión usando try-with-resources.
 */
public class ConexionDB {

    // ── Constantes de conexión ───────────────────────────────

    /**
     * URL de conexión JDBC.
     * Formato: jdbc:mysql://<host>:<puerto>/<basededatos>?parámetros
     * serverTimezone es obligatorio para MySQL 8+.
     */
    private static final String URL =
            "jdbc:mysql://localhost:3306/lab9_empleados?serverTimezone=America/Lima";

    /** Usuario de la base de datos */
    private static final String USER = "root";

    /**
     * Contraseña del usuario de la base de datos.
     * ⚠ En producción, usa variables de entorno o un archivo de configuración
     *   externo; nunca dejes la contraseña en el código fuente.
     */
    private static final String PASSWORD = "TuPasswordAqui2025";

    // ── Método principal ─────────────────────────────────────

    /**
     * Crea y devuelve una conexión activa a la base de datos.
     *
     * Carga el driver JDBC de MySQL dinámicamente (Class.forName)
     * y delega la creación de la conexión a DriverManager.
     *
     * @return Connection lista para usarse
     * @throws SQLException si no se puede conectar a la BD
     */
    public static Connection getConnection() throws SQLException {

        try {
            // Carga el driver JDBC de MySQL en memoria.
            // Necesario para que DriverManager lo reconozca.
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            // Si el driver no está en el classpath (falta la dependencia
            // en pom.xml), lanzamos RuntimeException para detener la app.
            throw new RuntimeException(
                    "Driver MySQL no encontrado. Verifica pom.xml", e);
        }

        // Retorna la conexión al llamador.
        // El llamador debe cerrarla con try-with-resources.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
