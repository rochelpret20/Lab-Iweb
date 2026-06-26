package com.lab.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Servlet que cierra la sesión del usuario.
 *
 * ─────────────────────────────────────────────────────────────
 * CÓMO ADAPTAR A OTRO TEMA:
 *   Este servlet NO necesita ningún cambio entre proyectos.
 *   Solo verifica que el enlace en navbar.jspf apunte a /logout.
 * ─────────────────────────────────────────────────────────────
 *
 * Ruta: GET /logout
 *
 * Proceso:
 *   1. Obtiene la sesión actual (sin crear una nueva si no existe)
 *   2. Si existe, la invalida (elimina todos los atributos)
 *   3. Redirige al login
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        // getSession(false): retorna null si no hay sesión activa
        // Nunca usamos getSession(true) aquí porque no queremos crear una nueva
        HttpSession session = request.getSession(false);

        if (session != null) {
            // invalidate() elimina la sesión y libera todos sus atributos
            // (incluyendo el objeto Usuario que guardamos al hacer login)
            session.invalidate();
        }

        // Redirigimos al login; el usuario ya no está autenticado
        response.sendRedirect(
                request.getContextPath() + "/login");
    }
}
