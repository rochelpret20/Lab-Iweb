package com.lab.dao;

import com.lab.beans.Asignacion;
import com.lab.config.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DAO para la tabla "asignacion_item".
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Este DAO es el equivalente al DaoCarrito del ejemplo original.
 *   Maneja la tabla de relación entre usuario y la entidad principal.
 *   Ejemplos:
 *     - DaoPrestamo  → tabla prestamo_item  (biblioteca)
 *     - DaoPedido    → tabla pedido_item    (restaurante)
 *     - DaoReserva   → tabla reserva_item   (hotel)
 *
 *   Ajusta los nombres de columnas en el SQL y los setters del bean.
 * ─────────────────────────────────────────────────────────────
 *
 * Lógica principal:
 *   - Si el usuario ya asignó al empleado → incrementa los meses (+1)
 *   - Si es la primera vez             → inserta un nuevo registro
 */
public class DaoAsignacion {

    // ════════════════════════════════════════════════════════
    // Agregar / incrementar asignación
    // ════════════════════════════════════════════════════════

    /**
     * Agrega un empleado a la lista de asignaciones del usuario.
     *
     * Primero busca si ya existe un registro (mismo usuario + mismo empleado).
     *   → Si existe: incrementa meses en 1 (UPDATE)
     *   → Si no existe: crea un registro nuevo con meses = 1 (INSERT)
     *
     * @param idUsuario  usuario que realiza la asignación
     * @param idEmpleado empleado que se agrega a la asignación
     */
    public void agregarEmpleado(int idUsuario, int idEmpleado) {

        // ── 1. Buscar si ya existe la combinación usuario+empleado ──
        String sqlBuscar = """
                SELECT *
                FROM asignacion_item
                WHERE id_usuario  = ?
                  AND id_empleado = ?
                """;

        // ── 2a. Si existe: sumamos 1 mes ──────────────────────────
        String sqlActualizar = """
                UPDATE asignacion_item
                SET meses = meses + 1
                WHERE id_usuario  = ?
                  AND id_empleado = ?
                """;

        // ── 2b. Si no existe: insertamos con meses = 1 ───────────
        String sqlInsertar = """
                INSERT INTO asignacion_item
                (
                    id_usuario,
                    id_empleado,
                    meses
                )
                VALUES
                (
                    ?, ?, 1
                )
                """;

        try (Connection con = ConexionDB.getConnection()) {

            // Abrimos la conexión una sola vez para las 2 operaciones
            PreparedStatement psBuscar =
                    con.prepareStatement(sqlBuscar);

            psBuscar.setInt(1, idUsuario);
            psBuscar.setInt(2, idEmpleado);

            ResultSet rs = psBuscar.executeQuery();

            if (rs.next()) {

                // Ya existe → actualizar
                PreparedStatement psUpdate =
                        con.prepareStatement(sqlActualizar);

                psUpdate.setInt(1, idUsuario);
                psUpdate.setInt(2, idEmpleado);
                psUpdate.executeUpdate();

            } else {

                // No existe → insertar
                PreparedStatement psInsert =
                        con.prepareStatement(sqlInsertar);

                psInsert.setInt(1, idUsuario);
                psInsert.setInt(2, idEmpleado);
                psInsert.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ════════════════════════════════════════════════════════
    // Listar asignaciones de un usuario
    // ════════════════════════════════════════════════════════

    /**
     * Retorna todas las asignaciones del usuario indicado.
     *
     * Hace JOIN con "empleado" y "usuario" para traer los nombres
     * en lugar de los IDs, facilitando el renderizado en la vista JSP.
     *
     * El costoTotal se calcula directamente en SQL (salario × meses)
     * para evitar lógica de negocio en la capa de presentación.
     *
     * @param idUsuario usuario cuyas asignaciones se quieren listar
     * @return lista de objetos Asignacion con toda la información
     */
    public ArrayList<Asignacion> listarAsignaciones(int idUsuario) {

        ArrayList<Asignacion> lista = new ArrayList<>();

        String sql = """
                SELECT
                    ai.id_asignacion,
                    ai.id_usuario,
                    e.id_empleado,
                    CONCAT(e.nombres, ' ', e.apellidos) AS nombre_empleado,
                    e.salario,
                    ai.meses,
                    (e.salario * ai.meses)              AS costo_total,
                    CONCAT(u.nombres, ' ', u.apellidos) AS usuario
                FROM asignacion_item ai
                INNER JOIN empleado e
                    ON ai.id_empleado = e.id_empleado
                INNER JOIN usuario u
                    ON ai.id_usuario  = u.id_usuario
                WHERE ai.id_usuario = ?
                ORDER BY ai.id_asignacion
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Asignacion a = new Asignacion();

                a.setIdAsignacion(rs.getInt("id_asignacion"));
                a.setIdUsuario(rs.getInt("id_usuario"));
                a.setIdEmpleado(rs.getInt("id_empleado"));
                a.setNombreEmpleado(rs.getString("nombre_empleado"));
                a.setSalarioBase(rs.getBigDecimal("salario"));
                a.setMeses(rs.getInt("meses"));
                a.setCostoTotal(rs.getBigDecimal("costo_total"));
                a.setNombreUsuario(rs.getString("usuario"));

                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ════════════════════════════════════════════════════════
    // Obtener costo total de todas las asignaciones
    // ════════════════════════════════════════════════════════

    /**
     * Calcula el costo total de todas las asignaciones del usuario.
     *
     * Usa SUM() en SQL para sumar (salario × meses) de todos los registros.
     * IFNULL maneja el caso en que la tabla esté vacía (SUM de nada = NULL).
     *
     * @param idUsuario usuario del que se quiere el total
     * @return BigDecimal con el costo total (0.00 si no hay asignaciones)
     */
    public BigDecimal obtenerCostoTotal(int idUsuario) {

        BigDecimal total = BigDecimal.ZERO; // Valor por defecto

        String sql = """
                SELECT
                    SUM(e.salario * ai.meses) AS total
                FROM asignacion_item ai
                INNER JOIN empleado e
                    ON ai.id_empleado = e.id_empleado
                WHERE ai.id_usuario = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                total = rs.getBigDecimal("total");

                // SUM() retorna NULL si no hay filas; protegemos con ZERO
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}
