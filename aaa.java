Para resolver este laboratorio utilizando **Java (Jakarta EE / Java EE) con Maven**, estructuraremos el proyecto siguiendo una arquitectura MVC limpia (Modelo-Vista-Controlador) utilizando Servlets, JSPs, DAOs y un Filtro de autenticación para asegurar las sesiones.

Dado que el enunciado menciona `"2026-1"` y requiere el uso de cifrado, usaremos **SHA-256** para validar la contraseña del usuario de ejemplo (`d54123de...`)  que ya viene en tu dump.

Aquí tienes la guía de implementación paso a paso con todo el código necesario:

---

## 1. Configuración de Maven (`pom.xml`)

Este archivo gestionará las dependencias necesarias: Jakarta EE (o Java EE), JSTL para los JSPs y el conector de MySQL.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>pe.edu.pucp</groupId>
    <artifactId>LAB9_CODIGO</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-api</artifactId>
            <version>9.1.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>2.0.1</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>LAB9_CODIGO</finalName>
    </build>
</project>

```

*(Nota: Si utilizas servidores más antiguos como Tomcat 9, cambia las dependencias de `jakarta` a sus equivalentes de `javax`).*

---

## 2. Modelos / Beans (`pe.edu.pucp.model.bean`)

### Usuario.java

```java
package pe.edu.pucp.model.bean;

public class Usuario {
    private int idUsuarios;
    private String nombre;
    private String apellido;
    private String pass;
    private String dni;
    private String correo;

    // Constructores, Getters y Setters
    public Usuario() {}

    public int getIdUsuarios() { return idUsuarios; }
    public void setIdUsuarios(int idUsuarios) { this.idUsuarios = idUsuarios; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}

```

### Transaccion.java

```java
package pe.edu.pucp.model.bean;
import java.sql.Date;

public class Transaccion {
    private int idTransacciones;
    private double monto;
    private String descripcion;
    private String titulo;
    private Date fecha;
    private String tipo;
    private Usuario usuario; // Relación con el Bean Usuario

    public Transaccion() {}

    // Getters y Setters
    public int getIdTransacciones() { return idTransacciones; }
    public void setIdTransacciones(int idTransacciones) { this.idTransacciones = idTransacciones; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

```

---

## 3. Utilidades y Conexión (`pe.edu.pucp.util`)

### DbConnection.java

```java
package pe.edu.pucp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3300/mydb?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root"; // Cambia según tu password de MySQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

```

### PasswordUtil.java (Cifrado SHA-256)

```java
package pe.edu.pucp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

```

---

## 4. Capa de Acceso a Datos (`pe.edu.pucp.model.dao`)

### UsuarioDao.java

```java
package pe.edu.pucp.model.dao;

import pe.edu.pucp.model.bean.Usuario;
import pe.edu.pucp.util.DbConnection;
import pe.edu.pucp.util.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public Usuario login(String correo, String password) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND pass = ?";
        String hashed = PasswordUtil.hashPassword(password);
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, hashed);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuarios(rs.getInt("idusuarios"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setDni(rs.getString("dni"));
                    u.setCorreo(rs.getString("correo"));
                    return u;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuarios(rs.getInt("idusuarios"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setDni(rs.getString("dni"));
                u.setCorreo(rs.getString("correo"));
                lista.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean registrarUsuario(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, pass, dni, correo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, PasswordUtil.hashPassword(u.getPass()));
            ps.setString(4, u.getDni());
            ps.setString(5, u.getCorreo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean existeDni(String dni) {
        String sql = "SELECT 1 FROM usuarios WHERE dni = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { return false; }
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { return false; }
    }
}

```

### TransaccionDao.java

```java
package pe.edu.pucp.model.dao;

import pe.edu.pucp.model.bean.Transaccion;
import pe.edu.pucp.model.bean.Usuario;
import pe.edu.pucp.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDao {

    // Se realiza en una sola Query con INNER JOIN exigido por la rúbrica
    public List<Transaccion> listarPorUsuario(int idUsuario) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT t.*, u.nombre, u.apellido FROM transacciones t " +
                     "INNER JOIN usuarios u ON t.usuarios_idusuarios = u.idusuarios " +
                     "WHERE t.usuarios_idusuarios = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransacciones(rs.getInt("idtransacciones"));
                    t.setMonto(rs.getDouble("monto"));
                    t.setDescripcion(rs.getString("descripcion"));
                    t.setTitulo(rs.getString("titulo"));
                    t.setFecha(rs.getDate("fecha"));
                    t.setTipo(rs.getString("tipo"));

                    Usuario u = new Usuario();
                    u.setIdUsuarios(rs.getInt("usuarios_idusuarios"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    t.setUsuario(u);

                    lista.add(t);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean registrarTransaccion(Transaccion t) {
        String sql = "INSERT INTO transacciones (monto, descripcion, titulo, fecha, usuarios_idusuarios, tipo) VALUES (?, ?, ?, CURDATE(), ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, t.getMonto());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getTitulo());
            ps.setInt(4, t.getUsuario().getIdUsuarios());
            ps.setString(5, t.getTipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean eliminarTransaccion(int idTransaccion, int idUsuarioSesion) {
        String sql = "DELETE FROM transacciones WHERE idtransacciones = ? AND usuarios_idusuarios = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransaccion);
            ps.setInt(2, idUsuarioSesion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}

```

---

## 5. Controladores y Filtros (`pe.edu.pucp.controller`)

### AuthFilter.java (Seguridad de Sesiones)

```java
package pe.edu.pucp.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/UsuariosServlet", "/TransaccionesServlet", "/views/*"})
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuarioSesion") == null) {
            res.sendRedirect(req.getContextPath() + "/LoginServlet");
        } else {
            chain.doFilter(request, response);
        }
    }
}

```

### LoginServlet.java

```java
package pe.edu.pucp.controller;

import pe.edu.pucp.model.bean.Usuario;
import pe.edu.pucp.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet", ""})
public class LoginServlet extends HttpServlet {
    private UsuarioDao usuarioDao = new UsuarioDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            request.getSession().invalidate();
            response.sendRedirect("LoginServlet");
            return;
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String pass = request.getParameter("pass");

        Usuario u = usuarioDao.login(correo, pass);
        if (u != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioSesion", u);
            response.sendRedirect("UsuariosServlet");
        } else {
            request.setAttribute("error", "Credenciales incorrectas");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}

```

### UsuariosServlet.java

```java
package pe.edu.pucp.controller;

import pe.edu.pucp.model.bean.Usuario;
import pe.edu.pucp.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/UsuariosServlet")
public class UsuariosServlet extends HttpServlet {
    private UsuarioDao usuarioDao = new UsuarioDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");

        if (action.equals("listar")) {
            request.setAttribute("listaUsuarios", usuarioDao.listarUsuarios());
            request.getRequestDispatcher("/views/usuarios/lista.jsp").forward(request, response);
        } else if (action.equals("nuevo")) {
            request.getRequestDispatcher("/views/usuarios/nuevo.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String correo = request.getParameter("correo");
        String pass = request.getParameter("pass");

        // Validaciones del Servidor exigidas por la rúbrica
        String error = null;
        if(nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty()) {
            error = "Nombre y apellido son obligatorios.";
        } else if(dni == null || !dni.matches("\\d{8}")) {
            error = "El DNI debe tener 8 dígitos numéricos.";
        } else if(usuarioDao.existeDni(dni)) {
            error = "El DNI ya se encuentra registrado.";
        } else if(usuarioDao.existeCorreo(correo)) {
            error = "El Correo ya se encuentra registrado.";
        } else if(pass == null || pass.length() < 8 || !pass.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$")) {
            error = "La contraseña debe tener mínimo 8 caracteres con letras y números.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/views/usuarios/nuevo.jsp").forward(request, response);
        } else {
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setDni(dni);
            u.setCorreo(correo);
            u.setPass(pass);
            usuarioDao.registrarUsuario(u);
            response.sendRedirect("UsuariosServlet");
        }
    }
}

```

### TransaccionesServlet.java

```java
package pe.edu.pucp.controller;

import pe.edu.pucp.model.bean.Transaccion;
import pe.edu.pucp.model.bean.Usuario;
import pe.edu.pucp.model.dao.TransaccionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/TransaccionesServlet")
public class TransaccionesServlet extends HttpServlet {
    private TransaccionDao transaccionDao = new TransaccionDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioSesion");

        if (action.equals("listar")) {
            request.setAttribute("listaTransacciones", transaccionDao.listarPorUsuario(usuarioSesion.getIdUsuarios()));
            request.getRequestDispatcher("/views/transacciones/lista.jsp").forward(request, response);
        } else if (action.equals("nuevo")) {
            request.getRequestDispatcher("/views/transacciones/nuevo.jsp").forward(request, response);
        } else if (action.equals("borrar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            transaccionDao.eliminarTransaccion(id, usuarioSesion.getIdUsuarios());
            response.sendRedirect("TransaccionesServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String titulo = request.getParameter("titulo");
        String tipo = request.getParameter("tipo");
        String descripcion = request.getParameter("descripcion");
        String montoStr = request.getParameter("monto");
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioSesion");

        double monto = 0;
        String error = null;

        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) error = "El monto debe ser mayor a 0.";
        } catch (Exception e) {
            error = "El monto debe ser un número válido.";
        }

        if (titulo == null || titulo.isEmpty() || tipo == null || tipo.isEmpty()) {
            error = "Título y Tipo son campos requeridos.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/views/transacciones/nuevo.jsp").forward(request, response);
        } else {
            Transaccion t = new Transaccion();
            t.setTitulo(titulo);
            t.setTipo(tipo);
            t.setDescripcion(descripcion);
            t.setMonto(monto);
            t.setUsuario(usuarioSesion);

            transaccionDao.registrarTransaccion(t);
            response.sendRedirect("TransaccionesServlet");
        }
    }
}

```

---

## 6. Vistas (JSPs) con Bootstrap

Asegúrate de colocar los archivos en la estructura de carpetas correcta dentro de `src/main/webapp/`.

### `src/main/webapp/includes/navbar.jsp` (Exigido usando Include)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Gestión de Gastos - ${sessionScope.usuarioSesion.nombre}</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/UsuariosServlet">Usuarios</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/TransaccionesServlet">Transacciones</a>
                </li>
            </ul>
            <span class="navbar-text">
                <a class="btn btn-outline-danger btn-sm text-white" href="${pageContext.request.contextPath}/LoginServlet?action=logout">Cerrar Sesión</a>
            </span>
        </div>
    </div>
</nav>

```

### `src/main/webapp/index.jsp` (Login)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Iniciar Sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5" style="max-width: 400px;">
    <div class="card p-4 shadow-sm">
        <h3 class="text-center mb-4">Iniciar Sesión</h3>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
        <% } %>
        <form action="LoginServlet" method="POST">
            <div class="mb-3">
                <label class="form-label">Correo</label>
                <input type="email" name="correo" class="form-content form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Contraseña</label>
                <input type="password" name="pass" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Ingresar</button>
        </form>
    </div>
</div>
</body>
</html>

```

### `src/main/webapp/views/usuarios/lista.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/鏡t/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lista de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/includes/navbar.jsp" %>
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Lista de Usuarios</h2>
            <a href="UsuariosServlet?action=nuevo" class="btn btn-success">Registrar Nuevo Usuario</a>
        </div>
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>DNI</th>
                    <th>Correo</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${listaUsuarios}">
                    <tr>
                        <td>${u.idUsuarios}</td>
                        <td>${u.nombre}</td>
                        <td>${u.apellido}</td>
                        <td>${u.dni}</td>
                        <td>${u.correo}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

```

### `src/main/webapp/views/usuarios/nuevo.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nuevo Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/includes/navbar.jsp" %>
    <div class="container" style="max-width: 500px;">
        <h2>Registrar Usuario</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
        <% } %>
        <form action="UsuariosServlet" method="POST" class="mt-3">
            <div class="mb-3">
                <label class="form-label">Nombre</label>
                <input type="text" name="nombre" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Apellido</label>
                <input type="text" name="apellido" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">DNI</label>
                <input type="text" name="dni" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Correo</label>
                <input type="email" name="correo" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Contraseña</label>
                <input type="password" name="pass" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary">Guardar</button>
            <a href="UsuariosServlet" class="btn btn-secondary">Cancelar</a>
        </form>
    </div>
</body>
</html>

```

### `src/main/webapp/views/transacciones/lista.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mis Transacciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/includes/navbar.jsp" %>
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Mis Transacciones</h2>
            <a href="TransaccionesServlet?action=nuevo" class="btn btn-success">Crear Transacción</a>
        </div>
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Monto</th>
                    <th>Tipo</th>
                    <th>Descripción</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="t" items="${listaTransacciones}">
                    <tr>
                        <td>${t.idTransacciones}</td>
                        <td>${t.titulo}</td>
                        <td>$ ${t.monto}</td>
                        <td>
                            <span class="badge ${t.tipo == 'ingreso' ? 'bg-success' : 'bg-danger'}">
                                ${t.tipo}
                            </span>
                        </td>
                        <td>${t.descripcion}</td>
                        <td>${t.fecha}</td>
                        <td>
                            <a href="TransaccionesServlet?action=borrar&id=${t.idTransacciones}" 
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Seguro que deseas borrar este gasto?');">
                               Borrar
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

```

### `src/main/webapp/views/transacciones/nuevo.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nueva Transacción</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="/includes/navbar.jsp" %>
    <div class="container" style="max-width: 500px;">
        <h2>Crear Transacción</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
        <% } %>
        <form action="TransaccionesServlet" method="POST" class="mt-3">
            <div class="mb-3">
                <label class="form-label">Título</label>
                <input type="text" name="titulo" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Tipo</label>
                <select name="tipo" class="form-select" required>
                    <option value="ingreso">Ingreso</option>
                    <option value="egreso">Egreso</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Monto</label>
                <input type="number" step="0.01" name="monto" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Descripción</label>
                <textarea name="descripcion" class="form-control" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Guardar</button>
            <a href="TransaccionesServlet" class="btn btn-secondary">Cancelar</a>
        </form>
    </div>
</body>
</html>

```

---

## Estructura Final del Proyecto en Maven

Asegúrate de que tu directorio de archivos quede organizado exactamente de este modo para evitar errores de empaquetado:

```text
LAB9_CODIGO/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── pe/edu/pucp/
│       │       ├── controller/
│       │       │   ├── AuthFilter.java
│       │       │   ├── LoginServlet.java
│       │       │   ├── TransaccionesServlet.java
│       │       │   └── UsuariosServlet.java
│       │       ├── model/
│       │       │   ├── bean/
│       │       │   │   ├── Transaccion.java
│       │       │   │   └── Usuario.java
│       │       │   └── dao/
│       │       │       ├── TransaccionDao.java
│       │       │       └── UsuarioDao.java
│       │       └── util/
│       │           ├── DbConnection.java
│       │           └── PasswordUtil.java
│       └── webapp/
│           ├── index.jsp
│           ├── includes/
│           │   └── navbar.jsp
│           └── views/
│               ├── transacciones/
│               │   ├── lista.jsp
│               │   └── nuevo.jsp
│               └── usuarios/
│                   ├── lista.jsp
│                   └── nuevo.jsp
├── pom.xml
Dependency 'org.glassfish.web:jakarta.servlet.jsp.jstl:2.0.1' not found
Dependency 'org.glassfish.web:jakarta.servlet.jsp.jstl:2.0.1' not found
Dependency 'org.glassfish.web:jakarta.servlet.jsp.jstl:2.0.1' not found
 Dependency maven:com.mysql:mysql-connector-j:8.0.33 is vulnerable CVE-2023-22102 8.3 Insufficient Information  Results powered by Mend.io 
 Provides transitive vulnerable dependency maven:com.google.protobuf:protobuf-java:3.21.9 CVE-2024-7254 7.5 Input Validation  Results powered by Mend.io 

```
