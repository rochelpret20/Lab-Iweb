package com.lab.dao;

import com.lab.beans.Departamento;
import com.lab.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DAO (Data Access Object) para la tabla "departamento".
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Renombra esta clase y ajusta el SQL según tu tabla de categorías.
 *   Ejemplos:
 *     - DaoGenero.java    → SELECT * FROM genero
 *     - DaoProveedor.java → SELECT * FROM proveedor
 *     - DaoArea.java      → SELECT * FROM area
 *
 *   Cambia también el bean que importas (Departamento → TuCategoria).
 * ─────────────────────────────────────────────────────────────
 *
 * Responsabilidad: proporcionar la lista de departamentos disponibles
 * para poblar el <select> del formulario de empleados.
 */
public class DaoDepartamento {

    /**
     * Retorna todos los departamentos ordenados por nombre.
     * Se usa para poblar el combo-box en el formulario de empleados.
     *
     * @return Lista de objetos Departamento (puede estar vacía, nunca null)
     */
    public ArrayList<Departamento> listarDepartamentos() {

        // Lista que devolveremos; se mantiene vacía si ocurre un error
        ArrayList<Departamento> lista = new ArrayList<>();

        // SQL: selecciona id y nombre de todos los departamentos
        String sql = """
                SELECT
                    id_departamento,
                    nombre
                FROM departamento
                ORDER BY nombre
                """;

        /*
         * try-with-resources: Java cierra automáticamente Connection y
         * PreparedStatement al terminar el bloque, incluso si hay excepciones.
         */
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Iteramos cada fila devuelta por la consulta
            while (rs.next()) {

                // Creamos un objeto Departamento por cada fila
                Departamento dep = new Departamento();

                dep.setIdDepartamento(rs.getInt("id_departamento"));
                dep.setNombre(rs.getString("nombre"));

                lista.add(dep); // Lo agregamos a la lista
            }

        } catch (Exception e) {
            // Imprime el error en consola del servidor (Tomcat)
            // En un proyecto real usarías un Logger (log4j / slf4j)
            e.printStackTrace();
        }

        return lista;
    }
}
