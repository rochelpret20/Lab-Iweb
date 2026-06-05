Beans

1.Clase.java
package com.lab.beans;

import java.math.BigDecimal;

public class Clase {

    private int idClase;

    private String nombreClase;

    private int idEntrenador;

    private String nombreEntrenador;

    private int cuposDisponibles;

    private BigDecimal precio;

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public int getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    public void setNombreEntrenador(String nombreEntrenador) {
        this.nombreEntrenador = nombreEntrenador;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}

2. Entrenador.java
package com.lab.beans;

public class Entrenador {

    private int idEntrenador;

    private String nombre;

    private String especialidad;

    private String telefono;

    public int getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

Config
1.ConexionDB
package com.lab.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/gimnasio_db?serverTimezone=America/Lima";

    private static final String USER = "root";

    private static final String PASSWORD = "Rosell2025";

    public static Connection getConnection() throws SQLException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);

        }

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}

DAO
1.DaoClase
package com.lab.dao;

import com.lab.beans.Clase;
import com.lab.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoClase {

    public ArrayList<Clase> listarClasesPorEntrenador(int idEntrenador) {

        ArrayList<Clase> lista = new ArrayList<>();

        String sql = """
                SELECT c.*,
                       e.nombre AS nombre_entrenador
                FROM clases c
                INNER JOIN entrenadores e
                    ON c.id_entrenador = e.id_entrenador
                WHERE c.id_entrenador = ?
                ORDER BY c.id_clase
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEntrenador);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Clase clase = new Clase();

                clase.setIdClase(
                        rs.getInt("id_clase"));

                clase.setNombreClase(
                        rs.getString("nombre_clase"));

                clase.setIdEntrenador(
                        rs.getInt("id_entrenador"));

                clase.setNombreEntrenador(
                        rs.getString("nombre_entrenador"));

                clase.setCuposDisponibles(
                        rs.getInt("cupos_disponibles"));

                clase.setPrecio(
                        rs.getBigDecimal("precio"));

                lista.add(clase);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}

2.DaoEntrenador
package com.lab.dao;

import com.lab.beans.Entrenador;
import com.lab.config.ConexionDB;

import java.sql.*;
import java.util.ArrayList;

public class DaoEntrenador {

    public ArrayList<Entrenador> listarEntrenadores() {

        ArrayList<Entrenador> lista = new ArrayList<>();

        String sql = """
                SELECT *
                FROM entrenadores
                ORDER BY id_entrenador
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Entrenador entrenador = new Entrenador();

                entrenador.setIdEntrenador(
                        rs.getInt("id_entrenador"));

                entrenador.setNombre(
                        rs.getString("nombre"));

                entrenador.setEspecialidad(
                        rs.getString("especialidad"));

                entrenador.setTelefono(
                        rs.getString("telefono"));

                lista.add(entrenador);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Entrenador obtenerEntrenador(int id) {

        Entrenador entrenador = null;

        String sql = """
                SELECT *
                FROM entrenadores
                WHERE id_entrenador = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                entrenador = new Entrenador();

                entrenador.setIdEntrenador(
                        rs.getInt("id_entrenador"));

                entrenador.setNombre(
                        rs.getString("nombre"));

                entrenador.setEspecialidad(
                        rs.getString("especialidad"));

                entrenador.setTelefono(
                        rs.getString("telefono"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entrenador;
    }

    public void crearEntrenador(Entrenador entrenador) {

        String sql = """
                INSERT INTO entrenadores
                (nombre, especialidad, telefono)
                VALUES (?, ?, ?)
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,
                    entrenador.getNombre());

            ps.setString(2,
                    entrenador.getEspecialidad());

            ps.setString(3,
                    entrenador.getTelefono());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarEntrenador(Entrenador entrenador) {

        String sql = """
                UPDATE entrenadores
                SET nombre=?,
                    especialidad=?,
                    telefono=?
                WHERE id_entrenador=?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,
                    entrenador.getNombre());

            ps.setString(2,
                    entrenador.getEspecialidad());

            ps.setString(3,
                    entrenador.getTelefono());

            ps.setInt(4,
                    entrenador.getIdEntrenador());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void borrarEntrenador(int id) {

        String sql = """
                DELETE FROM entrenadores
                WHERE id_entrenador=?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

Servlet
1.EntrenadorServlet
package com.lab.servlets;

import com.lab.beans.Entrenador;
import com.lab.dao.DaoClase;
import com.lab.dao.DaoEntrenador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/inicio")
public class EntrenadorServlet extends HttpServlet {

    private final DaoEntrenador daoEntrenador =
            new DaoEntrenador();

    private final DaoClase daoClase =
            new DaoClase();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action =
                request.getParameter("action");

        if (action == null) {

            request.setAttribute(
                    "listaEntrenadores",
                    daoEntrenador.listarEntrenadores());

            request.getRequestDispatcher(
                            "/inicio.jsp")
                    .forward(request, response);

            return;
        }

        switch (action) {

            case "nuevo":

                request.getRequestDispatcher(
                                "/entrenadorForm.jsp")
                        .forward(request, response);

                break;

            case "editar":

                int idEditar =
                        Integer.parseInt(
                                request.getParameter("id"));

                Entrenador entrenadorEditar =
                        daoEntrenador.obtenerEntrenador(
                                idEditar);

                request.setAttribute(
                        "entrenador",
                        entrenadorEditar);

                request.getRequestDispatcher(
                                "/entrenadorForm.jsp")
                        .forward(request, response);

                break;

            case "eliminar":

                int idEliminar =
                        Integer.parseInt(
                                request.getParameter("id"));

                daoEntrenador.borrarEntrenador(
                        idEliminar);

                response.sendRedirect(
                        request.getContextPath()
                                + "/inicio");

                break;

            case "detalle":

                int idDetalle =
                        Integer.parseInt(
                                request.getParameter("id"));

                Entrenador entrenadorDetalle =
                        daoEntrenador.obtenerEntrenador(
                                idDetalle);

                request.setAttribute(
                        "entrenador",
                        entrenadorDetalle);

                request.setAttribute(
                        "listaClases",
                        daoClase.listarClasesPorEntrenador(
                                idDetalle));

                request.getRequestDispatcher(
                                "/detalleEntrenador.jsp")
                        .forward(request, response);

                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id =
                request.getParameter(
                        "idEntrenador");

        Entrenador entrenador =
                new Entrenador();

        entrenador.setNombre(
                request.getParameter("nombre"));

        entrenador.setEspecialidad(
                request.getParameter("especialidad"));

        entrenador.setTelefono(
                request.getParameter("telefono"));

        if (id == null || id.isEmpty()) {

            daoEntrenador.crearEntrenador(
                    entrenador);

        } else {

            entrenador.setIdEntrenador(
                    Integer.parseInt(id));

            daoEntrenador.actualizarEntrenador(
                    entrenador);
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/inicio");
    }
} 

CSS
1.estilos.css
body{
    font-family: Arial, sans-serif;
    background:#f4f6f9;
    margin:0;
    padding:30px;
}

.contenedor{
    width:90%;
    margin:auto;
}

.contenedor-form{
    display:flex;
    justify-content:center;
    align-items:center;
    min-height:80vh;
}

h1{
    color:#1f2937;
    margin-bottom:20px;
}

h2{
    color:#374151;
}

.barra-superior{
    margin-bottom:20px;
}

table{
    width:100%;
    border-collapse:collapse;
    background:white;
    box-shadow:0 2px 10px rgba(0,0,0,0.1);
}

th{
    background:#2563eb;
    color:white;
    padding:12px;
}

td{
    padding:12px;
    border-bottom:1px solid #ddd;
}

tr:hover{
    background:#f8fafc;
}

.card{
    background:white;
    padding:25px;
    border-radius:10px;
    box-shadow:0 2px 10px rgba(0,0,0,0.1);
}

form{
    display:flex;
    flex-direction:column;
}

label{
    margin-top:10px;
    margin-bottom:5px;
    font-weight:bold;
}

input{
    padding:10px;
    border:1px solid #ccc;
    border-radius:6px;
}

.botones-form{
    margin-top:20px;
    display:flex;
    gap:10px;
}

.btn{
    text-decoration:none;
    color:white;
    padding:8px 14px;
    border-radius:6px;
    font-size:14px;
    display:inline-block;
}

.btn-nuevo{
    background:#16a34a;
}

.btn-editar{
    background:#2563eb;
}

.btn-eliminar{
    background:#dc2626;
}

.btn-detalle{
    background:#9333ea;
}

.btn-regresar{
    background:#6b7280;
}

button{
    background:#2563eb;
    color:white;
    border:none;
    padding:10px 18px;
    border-radius:6px;
    cursor:pointer;
}

button:hover{
    opacity:0.9;
}


WEB-INF
1.web.xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app
        xmlns="https://jakarta.ee/xml/ns/jakartaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="
        https://jakarta.ee/xml/ns/jakartaee
        https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
        version="6.0">

    <display-name>Lab6_20206399</display-name>

    <welcome-file-list>
        <welcome-file>inicio</welcome-file>
    </welcome-file-list>

</web-app>

POM
pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lab</groupId>
    <artifactId>Lab6_20206399</artifactId>
    <version>1.0-SNAPSHOT</version>

    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>3.0.0</version>
        </dependency>

        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>3.0.1</version>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.4.0</version>
        </dependency>

    </dependencies>

</project>

JSP
1.detalleEntrenador.jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Detalle Entrenador</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="contenedor">

    <h1>Detalle del Entrenador</h1>

    <div class="card">

        <p>
            <strong>ID:</strong>
            ${entrenador.idEntrenador}
        </p>

        <p>
            <strong>Nombre:</strong>
            ${entrenador.nombre}
        </p>

        <p>
            <strong>Especialidad:</strong>
            ${entrenador.especialidad}
        </p>

        <p>
            <strong>Teléfono:</strong>
            ${entrenador.telefono}
        </p>

    </div>

    <br>

    <h2>Clases Asignadas</h2>

    <table>

        <thead>

        <tr>
            <th>ID</th>
            <th>Clase</th>
            <th>Cupos Disponibles</th>
            <th>Precio</th>
        </tr>

        </thead>

        <tbody>

        <c:forEach var="c" items="${listaClases}">

            <tr>

                <td>${c.idClase}</td>

                <td>${c.nombreClase}</td>

                <td>${c.cuposDisponibles}</td>

                <td>S/ ${c.precio}</td>

            </tr>

        </c:forEach>

        </tbody>

    </table>

    <br>

    <a class="btn btn-regresar"
       href="${pageContext.request.contextPath}/inicio">

        Regresar al listado

    </a>

</div>

</body>
</html>
2.entrenadorForm.jsp
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Entrenador</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="contenedor-form">

    <div class="card">

        <h2>

            ${empty entrenador ?
                    "Registrar Entrenador"
                    :
                    "Editar Entrenador"}

        </h2>

        <form method="post"
              action="${pageContext.request.contextPath}/inicio">

            <input type="hidden"
                   name="idEntrenador"
                   value="${entrenador.idEntrenador}">

            <label>Nombre</label>

            <input type="text"
                   name="nombre"
                   value="${entrenador.nombre}"
                   required>

            <label>Especialidad</label>

            <input type="text"
                   name="especialidad"
                   value="${entrenador.especialidad}"
                   required>

            <label>Teléfono</label>

            <input type="text"
                   name="telefono"
                   value="${entrenador.telefono}"
                   required>

            <div class="botones-form">

                <button type="submit">
                    Guardar
                </button>

                <a class="btn btn-regresar"
                   href="${pageContext.request.contextPath}/inicio">
                    Cancelar
                </a>

            </div>

        </form>

    </div>

</div>

</body>
</html>

3.inicio.jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FitControl - Entrenadores</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="contenedor">

    <h1>Gestión de Entrenadores</h1>

    <div class="barra-superior">

        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/inicio?action=nuevo">
            + Nuevo Entrenador
        </a>

    </div>

    <table>

        <thead>

        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Especialidad</th>
            <th>Teléfono</th>
            <th>Acciones</th>
        </tr>

        </thead>

        <tbody>

        <c:forEach var="e" items="${listaEntrenadores}">

            <tr>

                <td>${e.idEntrenador}</td>

                <td>${e.nombre}</td>

                <td>${e.especialidad}</td>

                <td>${e.telefono}</td>

                <td>

                    <a class="btn btn-editar"
                       href="${pageContext.request.contextPath}/inicio?action=editar&id=${e.idEntrenador}">
                        Editar
                    </a>

                    <a class="btn btn-eliminar"
                       href="${pageContext.request.contextPath}/inicio?action=eliminar&id=${e.idEntrenador}"
                       onclick="return confirm('¿Está seguro de eliminar este entrenador?');">
                        Eliminar
                    </a>

                    <a class="btn btn-detalle"
                       href="${pageContext.request.contextPath}/inicio?action=detalle&id=${e.idEntrenador}">
                        Detalle
                    </a>

                </td>

            </tr>

        </c:forEach>

        </tbody>

    </table>

</div>

</body>
</html>
