
**Beans**

**1. Especie.java**

package com.lab.beans;

public class Especie {

    private int idEspecie;

    private String nombre;

    public int getIdEspecie() {
        return idEspecie;
    }

    public void setIdEspecie(int idEspecie) {
        this.idEspecie = idEspecie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
```

**2. Veterinario.java**
```
package com.lab.beans;

public class Veterinario {

    private int idVeterinario;

    private String nombre;

    private String especialidad;

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
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
}
```

**3. Dueno.java**
```
package com.lab.beans;

public class Dueno {

    private int idDueno;

    private String nombre;

    private String telefono;

    public int getIdDueno() {
        return idDueno;
    }

    public void setIdDueno(int idDueno) {
        this.idDueno = idDueno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
```

**4. Mascota.java**
```
package com.lab.beans;

import java.math.BigDecimal;

public class Mascota {

    private int idMascota;

    private String nombre;

    private int edad;

    private BigDecimal peso;

    private int especieId;

    private String nombreEspecie;

    private int veterinarioId;

    private String nombreVeterinario;

    private int duenoId;

    private String nombreDueno;

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public int getEspecieId() {
        return especieId;
    }

    public void setEspecieId(int especieId) {
        this.especieId = especieId;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    public int getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(int veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public int getDuenoId() {
        return duenoId;
    }

    public void setDuenoId(int duenoId) {
        this.duenoId = duenoId;
    }

    public String getNombreDueno() {
        return nombreDueno;
    }

    public void setNombreDueno(String nombreDueno) {
        this.nombreDueno = nombreDueno;
    }
}
```

---

**Config**

**1. ConexionDB.java**
```
package com.lab.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/Veterinaria?serverTimezone=America/Lima";

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
```

---

**DAO**

**1. DaoBase.java**
```
package com.lab.dao;

import com.lab.config.ConexionDB;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DaoBase {

    protected Connection getConnection() throws SQLException {
        return ConexionDB.getConnection();
    }

    public abstract void crear(Object entidad);

    public abstract void borrar(int id);
}
```

**2. MascotaDao.java**
```
package com.lab.dao;

import com.lab.beans.Mascota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MascotaDao extends DaoBase {

    public ArrayList<Mascota> listarMascotas() {

        ArrayList<Mascota> lista = new ArrayList<>();

        String sql = """
                SELECT m.idmascota,
                       m.nombre,
                       m.edad,
                       m.peso,
                       m.especie_id,
                       e.nombre    AS nombre_especie,
                       m.veterinario_id,
                       v.nombre    AS nombre_veterinario,
                       m.dueno_id,
                       d.nombre    AS nombre_dueno
                FROM mascota m
                INNER JOIN especie e
                    ON m.especie_id = e.idespecie
                INNER JOIN veterinario v
                    ON m.veterinario_id = v.idveterinario
                INNER JOIN dueno d
                    ON m.dueno_id = d.iddueno
                ORDER BY m.idmascota
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Mascota mascota = new Mascota();

                mascota.setIdMascota(
                        rs.getInt("idmascota"));

                mascota.setNombre(
                        rs.getString("nombre"));

                mascota.setEdad(
                        rs.getInt("edad"));

                mascota.setPeso(
                        rs.getBigDecimal("peso"));

                mascota.setEspecieId(
                        rs.getInt("especie_id"));

                mascota.setNombreEspecie(
                        rs.getString("nombre_especie"));

                mascota.setVeterinarioId(
                        rs.getInt("veterinario_id"));

                mascota.setNombreVeterinario(
                        rs.getString("nombre_veterinario"));

                mascota.setDuenoId(
                        rs.getInt("dueno_id"));

                mascota.setNombreDueno(
                        rs.getString("nombre_dueno"));

                lista.add(mascota);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Mascota> listarMascotasPorEspecie(int especieId) {

        ArrayList<Mascota> lista = new ArrayList<>();

        String sql = """
                SELECT m.idmascota,
                       m.nombre,
                       m.edad,
                       m.peso,
                       m.especie_id,
                       e.nombre    AS nombre_especie,
                       m.veterinario_id,
                       v.nombre    AS nombre_veterinario,
                       m.dueno_id,
                       d.nombre    AS nombre_dueno
                FROM mascota m
                INNER JOIN especie e
                    ON m.especie_id = e.idespecie
                INNER JOIN veterinario v
                    ON m.veterinario_id = v.idveterinario
                INNER JOIN dueno d
                    ON m.dueno_id = d.iddueno
                WHERE m.especie_id = ?
                ORDER BY m.idmascota
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, especieId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Mascota mascota = new Mascota();

                mascota.setIdMascota(
                        rs.getInt("idmascota"));

                mascota.setNombre(
                        rs.getString("nombre"));

                mascota.setEdad(
                        rs.getInt("edad"));

                mascota.setPeso(
                        rs.getBigDecimal("peso"));

                mascota.setEspecieId(
                        rs.getInt("especie_id"));

                mascota.setNombreEspecie(
                        rs.getString("nombre_especie"));

                mascota.setVeterinarioId(
                        rs.getInt("veterinario_id"));

                mascota.setNombreVeterinario(
                        rs.getString("nombre_veterinario"));

                mascota.setDuenoId(
                        rs.getInt("dueno_id"));

                mascota.setNombreDueno(
                        rs.getString("nombre_dueno"));

                lista.add(mascota);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void crear(Object entidad) {

        Mascota mascota = (Mascota) entidad;

        String sql = """
                INSERT INTO mascota
                (nombre, edad, peso, especie_id, veterinario_id, dueno_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,
                    mascota.getNombre());

            ps.setInt(2,
                    mascota.getEdad());

            ps.setBigDecimal(3,
                    mascota.getPeso());

            ps.setInt(4,
                    mascota.getEspecieId());

            ps.setInt(5,
                    mascota.getVeterinarioId());

            ps.setInt(6,
                    mascota.getDuenoId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void borrar(int id) {

        String sql = """
                DELETE FROM mascota
                WHERE idmascota = ?
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

**3. EspecieDao.java**
```
package com.lab.dao;

import com.lab.beans.Especie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EspecieDao extends DaoBase {

    public ArrayList<Especie> listarEspecies() {

        ArrayList<Especie> lista = new ArrayList<>();

        String sql = """
                SELECT *
                FROM especie
                ORDER BY idespecie
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Especie especie = new Especie();

                especie.setIdEspecie(
                        rs.getInt("idespecie"));

                especie.setNombre(
                        rs.getString("nombre"));

                lista.add(especie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void crear(Object entidad) {}

    @Override
    public void borrar(int id) {}
}
```

**4. VeterinarioDao.java**
```
package com.lab.dao;

import com.lab.beans.Veterinario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class VeterinarioDao extends DaoBase {

    public ArrayList<Veterinario> listarVeterinarios() {

        ArrayList<Veterinario> lista = new ArrayList<>();

        String sql = """
                SELECT *
                FROM veterinario
                ORDER BY idveterinario
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Veterinario veterinario = new Veterinario();

                veterinario.setIdVeterinario(
                        rs.getInt("idveterinario"));

                veterinario.setNombre(
                        rs.getString("nombre"));

                veterinario.setEspecialidad(
                        rs.getString("especialidad"));

                lista.add(veterinario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void crear(Object entidad) {}

    @Override
    public void borrar(int id) {}
}
```

**5. DuenoDao.java**
```
package com.lab.dao;

import com.lab.beans.Dueno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DuenoDao extends DaoBase {

    public ArrayList<Dueno> listarDuenos() {

        ArrayList<Dueno> lista = new ArrayList<>();

        String sql = """
                SELECT *
                FROM dueno
                ORDER BY iddueno
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Dueno dueno = new Dueno();

                dueno.setIdDueno(
                        rs.getInt("iddueno"));

                dueno.setNombre(
                        rs.getString("nombre"));

                dueno.setTelefono(
                        rs.getString("telefono"));

                lista.add(dueno);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void crear(Object entidad) {}

    @Override
    public void borrar(int id) {}
}
```

---

**Servlet**

**1. MascotaServlet.java**
```
package com.lab.servlets;

import com.lab.beans.Mascota;
import com.lab.dao.DuenoDao;
import com.lab.dao.EspecieDao;
import com.lab.dao.MascotaDao;
import com.lab.dao.VeterinarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/inicio")
public class MascotaServlet extends HttpServlet {

    private final MascotaDao mascotaDao =
            new MascotaDao();

    private final EspecieDao especieDao =
            new EspecieDao();

    private final VeterinarioDao veterinarioDao =
            new VeterinarioDao();

    private final DuenoDao duenoDao =
            new DuenoDao();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action =
                request.getParameter("action");

        if (action == null) {

            String filtroEspecie =
                    request.getParameter("especieId");

            if (filtroEspecie != null
                    && !filtroEspecie.isEmpty()
                    && !filtroEspecie.equals("0")) {

                int especieId =
                        Integer.parseInt(filtroEspecie);

                request.setAttribute(
                        "listaMascotas",
                        mascotaDao.listarMascotasPorEspecie(
                                especieId));

                request.setAttribute(
                        "especieSeleccionada",
                        especieId);

            } else {

                request.setAttribute(
                        "listaMascotas",
                        mascotaDao.listarMascotas());

                request.setAttribute(
                        "especieSeleccionada",
                        0);
            }

            request.setAttribute(
                    "listaEspecies",
                    especieDao.listarEspecies());

            request.getRequestDispatcher(
                            "/inicio.jsp")
                    .forward(request, response);

            return;
        }

        switch (action) {

            case "nuevo":

                request.setAttribute(
                        "listaEspecies",
                        especieDao.listarEspecies());

                request.setAttribute(
                        "listaVeterinarios",
                        veterinarioDao.listarVeterinarios());

                request.setAttribute(
                        "listaDuenos",
                        duenoDao.listarDuenos());

                request.getRequestDispatcher(
                                "/mascotaForm.jsp")
                        .forward(request, response);

                break;

            case "borrar":

                int idBorrar =
                        Integer.parseInt(
                                request.getParameter("id"));

                mascotaDao.borrar(idBorrar);

                response.sendRedirect(
                        request.getContextPath()
                                + "/inicio");

                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Mascota mascota = new Mascota();

        mascota.setNombre(
                request.getParameter("nombre"));

        mascota.setEdad(
                Integer.parseInt(
                        request.getParameter("edad")));

        mascota.setPeso(
                new BigDecimal(
                        request.getParameter("peso")));

        mascota.setEspecieId(
                Integer.parseInt(
                        request.getParameter("especieId")));

        mascota.setVeterinarioId(
                Integer.parseInt(
                        request.getParameter("veterinarioId")));

        mascota.setDuenoId(
                Integer.parseInt(
                        request.getParameter("duenoId")));

        mascotaDao.crear(mascota);

        response.sendRedirect(
                request.getContextPath()
                        + "/inicio");
    }
}
```

---

**CSS**

**1. estilos.css**
```
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
    display:flex;
    align-items:center;
    gap:15px;
}

.filtro-especie{
    display:flex;
    align-items:center;
    gap:8px;
    margin-bottom:15px;
}

.filtro-especie label{
    font-weight:bold;
    margin:0;
}

.filtro-especie select{
    padding:6px 10px;
    border:1px solid #ccc;
    border-radius:6px;
    font-size:14px;
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
    width:400px;
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

input, select{
    padding:10px;
    border:1px solid #ccc;
    border-radius:6px;
    font-size:14px;
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

.btn-borrar{
    background:#dc2626;
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
    font-size:14px;
}

button:hover{
    opacity:0.9;
}
```

---

**WEB-INF**

**1. web.xml**
```
<?xml version="1.0" encoding="UTF-8"?>

<web-app
        xmlns="https://jakarta.ee/xml/ns/jakartaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="
        https://jakarta.ee/xml/ns/jakartaee
        https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
        version="6.0">

    <display-name>LAB7_IWEB_2026-1</display-name>

    <welcome-file-list>
        <welcome-file>inicio</welcome-file>
    </welcome-file-list>

</web-app>
```

---

**POM**

**pom.xml**
```
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lab</groupId>
    <artifactId>LAB7_IWEB_2026-1</artifactId>
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
```

---

**JSP**

**1. inicio.jsp**
```
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Veterinaria - Mascotas</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="contenedor">

    <h1>Lista de Mascotas</h1>

    <div class="barra-superior">

        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/inicio?action=nuevo">
            Nueva Mascota
        </a>

    </div>

    <div class="filtro-especie">

        <label>Filtrar por especie:</label>

        <form method="get"
              action="${pageContext.request.contextPath}/inicio">

            <select name="especieId"
                    onchange="this.form.submit()">

                <option value="0">Todas las especies</option>

                <c:forEach var="e" items="${listaEspecies}">

                    <option value="${e.idEspecie}"
                        ${especieSeleccionada == e.idEspecie ? 'selected' : ''}>
                        ${e.nombre}
                    </option>

                </c:forEach>

            </select>

        </form>

    </div>

    <table>

        <thead>

        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Edad</th>
            <th>Peso</th>
            <th>Especie</th>
            <th>Veterinario</th>
            <th>Dueño</th>
            <th>Acción</th>
        </tr>

        </thead>

        <tbody>

        <c:forEach var="m" items="${listaMascotas}">

            <tr>

                <td>${m.idMascota}</td>

                <td>${m.nombre}</td>

                <td>${m.edad}</td>

                <td>${m.peso}</td>

                <td>${m.nombreEspecie}</td>

                <td>${m.nombreVeterinario}</td>

                <td>${m.nombreDueno}</td>

                <td>

                    <a class="btn btn-borrar"
                       href="${pageContext.request.contextPath}/inicio?action=borrar&id=${m.idMascota}"
                       onclick="return confirm('¿Está seguro de eliminar esta mascota?');">
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

**2. mascotaForm.jsp**
```
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Nueva Mascota</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="contenedor-form">

    <div class="card">

        <h2>Nueva Mascota</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/inicio">

            <label>Nombre:</label>

            <input type="text"
                   name="nombre"
                   required>

            <label>Edad:</label>

            <input type="number"
                   name="edad"
                   min="0"
                   required>

            <label>Peso (kg):</label>

            <input type="number"
                   name="peso"
                   step="0.01"
                   min="0"
                   required>

            <label>Especie:</label>

            <select name="especieId" required>

                <option value="">Seleccione una especie</option>

                <c:forEach var="e" items="${listaEspecies}">

                    <option value="${e.idEspecie}">
                        ${e.nombre}
                    </option>

                </c:forEach>

            </select>

            <label>Veterinario:</label>

            <select name="veterinarioId" required>

                <option value="">Seleccione un veterinario</option>

                <c:forEach var="v" items="${listaVeterinarios}">

                    <option value="${v.idVeterinario}">
                        ${v.nombre}
                    </option>

                </c:forEach>

            </select>

            <label>Dueño:</label>

            <select name="duenoId" required>

                <option value="">Seleccione un dueño</option>

                <c:forEach var="d" items="${listaDuenos}">

                    <option value="${d.idDueno}">
                        ${d.nombre}
                    </option>

                </c:forEach>

            </select>

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
