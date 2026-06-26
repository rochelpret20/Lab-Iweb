package com.lab.dao;

import com.lab.beans.Empleado;
import com.lab.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DAO (Data Access Object) para la tabla "empleado".
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   1. Renombra la clase:  DaoLibro, DaoVehiculo, DaoProducto, etc.
 *   2. Ajusta el import del bean correspondiente.
 *   3. Reemplaza el SQL en cada método con las columnas de tu tabla.
 *   4. En listarEmpleados(), cambia el JOIN a tu tabla de categoría.
 *   5. En crearEmpleado() y actualizarEmpleado(), ajusta los parámetros
 *      del PreparedStatement con los campos de tu entidad.
 * ─────────────────────────────────────────────────────────────
 *
 * Implementa las 4 operaciones CRUD:
 *   C → crearEmpleado()
 *   R → listarEmpleados() y obtenerEmpleado()
 *   U → actualizarEmpleado()
 *   D → eliminarEmpleado()
 */
public class DaoEmpleado {

    // ════════════════════════════════════════════════════════
    // READ - Listar todos
    // ════════════════════════════════════════════════════════

    /**
     * Retorna todos los empleados con el nombre de su departamento.
     *
     * Usa un INNER JOIN para traer el nombre del departamento en lugar
     * del id, de modo que la vista (JSP) lo muestre directamente.
     *
     * La columna "proyectos_asignados" descuenta los proyectos ya
     * asignados de la tabla "asignacion_item" (equivalente al stock
     * disponible del ejemplo original).
     *
     * @return Lista de empleados (vacía si no hay datos o hay error)
     */
    public ArrayList<Empleado> listarEmpleados() {

        ArrayList<Empleado> lista = new ArrayList<>();

        /*
         * Subquery en el SELECT: calcula los proyectos ya asignados
         * a cada empleado y los resta de su total de proyectos.
         * IFNULL(..., 0) evita nulos cuando no hay asignaciones.
         */
        String sql = """
                SELECT
                    e.id_empleado,
                    e.id_departamento,
                    e.nombres,
                    e.apellidos,
                    e.cargo,
                    e.salario,
                    (
                        e.proyectos -
                        IFNULL(
                            (
                                SELECT SUM(ai.meses)
                                FROM asignacion_item ai
                                WHERE ai.id_empleado = e.id_empleado
                            ),
                            0
                        )
                    ) proyectos_disponibles,
                    d.nombre departamento
                FROM empleado e
                INNER JOIN departamento d
                    ON e.id_departamento = d.id_departamento
                ORDER BY e.id_empleado
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Empleado emp = new Empleado();

                emp.setIdEmpleado(rs.getInt("id_empleado"));
                emp.setIdDepartamento(rs.getInt("id_departamento"));
                emp.setNombres(rs.getString("nombres"));
                emp.setApellidos(rs.getString("apellidos"));
                emp.setCargo(rs.getString("cargo"));
                emp.setSalario(rs.getBigDecimal("salario"));
                emp.setProyectos(rs.getInt("proyectos_disponibles"));
                emp.setDepartamentoNombre(rs.getString("departamento"));

                lista.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ════════════════════════════════════════════════════════
    // READ - Obtener uno por ID
    // ════════════════════════════════════════════════════════

    /**
     * Busca un empleado por su clave primaria.
     * Se usa en el flujo de edición para pre-cargar el formulario.
     *
     * @param idEmpleado clave primaria del empleado a buscar
     * @return Empleado encontrado, o null si no existe
     */
    public Empleado obtenerEmpleado(int idEmpleado) {

        Empleado empleado = null; // null indica "no encontrado"

        String sql = """
                SELECT *
                FROM empleado
                WHERE id_empleado = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Parámetro posicional: reemplaza el primer "?" en el SQL
            ps.setInt(1, idEmpleado);

            ResultSet rs = ps.executeQuery();

            // if(rs.next()): solo esperamos 0 ó 1 resultado (PK única)
            if (rs.next()) {

                empleado = new Empleado();

                empleado.setIdEmpleado(rs.getInt("id_empleado"));
                empleado.setIdDepartamento(rs.getInt("id_departamento"));
                empleado.setNombres(rs.getString("nombres"));
                empleado.setApellidos(rs.getString("apellidos"));
                empleado.setCargo(rs.getString("cargo"));
                empleado.setSalario(rs.getBigDecimal("salario"));
                empleado.setProyectos(rs.getInt("proyectos"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return empleado;
    }

    // ════════════════════════════════════════════════════════
    // CREATE
    // ════════════════════════════════════════════════════════

    /**
     * Inserta un nuevo empleado en la base de datos.
     * El id_empleado es AUTO_INCREMENT, así que no se envía.
     *
     * @param empleado objeto con los datos a insertar
     */
    public void crearEmpleado(Empleado empleado) {

        String sql = """
                INSERT INTO empleado
                (
                    id_departamento,
                    nombres,
                    apellidos,
                    cargo,
                    salario,
                    proyectos
                )
                VALUES
                (
                    ?, ?, ?, ?, ?, ?
                )
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignamos cada "?" en el mismo orden que el INSERT
            ps.setInt(1, empleado.getIdDepartamento());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getCargo());
            ps.setBigDecimal(5, empleado.getSalario());
            ps.setInt(6, empleado.getProyectos());

            ps.executeUpdate(); // Ejecuta el INSERT

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ════════════════════════════════════════════════════════
    // UPDATE
    // ════════════════════════════════════════════════════════

    /**
     * Actualiza todos los campos de un empleado existente.
     * Identifica el registro por su id (cláusula WHERE al final).
     *
     * @param empleado objeto con los datos actualizados (debe tener idEmpleado)
     */
    public void actualizarEmpleado(Empleado empleado) {

        String sql = """
                UPDATE empleado
                SET
                    id_departamento = ?,
                    nombres         = ?,
                    apellidos       = ?,
                    cargo           = ?,
                    salario         = ?,
                    proyectos       = ?
                WHERE id_empleado = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empleado.getIdDepartamento());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getCargo());
            ps.setBigDecimal(5, empleado.getSalario());
            ps.setInt(6, empleado.getProyectos());
            ps.setInt(7, empleado.getIdEmpleado()); // Condición WHERE

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ════════════════════════════════════════════════════════
    // DELETE
    // ════════════════════════════════════════════════════════

    /**
     * Elimina físicamente un empleado de la base de datos.
     *
     * ⚠ Si hay registros en "asignacion_item" que referencian este empleado,
     *   MySQL lanzará un error de FK. Considera un borrado lógico (campo activo=0)
     *   en un proyecto real.
     *
     * @param idEmpleado clave primaria del empleado a eliminar
     */
    public void eliminarEmpleado(int idEmpleado) {

        String sql = """
                DELETE FROM empleado
                WHERE id_empleado = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
