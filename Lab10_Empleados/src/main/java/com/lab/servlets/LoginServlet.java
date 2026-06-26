package com.lab.servlets;

import com.lab.beans.Usuario;
import com.lab.dao.DaoUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Servlet que maneja la autenticación de usuarios.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Este servlet casi no necesita cambios entre proyectos.
 *   Solo asegúrate de que DaoUsuario esté bien configurado.
 *   La URL de redirección después del login puede ajustarse:
 *     response.sendRedirect(...+ "/empleados");  ← cambia la ruta
 * ─────────────────────────────────────────────────────────────
 *
 * Rutas manejadas:
 *   GET  /login → muestra el formulario de login (login.jsp)
 *   POST /login → procesa las credenciales enviadas por el form
 *
 * Flujo POST:
 *   1. Lee email y password del formulario
 *   2. Consulta la BD vía DaoUsuario
 *   3. Si válido  → crea sesión y redirige a /empleados
 *   4. Si inválido → redirige a /login?error=1
 */
@WebServlet("/login") // Registra este servlet en la URL /login
public class LoginServlet extends HttpServlet {

    // Instanciamos el DAO una sola vez (el servlet es singleton en Tomcat)
    private final DaoUsuario daoUsuario = new DaoUsuario();

    // ── GET: mostrar formulario ──────────────────────────────

    /**
     * Responde a GET /login mostrando la página de inicio de sesión.
     * Solo hace forward a login.jsp; no procesa ningún dato.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Forward: el servidor internamente redirige a login.jsp
        // La URL en el navegador NO cambia (sigue siendo /login)
        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }

    // ── POST: procesar credenciales ──────────────────────────

    /**
     * Responde a POST /login procesando el formulario de login.
     * Crea la sesión HTTP si las credenciales son válidas.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        // 1. Leer parámetros del formulario
        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // 2. Validar en BD
        Usuario usuario = daoUsuario.validarUsuario(email, password);

        if (usuario != null) {

            // 3a. Credenciales correctas → crear sesión
            HttpSession session = request.getSession(); // crea sesión nueva

            // Guardamos el objeto Usuario en la sesión con la clave "usuario"
            // Cualquier servlet/JSP puede acceder con session.getAttribute("usuario")
            session.setAttribute("usuario", usuario);

            // Redirigimos al listado principal
            // sendRedirect: el navegador hace una nueva petición GET a /empleados
            response.sendRedirect(
                    request.getContextPath() + "/empleados");

        } else {

            // 3b. Credenciales incorrectas → volver al login con error
            // ?error=1 permite que login.jsp muestre el mensaje de error
            response.sendRedirect(
                    request.getContextPath() + "/login?error=1");
        }
    }
}
