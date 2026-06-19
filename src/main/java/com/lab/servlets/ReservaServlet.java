package com.lab.servlets;

import com.lab.beans.ReservaItem;
import com.lab.dao.DaoReserva;
import com.lab.dao.DaoTicket;
import com.lab.dao.DaoUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/reservas")
public class ReservaServlet
        extends HttpServlet {

    private final DaoReserva daoReserva =
            new DaoReserva();

    private final DaoTicket daoTicket =
            new DaoTicket();

    private final DaoUsuario daoUsuario =
            new DaoUsuario();

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
                    "listaReservas",
                    daoReserva.listarReservas());

            request.getRequestDispatcher(
                            "/reservas.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        switch (action) {

            case "nuevo":

                request.setAttribute(
                        "listaUsuarios",
                        daoUsuario.listarUsuarios());

                request.setAttribute(
                        "listaTickets",
                        daoTicket.listarTicketsSelector());

                request.getRequestDispatcher(
                                "/reservaForm.jsp")
                        .forward(
                                request,
                                response);

                break;

            case "eliminar":

                int id =
                        Integer.parseInt(
                                request.getParameter(
                                        "id"));

                daoReserva.eliminarReserva(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/reservas");

                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException,
            ServletException {

        int idUsuario =
                Integer.parseInt(
                        request.getParameter(
                                "idUsuario"));

        int idTicketTipo =
                Integer.parseInt(
                        request.getParameter(
                                "idTicketTipo"));

        int cantidad =
                Integer.parseInt(
                        request.getParameter(
                                "cantidad"));

        ReservaItem reserva =
                new ReservaItem();

        reserva.setIdUsuario(idUsuario);

        reserva.setIdTicketTipo(idTicketTipo);

        reserva.setCantidad(cantidad);

        String error =
                daoReserva.crearReserva(
                        reserva);

        if (error != null) {

            request.setAttribute(
                    "error",
                    error);

            request.setAttribute(
                    "listaUsuarios",
                    daoUsuario.listarUsuarios());

            request.setAttribute(
                    "listaTickets",
                    daoTicket.listarTicketsSelector());

            request.getRequestDispatcher(
                            "/reservaForm.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/reservas");
    }
}
