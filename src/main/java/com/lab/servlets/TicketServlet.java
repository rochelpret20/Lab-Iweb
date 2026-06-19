package com.lab.servlets;

import com.lab.beans.TicketTipo;
import com.lab.dao.DaoEvento;
import com.lab.dao.DaoTicket;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/tickets")
public class TicketServlet
        extends HttpServlet {

    private final DaoTicket daoTicket =
            new DaoTicket();

    private final DaoEvento daoEvento =
            new DaoEvento();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
            IOException {

        String action =
                request.getParameter("action");

        if (action == null) {

            request.setAttribute(
                    "listaTickets",
                    daoTicket.listarTickets());

            request.getRequestDispatcher(
                            "/tickets.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        switch (action) {

            case "nuevo":

                request.setAttribute(
                        "listaEventos",
                        daoEvento.listarEventosSelector());

                request.getRequestDispatcher(
                                "/ticketForm.jsp")
                        .forward(
                                request,
                                response);

                break;

            case "eliminar":

                int id =
                        Integer.parseInt(
                                request.getParameter(
                                        "id"));

                daoTicket.eliminarTicket(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/tickets");

                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException,
            ServletException {

        int idEvento =
                Integer.parseInt(
                        request.getParameter(
                                "idEvento"));

        String nombre =
                request.getParameter("nombre");

        BigDecimal precio =
                new BigDecimal(
                        request.getParameter(
                                "precio"));

        int cupoTotal =
                Integer.parseInt(
                        request.getParameter(
                                "cupoTotal"));

        int cupoDisponible =
                Integer.parseInt(
                        request.getParameter(
                                "cupoDisponible"));

        if (precio.compareTo(BigDecimal.ZERO) < 0
                || cupoTotal < 0
                || cupoDisponible < 0
                || cupoDisponible > cupoTotal) {

            request.setAttribute(
                    "error",
                    "Verifique los datos: precio ≥ 0, cupo total ≥ 0, cupo disponible ≥ 0 y cupo disponible ≤ cupo total.");

            request.setAttribute(
                    "listaEventos",
                    daoEvento.listarEventosSelector());

            request.getRequestDispatcher(
                            "/ticketForm.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        TicketTipo tt = new TicketTipo();

        tt.setIdEvento(idEvento);

        tt.setNombre(nombre);

        tt.setPrecio(precio);

        tt.setCupoTotal(cupoTotal);

        tt.setCupoDisponible(cupoDisponible);

        daoTicket.crearTicket(tt);

        response.sendRedirect(
                request.getContextPath()
                        + "/tickets");
    }
}
