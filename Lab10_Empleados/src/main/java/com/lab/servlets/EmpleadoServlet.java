package com.lab.servlets;

import com.lab.beans.Empleado;
import com.lab.dao.DaoDepartamento;
import com.lab.dao.DaoEmpleado;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Servlet que gestiona el CRUD completo de Empleados.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   1. Cambia @WebServlet("/empleados") a tu entidad: "/productos", etc.
 *   2. Reemplaza DaoEmpleado y DaoDepartamento por tus DAOs.
 *   3. Ajusta los request.getParameter() con los nombres de los campos
 *      de tu formulario JSP.
 *   4. Cambia las rutas de los JSPs (/empleados.jsp, /empleadoForm.jsp).
 * ─────────────────────────────────────────────────────────────
 *
 * Rutas manejadas:
 *   GET  /empleados              → lista todos los empleados
 *   GET  /empleados?action=nuevo → muestra formulario vacío
 *   GET  /empleados?action=editar&id=X → muestra formulario con datos
 *   GET  /empleados?action=eliminar&id=X → elimina y redirige
 *   POST /empleados              → crea o actualiza según el hidden "idEmpleado"
 *
 * Patrón de seguridad:
 *   Todos los métodos verifican que exista una sesión activa.
 *   Si no hay sesión, redirigen al login (evita acceso sin autenticación).
 */
@WebServlet("/empleados")
public class EmpleadoServlet extends HttpServlet {

    // DAOs instanciados una sola vez (el servlet vive todo el ciclo del app)
    private final DaoEmpleado     daoEmpleado     = new DaoEmpleado();
    private final DaoDepartamento daoDepartamento = new DaoDepartamento();

    // ════════════════════════════════════════════════════════
    // GET: listado, nuevo, editar, eliminar
    // ════════════════════════════════════════════════════════

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // ── Verificar sesión ─────────────────────────────────
        // getSession(false): no crea sesión nueva; retorna null si no existe
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {

            // Sin sesión → redirige al login (protección de ruta)
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return; // importante: detiene la ejecución del método
        }

        // ── Leer parámetro de acción ──────────────────────────
        // action determina qué operación ejecutar
        String action = request.getParameter("action");

        if (action == null) {

            // Sin action → mostrar listado principal
            mostrarListado(request, response);
            return;
        }

        // Delegamos según el valor de "action"
        switch (action) {

            case "nuevo":
                // Muestra el formulario vacío para registrar un empleado
                mostrarFormularioNuevo(request, response);
                break;

            case "editar":
                // Muestra el formulario pre-cargado con datos del empleado
                mostrarFormularioEditar(request, response);
                break;

            case "eliminar":
                // Elimina el empleado y redirige al listado
                eliminarEmpleado(request, response);
                break;

            default:
                // Acción desconocida → volvemos al listado de forma segura
                mostrarListado(request, response);
        }
    }

    // ── Acción: listar ───────────────────────────────────────

    /**
     * Carga la lista de empleados y hace forward a empleados.jsp.
     */
    private void mostrarListado(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Ponemos la lista en el request para que la JSP la lea con JSTL
        request.setAttribute(
                "listaEmpleados",
                daoEmpleado.listarEmpleados());

        request.getRequestDispatcher("/empleados.jsp")
               .forward(request, response);
    }

    // ── Acción: formulario nuevo ─────────────────────────────

    /**
     * Prepara el formulario de creación (sin datos pre-cargados).
     * Solo necesitamos la lista de departamentos para el <select>.
     */
    private void mostrarFormularioNuevo(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // La JSP usará ${listaDepartamentos} para el <select>
        request.setAttribute(
                "listaDepartamentos",
                daoDepartamento.listarDepartamentos());

        // No se envía "empleado" → la JSP mostrará "Registrar Empleado"
        request.getRequestDispatcher("/empleadoForm.jsp")
               .forward(request, response);
    }

    // ── Acción: formulario editar ────────────────────────────

    /**
     * Carga el empleado indicado y lo envía a la JSP para pre-cargar el form.
     */
    private void mostrarFormularioEditar(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Leemos el ID del empleado a editar desde la URL (?id=X)
        int idEditar = Integer.parseInt(
                request.getParameter("id"));

        // Enviamos el empleado encontrado y la lista de departamentos
        request.setAttribute(
                "empleado",
                daoEmpleado.obtenerEmpleado(idEditar));

        request.setAttribute(
                "listaDepartamentos",
                daoDepartamento.listarDepartamentos());

        // La JSP detectará ${empleado} no vacío → mostrará "Editar Empleado"
        request.getRequestDispatcher("/empleadoForm.jsp")
               .forward(request, response);
    }

    // ── Acción: eliminar ─────────────────────────────────────

    /**
     * Elimina el empleado y redirige al listado.
     * La confirmación se hace en el lado cliente (onclick confirm en el JSP).
     */
    private void eliminarEmpleado(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int idEliminar = Integer.parseInt(
                request.getParameter("id"));

        daoEmpleado.eliminarEmpleado(idEliminar);

        // sendRedirect: el navegador hace GET /empleados → listado actualizado
        response.sendRedirect(
                request.getContextPath() + "/empleados");
    }

    // ════════════════════════════════════════════════════════
    // POST: crear o actualizar
    // ════════════════════════════════════════════════════════

    /**
     * Recibe los datos del formulario (POST) y decide si crear o actualizar.
     *
     * La distinción se hace con el campo hidden "idEmpleado":
     *   - vacío o null → es un registro nuevo (INSERT)
     *   - tiene valor   → es una actualización (UPDATE)
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        // ── Verificar sesión ─────────────────────────────────
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        // ── Leer ID del campo hidden ──────────────────────────
        // Si el form es de creación, idEmpleado estará vacío
        String idEmpleadoParam = request.getParameter("idEmpleado");

        // ── Construir el objeto Empleado con los datos del form ──
        Empleado empleado = new Empleado();

        // Conversión explícita: los parámetros siempre llegan como String
        empleado.setIdDepartamento(
                Integer.parseInt(request.getParameter("idDepartamento")));

        empleado.setNombres(
                request.getParameter("nombres"));

        empleado.setApellidos(
                request.getParameter("apellidos"));

        empleado.setCargo(
                request.getParameter("cargo"));

        // BigDecimal para salario (evita errores de punto flotante)
        empleado.setSalario(
                new BigDecimal(request.getParameter("salario")));

        empleado.setProyectos(
                Integer.parseInt(request.getParameter("proyectos")));

        // ── Decidir INSERT vs UPDATE ──────────────────────────
        if (idEmpleadoParam == null || idEmpleadoParam.isEmpty()) {

            // No hay ID → crear nuevo empleado
            daoEmpleado.crearEmpleado(empleado);

        } else {

            // Hay ID → actualizar empleado existente
            empleado.setIdEmpleado(
                    Integer.parseInt(idEmpleadoParam));

            daoEmpleado.actualizarEmpleado(empleado);
        }

        // Después de guardar, volvemos al listado
        response.sendRedirect(
                request.getContextPath() + "/empleados");
    }
}
