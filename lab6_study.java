

Base de Datos

Script SQL

CREATE DATABASE Biblioteca;
USE Biblioteca;
CREATE TABLE libro(
    idlibro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    anio_publicacion INT NOT NULL
);

⸻

Datos iniciales

INSERT INTO libro
(titulo,autor,editorial,anio_publicacion)
VALUES
('Clean Code',
 'Robert Martin',
 'Prentice Hall',
 2008),
('Java How To Program',
 'Deitel',
 'Pearson',
 2017),
('Redes de Computadoras',
 'Andrew Tanenbaum',
 'Pearson',
 2012);

⸻

Pregunta 1 (2 puntos)

Crear el Bean Libro.

package com.example.lab.beans;
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

Pregunta 2 (5 puntos)

Crear LibroDao.

Debe implementar:

listar()
buscarPorId()
crear()
actualizar()
borrar()

⸻

Método listar

public ArrayList<Libro> listar()

Consulta:

SELECT * FROM libro;

⸻

Método buscarPorId

public Libro buscarPorId(int id)

Consulta:

SELECT *
FROM libro
WHERE idlibro=?;

⸻

Método crear

public void crear(
        String titulo,
        String autor,
        String editorial,
        int anio)

Consulta:

INSERT INTO libro
(
titulo,
autor,
editorial,
anio_publicacion
)
VALUES (?,?,?,?);

⸻

Método actualizar

public void actualizar(Libro libro)

Consulta:

UPDATE libro
SET
titulo=?,
autor=?,
editorial=?,
anio_publicacion=?
WHERE idlibro=?;

⸻

Método borrar

public void borrar(int id)

Consulta:

DELETE FROM libro
WHERE idlibro=?;

⸻

Pregunta 3 (5 puntos)

Implementar LibroServlet.

Acciones:

?action=lista
?action=new
?action=edit
?action=del

POST:

action=crear
action=e

⸻

Pregunta 4 (4 puntos)

Crear vista de listado.

lista.jsp

Mostrar:

ID	Título	Autor	Editorial	Año

Más dos botones:

Editar
Borrar

Ejemplo:

<td>
<a class="btn btn-success"
href="LibroServlet?action=edit&id=<%= libro.getIdlibro()%>">
Editar
</a>
</td>
<td>
<a class="btn btn-danger"
href="LibroServlet?action=del&id=<%= libro.getIdlibro()%>">
Borrar
</a>
</td>

⸻

Pregunta 5 (4 puntos)

Crear formulario de registro.

form_new.jsp

Campos:

Título
Autor
Editorial
Año

Formulario:

<form method="post"
action="<%=request.getContextPath()%>/LibroServlet">

Botón:

<button type="submit"
class="btn btn-primary">
Guardar
</button>

⸻

Pregunta 6 (4 puntos)

Crear formulario de edición.

form_edit.jsp

Mostrar datos precargados.

Ejemplo:

<input type="text"
name="titulo"
value="<%= libro.getTitulo()%>">

⸻

Estructura final

src/main/java
│
├── beans
│   └── Libro.java
│
├── daos
│   └── LibroDao.java
│
└── servlets
    └── LibroServlet.java
src/main/webapp
│
├── index.jsp
│
└── libro
    ├── lista.jsp
    ├── form_new.jsp
    └── form_edit.jsp

Qué evalúa este laboratorio

Tema	Puntaje
Bean	2
DAO + JDBC	5
Servlet MVC	5
Listado JSP	4
Crear	4
Editar	4
Borrar	3
Total	27

Es un laboratorio muy parecido al estilo que suelen tomar en Ingeniería Web PUCP porque obliga a usar:

* MySQL
* JDBC
* DAO
* Bean
* Servlet
* JSP
* CRUD completo
* Patrón MVC

sin agregar todavía relaciones entre tablas ni ComboBox.
