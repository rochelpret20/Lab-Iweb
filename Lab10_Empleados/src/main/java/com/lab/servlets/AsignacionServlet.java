package com.lab.servlets;

import com.lab.beans.Usuario;
import com.lab.dao.DaoAsignacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Servlet que gestiona la lista de asignaciones del usuario.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   1. Cambia @WebServlet("/asignaciones") según tu contexto.
 *   2. Reemplaza DaoAsignacion por tu DAO equivalente.
 *   3. Ajusta los nombres de atributos que se envían al JSP.
 *   4. Actualiza la URL de redirección tras agregar un item.
 * ─────────────────────────────────────────────────────────────
 *
 * Rutas manejadas (solo GET):
 *   GET /asignaciones                          → muestra la lista de asignaciones
 *   GET /asignaciones?action=agregar&id=X      → agrega el empleado X y redirige
 */
@WebServlet("/asignaciones")
public class AsignacionServlet extends HttpServlet {

    // DAO de asignaciones (instanciado una sola vez)
    private final DaoAsignacion daoAsignacion = new DaoAsignacion();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // ── 1. Verificar sesión ───────────────────────────────
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        // ── 2. Obtener el usuario de la sesión ────────────────
        // Lo necesitamos para filtrar las asignaciones de ESTE usuario
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // ── 3. Leer parámetro de acción ───────────────────────
        String action = request.getParameter("action");

        if ("agregar".equals(action)) {

            // ── Agregar empleado a la asignación ─────────────
            int idEmpleado = Integer.parseInt(
                    request.getParameter("id"));

            // Delegamos al DAO la lógica INSERT o UPDATE
            daoAsignacion.agregarEmpleado(
                    usuario.getIdUsuario(),
                    idEmpleado);

            // Después de agregar, volvemos al listado de empleados
            // (no al de asignaciones, igual que el carrito original)
            response.sendRedirect(
                    request.getContextPath() + "/empleados");

            return; // Importante: no seguir ejecutando el método
        }

        // ── 4. Mostrar listado de asignaciones ────────────────
        // Sin action → cargamos la lista y la pasamos al JSP

        // Lista de asignaciones del usuario actual
        request.setAttribute(
                "listaAsignaciones",
                daoAsignacion.listarAsignaciones(
                        usuario.getIdUsuario()));

        // Costo total de todas las asignaciones del usuario
        request.setAttribute(
                "costoTotal",
                daoAsignacion.obtenerCostoTotal(
                        usuario.getIdUsuario()));

        // Forward al JSP de asignaciones
        request.getRequestDispatcher("/asignaciones.jsp")
               .forward(request, response);
    }
}
