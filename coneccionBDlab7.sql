1. Agregar dependencia MySQL en Maven

En tu pom.xml:

<dependencies>
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.3.0</version>
    </dependency>
</dependencies>

⸻

2. Crear la base de datos

Ejecuta en MySQL:

CREATE DATABASE Veterinaria;
USE Veterinaria;

Luego ejecuta todas las tablas e inserts del PDF.  ￼

⸻

3. Crear DaoBase

La idea es que todos los DAO hereden la conexión.

package com.example.lab7.daos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public abstract class DaoBase {
    private final String URL =
            "jdbc:mysql://localhost:3306/Veterinaria";
    private final String USER = "root";
    private final String PASSWORD = "root";
    protected Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
    public abstract void crear(Object obj);
    public abstract void borrar(int id);
}

⸻

4. Registrar el Driver

Puedes hacerlo en el constructor:

public DaoBase() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
}

o dentro de getConnection():

protected Connection getConnection()
        throws SQLException {
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

⸻

5. Probar la conexión

Crea temporalmente este método:

public void probarConexion() {
    try(Connection conn = getConnection()) {
        System.out.println("Conexión exitosa");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

Y desde el servlet:

MascotaDao mascotaDao =
        new MascotaDao();
mascotaDao.probarConexion();

En la consola de IntelliJ debe aparecer:

Conexión exitosa

⸻

6. Verifica tus credenciales

Si usas XAMPP normalmente:

String USER = "root";
String PASSWORD = "";

Si usas MySQL Workbench:

String USER = "root";
String PASSWORD = "root";

o la contraseña que configuraste.

⸻

7. Verifica el puerto

Normalmente:

jdbc:mysql://localhost:3306/Veterinaria

Si tu MySQL corre en otro puerto:

jdbc:mysql://localhost:3307/Veterinaria

Puedes verlo en Workbench:

SHOW VARIABLES LIKE 'port';

⸻

Ejemplo final que usaría en el laboratorio

package com.example.lab7.daos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public abstract class DaoBase {
    private final String URL =
            "jdbc:mysql://localhost:3306/Veterinaria";
    private final String USER = "root";
    private final String PASSWORD = "root";
    protected Connection getConnection()
            throws SQLException {
        try {
            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
    public abstract void crear(Object obj);
    public abstract void borrar(int id);
}

Con eso, cualquier DAO puede conectarse simplemente usando:

try(Connection conn = getConnection()) {
    // consultas SQL
}

