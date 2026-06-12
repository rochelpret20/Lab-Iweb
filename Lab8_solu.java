Lab9_Productos
│
├── pom.xml
│
├── src
│   └── main
│
│       ├── java
│       │
│       │   └── com
│       │       └── lab
│       │
│       │           ├── beans
│       │           │
│       │           │   ├── Usuario.java
│       │           │   ├── Categoria.java
│       │           │   ├── Producto.java
│       │           │   └── Carrito.java
│       │           │
│       │           ├── config
│       │           │
│       │           │   └── ConexionDB.java
│       │           │
│       │           ├── dao
│       │           │
│       │           │   ├── DaoUsuario.java
│       │           │   ├── DaoCategoria.java
│       │           │   ├── DaoProducto.java
│       │           │   └── DaoCarrito.java
│       │           │
│       │           └── servlets
│       │
│       │               ├── LoginServlet.java
│       │               ├── LogoutServlet.java
│       │               ├── ProductoServlet.java
│       │               └── CarritoServlet.java
│       │
│       ├── resources
│       │
│       └── webapp
│
│           ├── css
│           │
│           │   └── estilos.css
│           │
│           ├── includes
│           │
│           │   └── navbar.jspf
│           │
│           ├── login.jsp
│           ├── productos.jsp
│           ├── productoForm.jsp
│           ├── carrito.jsp
│           │
│           └── WEB-INF
│
│               └── web.xml
│
└── target

1. Beans
  1.1)Carrito.java
package com.lab.beans;

import java.math.BigDecimal;

public class Carrito {

    private int idItem;

    private int idUsuario;

    private int idProducto;

    private String nombreProducto;

    private String nombreUsuario;

    private BigDecimal precioUnit;

    private int cantidad;

    private BigDecimal subtotal;

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public BigDecimal getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(BigDecimal precioUnit) {
        this.precioUnit = precioUnit;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}

1.2) Categoria.java
package com.lab.beans;

public class Categoria {

    private int idCategoria;

    private String nombre;

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

1.3)Producto.java
package com.lab.beans;

import java.math.BigDecimal;

public class Producto {

    private int idProducto;

    private int idCategoria;

    private String categoriaNombre;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private int stock;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

1.4) Usuario.java
package com.lab.beans;

public class Usuario {

    private int idUsuario;

    private String nombre;

    private String correo;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}


2. config
package com.lab.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/lab9_productos?serverTimezone=America/Lima";

    private static final String USER =
            "root";

    private static final String PASSWORD =
            "Rosell2025";

    public static Connection getConnection()
            throws SQLException {

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);
        }

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD);
    }
}

3. DAO
3.1 DaoCarrito
package com.lab.dao;

import com.lab.beans.Carrito;
import com.lab.config.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoCarrito {

    public void agregarProducto(
            int idUsuario,
            int idProducto) {

        String sqlBuscar = """
                SELECT *
                FROM carrito_item
                WHERE id_usuario = ?
                AND id_producto = ?
                """;

        String sqlActualizar = """
                UPDATE carrito_item
                SET cantidad = cantidad + 1
                WHERE id_usuario = ?
                AND id_producto = ?
                """;

        String sqlInsertar = """
                INSERT INTO carrito_item
                (
                    id_usuario,
                    id_producto,
                    cantidad
                )
                VALUES
                (
                    ?, ?, 1
                )
                """;

        try(Connection con =
                    ConexionDB.getConnection()) {

            PreparedStatement psBuscar =
                    con.prepareStatement(
                            sqlBuscar);

            psBuscar.setInt(
                    1,
                    idUsuario);

            psBuscar.setInt(
                    2,
                    idProducto);

            ResultSet rs =
                    psBuscar.executeQuery();

            if(rs.next()) {

                PreparedStatement psUpdate =
                        con.prepareStatement(
                                sqlActualizar);

                psUpdate.setInt(
                        1,
                        idUsuario);

                psUpdate.setInt(
                        2,
                        idProducto);

                psUpdate.executeUpdate();

            } else {

                PreparedStatement psInsert =
                        con.prepareStatement(
                                sqlInsertar);

                psInsert.setInt(
                        1,
                        idUsuario);

                psInsert.setInt(
                        2,
                        idProducto);

                psInsert.executeUpdate();
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public ArrayList<Carrito>
    listarCarrito(int idUsuario) {

        ArrayList<Carrito> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                ci.id_item,
                ci.id_usuario,
                p.id_producto,
                p.nombre,
                p.precio,
                ci.cantidad,

                (
                    p.precio *
                    ci.cantidad
                ) subtotal,

                CONCAT(
                    u.nombres,
                    ' ',
                    u.apellidos
                ) usuario

                FROM carrito_item ci

                INNER JOIN producto p
                ON ci.id_producto =
                   p.id_producto

                INNER JOIN usuario u
                ON ci.id_usuario =
                   u.id_usuario

                WHERE ci.id_usuario = ?

                ORDER BY ci.id_item
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                Carrito carrito =
                        new Carrito();

                carrito.setIdItem(
                        rs.getInt(
                                "id_item"));

                carrito.setIdUsuario(
                        rs.getInt(
                                "id_usuario"));

                carrito.setIdProducto(
                        rs.getInt(
                                "id_producto"));

                carrito.setNombreProducto(
                        rs.getString(
                                "nombre"));

                carrito.setPrecioUnit(
                        rs.getBigDecimal(
                                "precio"));

                carrito.setCantidad(
                        rs.getInt(
                                "cantidad"));

                carrito.setSubtotal(
                        rs.getBigDecimal(
                                "subtotal"));

                carrito.setNombreUsuario(
                        rs.getString(
                                "usuario"));

                lista.add(carrito);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public BigDecimal obtenerTotal(
            int idUsuario) {

        BigDecimal total =
                BigDecimal.ZERO;

        String sql = """
                SELECT
                SUM(
                    p.precio *
                    ci.cantidad
                ) total
                FROM carrito_item ci

                INNER JOIN producto p
                ON ci.id_producto =
                   p.id_producto

                WHERE ci.id_usuario = ?
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                total =
                        rs.getBigDecimal(
                                "total");

                if(total == null){

                    total =
                            BigDecimal.ZERO;
                }
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return total;
    }
}

2.2) DaoCategoria
package com.lab.dao;

import com.lab.beans.Carrito;
import com.lab.config.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoCarrito {

    public void agregarProducto(
            int idUsuario,
            int idProducto) {

        String sqlBuscar = """
                SELECT *
                FROM carrito_item
                WHERE id_usuario = ?
                AND id_producto = ?
                """;

        String sqlActualizar = """
                UPDATE carrito_item
                SET cantidad = cantidad + 1
                WHERE id_usuario = ?
                AND id_producto = ?
                """;

        String sqlInsertar = """
                INSERT INTO carrito_item
                (
                    id_usuario,
                    id_producto,
                    cantidad
                )
                VALUES
                (
                    ?, ?, 1
                )
                """;

        try(Connection con =
                    ConexionDB.getConnection()) {

            PreparedStatement psBuscar =
                    con.prepareStatement(
                            sqlBuscar);

            psBuscar.setInt(
                    1,
                    idUsuario);

            psBuscar.setInt(
                    2,
                    idProducto);

            ResultSet rs =
                    psBuscar.executeQuery();

            if(rs.next()) {

                PreparedStatement psUpdate =
                        con.prepareStatement(
                                sqlActualizar);

                psUpdate.setInt(
                        1,
                        idUsuario);

                psUpdate.setInt(
                        2,
                        idProducto);

                psUpdate.executeUpdate();

            } else {

                PreparedStatement psInsert =
                        con.prepareStatement(
                                sqlInsertar);

                psInsert.setInt(
                        1,
                        idUsuario);

                psInsert.setInt(
                        2,
                        idProducto);

                psInsert.executeUpdate();
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public ArrayList<Carrito>
    listarCarrito(int idUsuario) {

        ArrayList<Carrito> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                ci.id_item,
                ci.id_usuario,
                p.id_producto,
                p.nombre,
                p.precio,
                ci.cantidad,

                (
                    p.precio *
                    ci.cantidad
                ) subtotal,

                CONCAT(
                    u.nombres,
                    ' ',
                    u.apellidos
                ) usuario

                FROM carrito_item ci

                INNER JOIN producto p
                ON ci.id_producto =
                   p.id_producto

                INNER JOIN usuario u
                ON ci.id_usuario =
                   u.id_usuario

                WHERE ci.id_usuario = ?

                ORDER BY ci.id_item
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                Carrito carrito =
                        new Carrito();

                carrito.setIdItem(
                        rs.getInt(
                                "id_item"));

                carrito.setIdUsuario(
                        rs.getInt(
                                "id_usuario"));

                carrito.setIdProducto(
                        rs.getInt(
                                "id_producto"));

                carrito.setNombreProducto(
                        rs.getString(
                                "nombre"));

                carrito.setPrecioUnit(
                        rs.getBigDecimal(
                                "precio"));

                carrito.setCantidad(
                        rs.getInt(
                                "cantidad"));

                carrito.setSubtotal(
                        rs.getBigDecimal(
                                "subtotal"));

                carrito.setNombreUsuario(
                        rs.getString(
                                "usuario"));

                lista.add(carrito);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public BigDecimal obtenerTotal(
            int idUsuario) {

        BigDecimal total =
                BigDecimal.ZERO;

        String sql = """
                SELECT
                SUM(
                    p.precio *
                    ci.cantidad
                ) total
                FROM carrito_item ci

                INNER JOIN producto p
                ON ci.id_producto =
                   p.id_producto

                WHERE ci.id_usuario = ?
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idUsuario);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                total =
                        rs.getBigDecimal(
                                "total");

                if(total == null){

                    total =
                            BigDecimal.ZERO;
                }
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return total;
    }
}

2.3)DaoProducto
package com.lab.dao;

import com.lab.beans.Producto;
import com.lab.config.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoProducto {

    public ArrayList<Producto> listarProductos() {

        ArrayList<Producto> lista =
                new ArrayList<>();

        String sql = """
                SELECT
                p.id_producto,
                p.id_categoria,
                p.nombre,
                p.descripcion,
                p.precio,

                (
                    p.stock -
                    IFNULL(
                        (
                            SELECT SUM(ci.cantidad)
                            FROM carrito_item ci
                            WHERE ci.id_producto =
                                  p.id_producto
                        ),
                        0
                    )
                ) stock_disponible,

                c.nombre categoria

                FROM producto p

                INNER JOIN categoria c
                ON p.id_categoria =
                   c.id_categoria

                ORDER BY p.id_producto
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery()) {

            while(rs.next()) {

                Producto p =
                        new Producto();

                p.setIdProducto(
                        rs.getInt(
                                "id_producto"));

                p.setIdCategoria(
                        rs.getInt(
                                "id_categoria"));

                p.setNombre(
                        rs.getString(
                                "nombre"));

                p.setDescripcion(
                        rs.getString(
                                "descripcion"));

                p.setPrecio(
                        rs.getBigDecimal(
                                "precio"));

                p.setStock(
                        rs.getInt(
                                "stock_disponible"));

                p.setCategoriaNombre(
                        rs.getString(
                                "categoria"));

                lista.add(p);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public Producto obtenerProducto(
            int idProducto){

        Producto producto =
                null;

        String sql = """
                SELECT *
                FROM producto
                WHERE id_producto = ?
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()){

                producto =
                        new Producto();

                producto.setIdProducto(
                        rs.getInt(
                                "id_producto"));

                producto.setIdCategoria(
                        rs.getInt(
                                "id_categoria"));

                producto.setNombre(
                        rs.getString(
                                "nombre"));

                producto.setDescripcion(
                        rs.getString(
                                "descripcion"));

                producto.setPrecio(
                        rs.getBigDecimal(
                                "precio"));

                producto.setStock(
                        rs.getInt(
                                "stock"));
            }

        }catch(Exception e){

            e.printStackTrace();
        }

        return producto;
    }

    public void crearProducto(
            Producto producto){

        String sql = """
                INSERT INTO producto
                (
                    id_categoria,
                    nombre,
                    descripcion,
                    precio,
                    stock
                )
                VALUES
                (
                    ?,?,?,?,?
                )
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    producto.getIdCategoria());

            ps.setString(
                    2,
                    producto.getNombre());

            ps.setString(
                    3,
                    producto.getDescripcion());

            ps.setBigDecimal(
                    4,
                    producto.getPrecio());

            ps.setInt(
                    5,
                    producto.getStock());

            ps.executeUpdate();

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    public void actualizarProducto(
            Producto producto){

        String sql = """
                UPDATE producto
                SET
                id_categoria=?,
                nombre=?,
                descripcion=?,
                precio=?,
                stock=?
                WHERE id_producto=?
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    producto.getIdCategoria());

            ps.setString(
                    2,
                    producto.getNombre());

            ps.setString(
                    3,
                    producto.getDescripcion());

            ps.setBigDecimal(
                    4,
                    producto.getPrecio());

            ps.setInt(
                    5,
                    producto.getStock());

            ps.setInt(
                    6,
                    producto.getIdProducto());

            ps.executeUpdate();

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    public void eliminarProducto(
            int idProducto){

        String sql = """
                DELETE FROM producto
                WHERE id_producto = ?
                """;

        try(Connection con =
                    ConexionDB.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProducto);

            ps.executeUpdate();

        }catch(Exception e){

            e.printStackTrace();
        }
    }
}

2.4)DaoUsuario
package com.lab.dao;

import com.lab.beans.Usuario;
import com.lab.config.ConexionDB;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DaoUsuario {

    public Usuario validarUsuario(
            String email,
            String password) {

        Usuario usuario = null;

        String sql = """
                SELECT *
                FROM usuario
                WHERE email = ?
                AND estado = 'ACTIVO'
                """;

        try (Connection con =
                     ConexionDB.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                String hashBD =
                        rs.getString(
                                "password_hash");

                String hashIngresado =
                        generarSHA256(
                                password);

                if (hashBD.equals(
                        hashIngresado)) {

                    usuario =
                            new Usuario();

                    usuario.setIdUsuario(
                            rs.getInt(
                                    "id_usuario"));

                    usuario.setNombre(
                            rs.getString(
                                    "nombres")
                                    + " "
                                    +
                                    rs.getString(
                                            "apellidos"));

                    usuario.setCorreo(
                            rs.getString(
                                    "email"));
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return usuario;
    }

    private String generarSHA256(
            String texto) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance(
                            "SHA-256");

            byte[] hash =
                    md.digest(
                            texto.getBytes());

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hash) {

                sb.append(
                        String.format(
                                "%02x",
                                b));
            }

            return sb.toString();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}

4.SERVLETS
4.1) CarritoServlet
package com.lab.servlets;

import com.lab.beans.Usuario;
import com.lab.dao.DaoCarrito;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/carrito")
public class CarritoServlet
        extends HttpServlet {

    private final DaoCarrito daoCarrito =
            new DaoCarrito();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
            IOException {

        HttpSession session =
                request.getSession(false);

        if(session == null ||
                session.getAttribute("usuario")
                        == null){

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        Usuario usuario =
                (Usuario)
                        session.getAttribute(
                                "usuario");

        String action =
                request.getParameter(
                        "action");

        if(action != null &&
                action.equals(
                        "agregar")) {

            int idProducto =
                    Integer.parseInt(
                            request.getParameter(
                                    "id"));

            daoCarrito.agregarProducto(
                    usuario.getIdUsuario(),
                    idProducto);

            response.sendRedirect(
                    request.getContextPath()
                            + "/productos");

            return;
        }

        request.setAttribute(
                "listaCarrito",
                daoCarrito.listarCarrito(
                        usuario.getIdUsuario()));

        request.setAttribute(
                "total",
                daoCarrito.obtenerTotal(
                        usuario.getIdUsuario()));

        request.getRequestDispatcher(
                        "/carrito.jsp")
                .forward(
                        request,
                        response);
    }
}

4.2)LoginServlet
package com.lab.servlets;

import com.lab.beans.Usuario;
import com.lab.dao.DaoUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final DaoUsuario daoUsuario =
            new DaoUsuario();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                        "/login.jsp")
                .forward(
                        request,
                        response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String email =
                request.getParameter(
                        "email");

        String password =
                request.getParameter(
                        "password");

        Usuario usuario =
                daoUsuario.validarUsuario(
                        email,
                        password);

        if (usuario != null) {

            HttpSession session =
                    request.getSession();

            session.setAttribute(
                    "usuario",
                    usuario);

            response.sendRedirect(
                    request.getContextPath()
                            + "/productos");

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?error=1");
        }
    }
}

4.3) LogoutServlet
package com.lab.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session =
                request.getSession(false);

        if (session != null) {

            session.invalidate();
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/login");
    }
}

4.4) ProductoServlet
package com.lab.servlets;

import com.lab.beans.Producto;
import com.lab.dao.DaoCategoria;
import com.lab.dao.DaoProducto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/productos")
public class ProductoServlet
        extends HttpServlet {

    private final DaoProducto daoProducto =
            new DaoProducto();

    private final DaoCategoria daoCategoria =
            new DaoCategoria();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
            IOException {

        HttpSession session =
                request.getSession(false);

        if(session == null ||
                session.getAttribute("usuario")
                        == null){

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        String action =
                request.getParameter("action");

        if(action == null){

            request.setAttribute(
                    "listaProductos",
                    daoProducto.listarProductos());

            request.getRequestDispatcher(
                            "/productos.jsp")
                    .forward(
                            request,
                            response);

            return;
        }

        switch(action){

            case "nuevo":

                request.setAttribute(
                        "listaCategorias",
                        daoCategoria.listarCategorias());

                request.getRequestDispatcher(
                                "/productoForm.jsp")
                        .forward(
                                request,
                                response);

                break;

            case "editar":

                int idEditar =
                        Integer.parseInt(
                                request.getParameter(
                                        "id"));

                request.setAttribute(
                        "producto",
                        daoProducto.obtenerProducto(
                                idEditar));

                request.setAttribute(
                        "listaCategorias",
                        daoCategoria.listarCategorias());

                request.getRequestDispatcher(
                                "/productoForm.jsp")
                        .forward(
                                request,
                                response);

                break;

            case "eliminar":

                int idEliminar =
                        Integer.parseInt(
                                request.getParameter(
                                        "id"));

                daoProducto.eliminarProducto(
                        idEliminar);

                response.sendRedirect(
                        request.getContextPath()
                                + "/productos");

                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session =
                request.getSession(false);

        if(session == null ||
                session.getAttribute("usuario")
                        == null){

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        String idProducto =
                request.getParameter(
                        "idProducto");

        Producto producto =
                new Producto();

        producto.setIdCategoria(
                Integer.parseInt(
                        request.getParameter(
                                "idCategoria")));

        producto.setNombre(
                request.getParameter(
                        "nombre"));

        producto.setDescripcion(
                request.getParameter(
                        "descripcion"));

        producto.setPrecio(
                new BigDecimal(
                        request.getParameter(
                                "precio")));

        producto.setStock(
                Integer.parseInt(
                        request.getParameter(
                                "stock")));

        if(idProducto == null ||
                idProducto.isEmpty()){

            daoProducto.crearProducto(
                    producto);

        }else{

            producto.setIdProducto(
                    Integer.parseInt(
                            idProducto));

            daoProducto.actualizarProducto(
                    producto);
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/productos");
    }
}

5.) estilos.css
body{
    font-family: Arial,sans-serif;
    background:#eef2f7;
    margin:0;
    padding:0;
}

.contenedor{
    width:90%;
    margin:auto;
    padding:20px;
}

.contenedor-form{
    display:flex;
    justify-content:center;
    align-items:center;
    min-height:100vh;
}

.card{
    background:white;
    padding:25px;
    border-radius:12px;
    box-shadow:0 4px 15px rgba(0,0,0,.08);
    min-width:400px;
}

h1{
    color:#0f172a;
}

h2{
    color:#1e293b;
}

form{
    display:flex;
    flex-direction:column;
}

label{
    margin-top:10px;
    margin-bottom:5px;
    font-weight:bold;
    color:#334155;
}

input,select{
    padding:10px;
    border:1px solid #cbd5e1;
    border-radius:6px;
}

input:focus,
select:focus{
    outline:none;
    border-color:#06b6d4;
    box-shadow:0 0 5px rgba(6,182,212,.4);
}

button{
    background:#0891b2;
    color:white;
    border:none;
    padding:10px;
    border-radius:6px;
    margin-top:20px;
    cursor:pointer;
    transition:.3s;
}

button:hover{
    background:#0e7490;
}

.navbar{
    background:#0f172a;
    color:white;
    padding:15px;
    display:flex;
    justify-content:space-between;
    align-items:center;
}

.navbar a{
    color:white;
    text-decoration:none;
    margin-left:15px;
    transition:.3s;
}

.navbar a:hover{
    color:#22d3ee;
}

table{
    width:100%;
    border-collapse:collapse;
    background:white;
    margin-top:20px;
    box-shadow:0 4px 15px rgba(0,0,0,.08);
    border-radius:10px;
    overflow:hidden;
}

th{
    background:#0f172a;
    color:white;
    padding:12px;
}

td{
    padding:12px;
    border-bottom:1px solid #e2e8f0;
}

tr:hover{
    background:#f1f5f9;
}

.btn{
    text-decoration:none;
    color:white;
    padding:8px 14px;
    border-radius:6px;
    display:inline-block;
    transition:.3s;
}

.btn-nuevo{
    background:#14b8a6;
}

.btn-nuevo:hover{
    background:#0f766e;
}

.btn-editar{
    background:#0284c7;
}

.btn-editar:hover{
    background:#0369a1;
}

.btn-eliminar{
    background:#dc2626;
}

.btn-eliminar:hover{
    background:#b91c1c;
}

.btn-detalle{
    background:#f59e0b;
}

.btn-detalle:hover{
    background:#d97706;
}

.btn-regresar{
    background:#64748b;
}

.btn-regresar:hover{
    background:#475569;
}

.barra-superior{
    margin-top:20px;
    margin-bottom:20px;
}

.botones-form{
    margin-top:20px;
    display:flex;
    gap:10px;
}

6)navbar.jspf
<nav class="navbar">

    <div>

        Sistema Productos

    </div>

    <div>

        Bienvenido:
        ${sessionScope.usuario.nombre}

    </div>

    <div>

        <a href="${pageContext.request.contextPath}/productos">

            Productos

        </a>

        <a href="${pageContext.request.contextPath}/carrito">

            Carrito

        </a>

        <a href="${pageContext.request.contextPath}/logout">

            Cerrar Sesión

        </a>

    </div>

</nav>

7)web.xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app
        xmlns="https://jakarta.ee/xml/ns/jakartaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="
        https://jakarta.ee/xml/ns/jakartaee
        https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
        version="6.0">

    <display-name>
        Lab8
    </display-name>

    <welcome-file-list>

        <welcome-file>
            login
        </welcome-file>

    </welcome-file-list>

</web-app>

8)carrito.jsp
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Carrito</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Carrito de Compras</h1>

    <table>

        <thead>

        <tr>

            <th>ID Item</th>

            <th>Producto</th>

            <th>Usuario</th>

            <th>Precio Unitario</th>

            <th>Cantidad</th>

            <th>Subtotal</th>

        </tr>

        </thead>

        <tbody>

        <c:forEach var="c"
                   items="${listaCarrito}">

            <tr>

                <td>${c.idItem}</td>

                <td>${c.nombreProducto}</td>

                <td>${c.nombreUsuario}</td>

                <td>S/ ${c.precioUnit}</td>

                <td>${c.cantidad}</td>

                <td>S/ ${c.subtotal}</td>

            </tr>

        </c:forEach>

        </tbody>

    </table>

    <br>

    <div class="card">

        <h2>

            Total General:
            S/ ${total}

        </h2>

    </div>

</div>

</body>
</html>

8)login.jsp
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Login</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="contenedor-form">

    <div class="card">

        <h2>Iniciar Sesión</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/login">

            <label>Correo</label>

            <input type="email"
                   name="email"
                   required>

            <label>Contraseña</label>

            <input type="password"
                   name="password"
                   required>

            <button type="submit">

                Ingresar

            </button>

        </form>

        <%
            String error =
                    request.getParameter(
                            "error");

            if(error != null){
        %>

        <p style="color:red">

            Usuario o contraseña incorrectos

        </p>

        <%
            }
        %>

    </div>

</div>

</body>
</html>

9) productoForm.jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Producto</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor-form">

    <div class="card">

        <h2>

            ${empty producto ?
                    "Registrar Producto"
                    :
                    "Editar Producto"}

        </h2>

        <form method="post"
              action="${pageContext.request.contextPath}/productos">

            <input type="hidden"
                   name="idProducto"
                   value="${producto.idProducto}">

            <label>Categoría</label>

            <select name="idCategoria">

                <c:forEach var="c"
                           items="${listaCategorias}">

                    <option value="${c.idCategoria}"

                        ${producto.idCategoria ==
                                c.idCategoria ?
                                'selected' : ''}>

                            ${c.nombre}

                    </option>

                </c:forEach>

            </select>

            <label>Nombre</label>

            <input type="text"
                   name="nombre"
                   value="${producto.nombre}"
                   required>

            <label>Descripción</label>

            <input type="text"
                   name="descripcion"
                   value="${producto.descripcion}"
                   required>

            <label>Precio</label>

            <input type="number"
                   step="0.01"
                   name="precio"
                   value="${producto.precio}"
                   required>

            <label>Stock</label>

            <input type="number"
                   name="stock"
                   value="${producto.stock}"
                   required>

            <div class="botones-form">

                <button type="submit">

                    Guardar

                </button>

                <a class="btn btn-regresar"
                   href="${pageContext.request.contextPath}/productos">

                    Cancelar

                </a>

            </div>

        </form>

    </div>

</div>

</body>
</html>

10)productos.jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Productos</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Listado de Productos</h1>

    <div class="barra-superior">

        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/productos?action=nuevo">

            Nuevo Producto

        </a>

    </div>

    <table>

        <thead>

        <tr>

            <th>ID</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Acciones</th>

        </tr>

        </thead>

        <tbody>

        <c:forEach var="p"
                   items="${listaProductos}">

            <tr>

                <td>${p.idProducto}</td>
                <td>${p.nombre}</td>
                <td>${p.categoriaNombre}</td>
                <td>S/ ${p.precio}</td>
                <td>${p.stock}</td>

                <td>

                    <a class="btn btn-editar"
                       href="${pageContext.request.contextPath}/productos?action=editar&id=${p.idProducto}">

                        Editar

                    </a>

                    <a class="btn btn-eliminar"
                       href="${pageContext.request.contextPath}/productos?action=eliminar&id=${p.idProducto}"
                       onclick="return confirm('¿Eliminar producto?')">

                        Eliminar

                    </a>

                    <a class="btn btn-detalle"
                       href="${pageContext.request.contextPath}/carrito?action=agregar&id=${p.idProducto}">

                        Añadir Carrito

                    </a>

                </td>

            </tr>

        </c:forEach>

        </tbody>

    </table>

</div>

</body>
</html>

11)pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lab</groupId>
    <artifactId>Lab8</artifactId>
    <version>1.0-SNAPSHOT</version>

    <packaging>war</packaging>

    <properties>

        <maven.compiler.source>17</maven.compiler.source>

        <maven.compiler.target>17</maven.compiler.target>

        <project.build.sourceEncoding>
            UTF-8
        </project.build.sourceEncoding>

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




