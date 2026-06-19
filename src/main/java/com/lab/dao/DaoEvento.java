package com.lab.dao;

import com.lab.beans.Evento;
import com.lab.config.ConexionDB;
import com.lab.dto.EventoDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class DaoEvento {

    public ArrayList<EventoDTO> listarEventos() {

        ArrayList<EventoDTO> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                id_evento,
                titulo,
                descripcion,
                fecha,
                lugar
                FROM evento
                ORDER BY fecha
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                EventoDTO dto =
                        new EventoDTO();

                dto.setIdEvento(
                        rs.getInt(
                                "id_evento"));

                dto.setTitulo(
                        rs.getString(
                                "titulo"));

                dto.setDescripcion(
                        rs.getString(
                                "descripcion"));

                dto.setFecha(
                        rs.getDate(
                                "fecha")
                                .toLocalDate());

                dto.setLugar(
                        rs.getString(
                                "lugar"));

                lista.add(dto);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Evento> listarEventosSelector() {

        ArrayList<Evento> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                id_evento,
                titulo,
                fecha
                FROM evento
                WHERE fecha >= CURDATE()
                ORDER BY fecha
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Evento e =
                        new Evento();

                e.setIdEvento(
                        rs.getInt(
                                "id_evento"));

                e.setTitulo(
                        rs.getString(
                                "titulo"));

                e.setFecha(
                        rs.getDate(
                                "fecha")
                                .toLocalDate());

                lista.add(e);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public void crearEvento(
            Evento evento) {

        String sql = """
                INSERT INTO evento
                (
                    titulo,
                    descripcion,
                    fecha,
                    lugar
                )
                VALUES
                (
                    ?, ?, ?, ?
                )
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, evento.getTitulo());

            ps.setString(2, evento.getDescripcion());

            ps.setDate(3,
                    Date.valueOf(
                            evento.getFecha()));

            ps.setString(4, evento.getLugar());

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void eliminarEvento(
            int idEvento) {

        String sql = """
                DELETE FROM evento
                WHERE id_evento = ?
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, idEvento);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
