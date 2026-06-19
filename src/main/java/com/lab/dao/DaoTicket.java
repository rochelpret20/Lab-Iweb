package com.lab.dao;

import com.lab.beans.TicketTipo;
import com.lab.config.ConexionDB;
import com.lab.dto.TicketDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoTicket {

    public ArrayList<TicketDTO> listarTickets() {

        ArrayList<TicketDTO> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                tt.id_ticket_tipo,
                e.titulo,
                e.descripcion,
                e.fecha,
                e.lugar,
                tt.nombre,
                tt.precio,
                tt.cupo_disponible

                FROM ticket_tipo tt

                INNER JOIN evento e
                ON tt.id_evento =
                   e.id_evento

                ORDER BY e.fecha, tt.nombre
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                TicketDTO dto =
                        new TicketDTO();

                dto.setIdTicketTipo(
                        rs.getInt(
                                "id_ticket_tipo"));

                dto.setTituloEvento(
                        rs.getString(
                                "titulo"));

                dto.setDescripcionEvento(
                        rs.getString(
                                "descripcion"));

                dto.setFechaEvento(
                        rs.getDate(
                                "fecha")
                                .toLocalDate());

                dto.setLugarEvento(
                        rs.getString(
                                "lugar"));

                dto.setNombreTicket(
                        rs.getString(
                                "nombre"));

                dto.setPrecio(
                        rs.getBigDecimal(
                                "precio"));

                dto.setCupoDisponible(
                        rs.getInt(
                                "cupo_disponible"));

                lista.add(dto);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<TicketTipo> listarTicketsSelector() {

        ArrayList<TicketTipo> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                tt.id_ticket_tipo,
                tt.id_evento,
                tt.nombre,
                tt.precio,
                tt.cupo_total,
                tt.cupo_disponible,
                e.titulo,
                e.fecha

                FROM ticket_tipo tt

                INNER JOIN evento e
                ON tt.id_evento =
                   e.id_evento

                WHERE e.fecha >= CURDATE()
                AND tt.cupo_disponible > 0

                ORDER BY e.titulo, tt.nombre
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                TicketTipo tt =
                        new TicketTipo();

                tt.setIdTicketTipo(
                        rs.getInt(
                                "id_ticket_tipo"));

                tt.setIdEvento(
                        rs.getInt(
                                "id_evento"));

                tt.setNombre(
                        rs.getString(
                                "titulo")
                                + " - "
                                + rs.getString(
                                "nombre"));

                tt.setCupoDisponible(
                        rs.getInt(
                                "cupo_disponible"));

                lista.add(tt);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public TicketTipo obtenerTicket(
            int idTicketTipo) {

        TicketTipo tt = null;

        String sql = """
                SELECT
                tt.id_ticket_tipo,
                tt.id_evento,
                tt.nombre,
                tt.precio,
                tt.cupo_total,
                tt.cupo_disponible,
                e.fecha

                FROM ticket_tipo tt

                INNER JOIN evento e
                ON tt.id_evento =
                   e.id_evento

                WHERE tt.id_ticket_tipo = ?
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, idTicketTipo);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                tt = new TicketTipo();

                tt.setIdTicketTipo(
                        rs.getInt(
                                "id_ticket_tipo"));

                tt.setIdEvento(
                        rs.getInt(
                                "id_evento"));

                tt.setNombre(
                        rs.getString(
                                "nombre"));

                tt.setPrecio(
                        rs.getBigDecimal(
                                "precio"));

                tt.setCupoTotal(
                        rs.getInt(
                                "cupo_total"));

                tt.setCupoDisponible(
                        rs.getInt(
                                "cupo_disponible"));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return tt;
    }

    public void crearTicket(
            TicketTipo tt) {

        String sql = """
                INSERT INTO ticket_tipo
                (
                    id_evento,
                    nombre,
                    precio,
                    cupo_total,
                    cupo_disponible
                )
                VALUES
                (
                    ?, ?, ?, ?, ?
                )
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, tt.getIdEvento());

            ps.setString(2, tt.getNombre());

            ps.setBigDecimal(3, tt.getPrecio());

            ps.setInt(4, tt.getCupoTotal());

            ps.setInt(5, tt.getCupoDisponible());

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void eliminarTicket(
            int idTicketTipo) {

        String sql = """
                DELETE FROM ticket_tipo
                WHERE id_ticket_tipo = ?
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, idTicketTipo);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
