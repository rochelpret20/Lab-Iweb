package com.lab.servlets;

import com.lab.beans.Evento;
import com.lab.dao.DaoEvento;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/eventos")
public class EventoServlet
        extends HttpServlet {

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
                    "listaEventos",
                    daoEvento.listarEventos());

            request.getRequestDispatcher(
                            "/eventos.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        switch (action) {

            case "nuevo":

                request.getRequestDispatcher(
                                "/eventoForm.jsp")
                        .forward(
                                request,
                                response);

                break;

            case "eliminar":

                int id =
                        Integer.parseInt(
                                request.getParameter(
                                        "id"));

                daoEvento.eliminarEvento(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/eventos");

                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException,
            ServletException {

        String titulo =
                request.getParameter("titulo");

        String descripcion =
                request.getParameter(
                        "descripcion");

        String fechaStr =
                request.getParameter("fecha");

        String lugar =
                request.getParameter("lugar");

        LocalDate fecha =
                LocalDate.parse(fechaStr);

        if (fecha.isBefore(LocalDate.now())) {

            request.setAttribute(
                    "error",
                    "La fecha del evento debe ser mayor o igual a hoy.");

            request.getRequestDispatcher(
                            "/eventoForm.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        Evento evento = new Evento();

        evento.setTitulo(titulo);

        evento.setDescripcion(descripcion);

        evento.setFecha(fecha);

        evento.setLugar(lugar);

        daoEvento.crearEvento(evento);

        response.sendRedirect(
                request.getContextPath()
                        + "/eventos");
    }
}
