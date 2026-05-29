

1. Base de Datos

Biblioteca.sql

CREATE DATABASE Biblioteca;
USE Biblioteca;
CREATE TABLE libro(
    idlibro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    anio_publicacion INT NOT NULL
);
INSERT INTO libro
(titulo,autor,editorial,anio_publicacion)
VALUES
('Clean Code','Robert Martin','Prentice Hall',2008),
('Java How To Program','Deitel','Pearson',2017),
('Redes de Computadoras','Andrew Tanenbaum','Pearson',2012);

⸻

2. Bean

Libro.java

package com.example.lab.beans;
/*
 * Bean que representa un registro
 * de la tabla libro.
 */
public class Libro {
    private int idlibro;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    public int getIdlibro() {
        return idlibro;
    }
    public void setIdlibro(int idlibro) {
        this.idlibro = idlibro;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    public int getAnioPublicacion() {
        return anioPublicacion;
    }
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }
}

⸻

3. DAO

LibroDao.java

package com.example.lab.daos;
import com.example.lab.beans.Libro;
import java.sql.*;
import java.util.ArrayList;
/*
 * DAO encargado de toda la comunicación
 * con la base de datos.
 */
public class LibroDao {
    private final String URL =
            "jdbc:mysql://localhost:3306/Biblioteca";
    private final String USER = "root";
    private final String PASSWORD = "root";
    /*
     * LISTAR LIBROS
     */
    public ArrayList<Libro> listar() {
        ArrayList<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libro";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);
            Statement stmt =
                    conn.createStatement();
            ResultSet rs =
                    stmt.executeQuery(sql);
            while(rs.next()) {
                Libro libro = new Libro();
                libro.setIdlibro(
                        rs.getInt("idlibro"));
                libro.setTitulo(
                        rs.getString("titulo"));
                libro.setAutor(
                        rs.getString("autor"));
                libro.setEditorial(
                        rs.getString("editorial"));
                libro.setAnioPublicacion(
                        rs.getInt("anio_publicacion"));
                lista.add(libro);
            }
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    /*
     * BUSCAR POR ID
     */
    public Libro buscarPorId(int id){
        Libro libro = null;
        String sql =
                "SELECT * FROM libro WHERE idlibro=?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);
            PreparedStatement pstmt =
                    conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            ResultSet rs =
                    pstmt.executeQuery();
            if(rs.next()){
                libro = new Libro();
                libro.setIdlibro(
                        rs.getInt("idlibro"));
                libro.setTitulo(
                        rs.getString("titulo"));
                libro.setAutor(
                        rs.getString("autor"));
                libro.setEditorial(
                        rs.getString("editorial"));
                libro.setAnioPublicacion(
                        rs.getInt("anio_publicacion"));
            }
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return libro;
    }
    /*
     * CREAR LIBRO
     */
    public void crear(String titulo,
                      String autor,
                      String editorial,
                      int anio){
        String sql =
                "INSERT INTO libro(titulo,autor,editorial,anio_publicacion) VALUES(?,?,?,?)";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);
            PreparedStatement pstmt =
                    conn.prepareStatement(sql);
            pstmt.setString(1,titulo);
            pstmt.setString(2,autor);
            pstmt.setString(3,editorial);
            pstmt.setInt(4,anio);
            pstmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*
     * ACTUALIZAR LIBRO
     */
    public void actualizar(Libro libro){
        String sql =
                "UPDATE libro SET titulo=?,autor=?,editorial=?,anio_publicacion=? WHERE idlibro=?";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);
            PreparedStatement pstmt =
                    conn.prepareStatement(sql);
            pstmt.setString(1,
                    libro.getTitulo());
            pstmt.setString(2,
                    libro.getAutor());
            pstmt.setString(3,
                    libro.getEditorial());
            pstmt.setInt(4,
                    libro.getAnioPublicacion());
            pstmt.setInt(5,
                    libro.getIdlibro());
            pstmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*
     * BORRAR LIBRO
     */
    public void borrar(int id){
        String sql =
                "DELETE FROM libro WHERE idlibro=?";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);
            PreparedStatement pstmt =
                    conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.executeUpdate();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

⸻

4. Servlet

LibroServlet.java

package com.example.lab.servlets;
import com.example.lab.beans.Libro;
import com.example.lab.daos.LibroDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
@WebServlet("/LibroServlet")
public class LibroServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String action =
                request.getParameter("action")
                        == null ?
                        "lista" :
                        request.getParameter("action");
        LibroDao dao = new LibroDao();
        switch(action){
            case "lista":
                ArrayList<Libro> lista =
                        dao.listar();
                request.setAttribute(
                        "lista",lista);
                request.getRequestDispatcher(
                        "libro/lista.jsp")
                        .forward(request,response);
                break;
        }
    }
}

⸻

5. Vista Principal

index.jsp

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Biblioteca</title>
</head>
<body>
<h1>CRUD Biblioteca</h1>
<a href="LibroServlet">
    Ingresar al sistema
</a>
</body>
</html>

⸻

6. Listado

lista.jsp

<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.lab.beans.Libro" %>
<jsp:useBean id="lista"
             scope="request"
             type="ArrayList<Libro>" />
<html>
<head>
    <title>Libros</title>
</head>
<body>
<h1>Lista de Libros</h1>
<table border="1">
<tr>
    <th>ID</th>
    <th>Título</th>
    <th>Autor</th>
    <th>Editorial</th>
    <th>Año</th>
</tr>
<% for(Libro libro : lista){ %>
<tr>
<td><%= libro.getIdlibro()%></td>
<td><%= libro.getTitulo()%></td>
<td><%= libro.getAutor()%></td>
<td><%= libro.getEditorial()%></td>
<td><%= libro.getAnioPublicacion()%></td>
</tr>
<% } %>
</table>
</body>
</html>

⸻

7. Formularios

form_new.jsp

<form method="post"
      action="LibroServlet">
    <input name="titulo">
    <input name="autor">
    <input name="editorial">
    <input name="anioPublicacion">
    <button type="submit">
        Guardar
    </button>
</form>

form_edit.jsp

<form method="post"
      action="LibroServlet?action=e">
    <input type="hidden"
           name="idlibro">
    <input name="titulo">
    <input name="autor">
    <input name="editorial">
    <input name="anioPublicacion">
    <button type="submit">
        Actualizar
    </button>
</form>

