package com.lab.dao;

import com.lab.beans.Usuario;
import com.lab.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoUsuario {

    public ArrayList<Usuario> listarUsuarios() {

        ArrayList<Usuario> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                id_usuario,
                nombres,
                apellidos,
                email
                FROM usuario
                ORDER BY nombres, apellidos
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Usuario u =
                        new Usuario();

                u.setIdUsuario(
                        rs.getInt(
                                "id_usuario"));

                u.setNombres(
                        rs.getString(
                                "nombres"));

                u.setApellidos(
                        rs.getString(
                                "apellidos"));

                u.setEmail(
                        rs.getString(
                                "email"));

                lista.add(u);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
}
