package com.lab.dao;

import com.lab.beans.ReservaItem;
import com.lab.config.ConexionDB;
import com.lab.dto.ReservaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoReserva {

    public ArrayList<ReservaDTO> listarReservas() {

        ArrayList<ReservaDTO> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                ri.id_item,
                e.titulo,
                e.fecha,
                u.nombres,
                u.apellidos,
                u.email,
                tt.nombre nombre_ticket,
                ri.cantidad

                FROM reserva_item ri

                INNER JOIN ticket_tipo tt
                ON ri.id_ticket_tipo =
                   tt.id_ticket_tipo

                INNER JOIN evento e
                ON tt.id_evento =
                   e.id_evento

                INNER JOIN usuario u
                ON ri.id_usuario =
                   u.id_usuario

                ORDER BY ri.id_item
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                ReservaDTO dto =
                        new ReservaDTO();

                dto.setIdItem(
                        rs.getInt(
                                "id_item"));

                dto.setTituloEvento(
                        rs.getString(
                                "titulo"));

                dto.setFechaEvento(
                        rs.getDate(
                                "fecha")
                                .toLocalDate());

                dto.setNombres(
                        rs.getString(
                                "nombres"));

                dto.setApellidos(
                        rs.getString(
                                "apellidos"));

                dto.setEmail(
                        rs.getString(
                                "email"));

                dto.setNombreTicket(
                        rs.getString(
                                "nombre_ticket"));

                dto.setCantidad(
                        rs.getInt(
                                "cantidad"));

                lista.add(dto);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public String crearReserva(
            ReservaItem reserva) {

        String sqlTicket = """
                SELECT
                tt.cupo_disponible,
                e.fecha
                FROM ticket_tipo tt
                INNER JOIN evento e
                ON tt.id_evento = e.id_evento
                WHERE tt.id_ticket_tipo = ?
                """;

        String sqlInsert = """
                INSERT INTO reserva_item
                (
                    id_usuario,
                    id_ticket_tipo,
                    cantidad
                )
                VALUES
                (
                    ?, ?, ?
                )
                """;

        String sqlUpdate = """
                UPDATE ticket_tipo
                SET cupo_disponible =
                    cupo_disponible - ?
                WHERE id_ticket_tipo = ?
                """;

        try (Connection con =
                     ConexionDB.getConnection()) {

            PreparedStatement psTicket =
                    con.prepareStatement(
                            sqlTicket);

            psTicket.setInt(
                    1,
                    reserva.getIdTicketTipo());

            ResultSet rs =
                    psTicket.executeQuery();

            if (!rs.next()) {

                return "Ticket no encontrado.";
            }

            int cupoDisponible =
                    rs.getInt(
                            "cupo_disponible");

            java.time.LocalDate fechaEvento =
                    rs.getDate(
                            "fecha")
                            .toLocalDate();

            if (fechaEvento.isBefore(
                    java.time.LocalDate.now())) {

                return "El evento ya ocurrió.";
            }

            if (cupoDisponible <= 0) {

                return "No hay cupo disponible.";
            }

            if (reserva.getCantidad() >
                    cupoDisponible) {

                return "Cantidad supera el cupo disponible ("
                        + cupoDisponible + ").";
            }

            PreparedStatement psInsert =
                    con.prepareStatement(
                            sqlInsert);

            psInsert.setInt(
                    1,
                    reserva.getIdUsuario());

            psInsert.setInt(
                    2,
                    reserva.getIdTicketTipo());

            psInsert.setInt(
                    3,
                    reserva.getCantidad());

            psInsert.executeUpdate();

            PreparedStatement psUpdate =
                    con.prepareStatement(
                            sqlUpdate);

            psUpdate.setInt(
                    1,
                    reserva.getCantidad());

            psUpdate.setInt(
                    2,
                    reserva.getIdTicketTipo());

            psUpdate.executeUpdate();

            return null;

        } catch (Exception e) {

            e.printStackTrace();

            return "Error al crear la reserva.";
        }
    }

    public void eliminarReserva(
            int idItem) {

        String sqlReserva = """
                SELECT id_ticket_tipo, cantidad
                FROM reserva_item
                WHERE id_item = ?
                """;

        String sqlDelete = """
                DELETE FROM reserva_item
                WHERE id_item = ?
                """;

        String sqlRestaurar = """
                UPDATE ticket_tipo
                SET cupo_disponible =
                    cupo_disponible + ?
                WHERE id_ticket_tipo = ?
                """;

        try (Connection con =
                     ConexionDB.getConnection()) {

            PreparedStatement psReserva =
                    con.prepareStatement(
                            sqlReserva);

            psReserva.setInt(1, idItem);

            ResultSet rs =
                    psReserva.executeQuery();

            if (!rs.next()) {
                return;
            }

            int idTicketTipo =
                    rs.getInt(
                            "id_ticket_tipo");

            int cantidad =
                    rs.getInt(
                            "cantidad");

            PreparedStatement psDelete =
                    con.prepareStatement(
                            sqlDelete);

            psDelete.setInt(1, idItem);

            psDelete.executeUpdate();

            PreparedStatement psRestaurar =
                    con.prepareStatement(
                            sqlRestaurar);

            psRestaurar.setInt(1, cantidad);

            psRestaurar.setInt(
                    2,
                    idTicketTipo);

            psRestaurar.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
