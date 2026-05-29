src/main/java
│
├── beans
│   ├── Mascota.java
│   ├── Especie.java
│   ├── Veterinario.java
│   └── Dueno.java
│
├── daos
│   ├── DaoBase.java
│   ├── MascotaDao.java
│   ├── EspecieDao.java
│   ├── VeterinarioDao.java
│   └── DuenoDao.java
│
└── servlets
    └── MascotaServlet.java
src/main/webapp
│
└── mascota
    ├── lista.jsp
    ├── form_new.jsp
    └── form_edit.jsp

⸻

PREGUNTA 1 (DaoBase)

Te piden una clase abstracta con:

* Conexión JDBC
* Método getConnection()
* Métodos abstractos:
    * crear()
    * borrar()

￼

DaoBase.java

package com.example.lab7.daos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public abstract class DaoBase {
    private final String URL =
            "jdbc:mysql://localhost:3306/veterinaria";
    private final String USER = "root";
    private final String PASSWORD = "root";
    // Método reutilizable para todos los DAO
    protected Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
    // Métodos abstractos
    public abstract void crear(Object obj);
    public abstract void borrar(int id);
}

⸻

PREGUNTA 2 (Listado)

La consulta principal ya está prácticamente dada en el PDF.  ￼

⸻

Bean Especie

package com.example.lab7.beans;
public class Especie {
    private int idespecie;
    private String nombre;
    public int getIdespecie() {
        return idespecie;
    }
    public void setIdespecie(int idespecie) {
        this.idespecie = idespecie;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

⸻

Bean Veterinario

package com.example.lab7.beans;
public class Veterinario {
    private int idveterinario;
    private String nombre;
    private String especialidad;
    public int getIdveterinario() {
        return idveterinario;
    }
    public void setIdveterinario(int idveterinario) {
        this.idveterinario = idveterinario;
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

⸻

Bean Dueno

package com.example.lab7.beans;
public class Dueno {
    private int iddueno;
    private String nombre;
    private String telefono;
    public int getIddueno() {
        return iddueno;
    }
    public void setIddueno(int iddueno) {
        this.iddueno = iddueno;
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

⸻

Bean Mascota

Aquí está la clave del laboratorio.

No debes guardar solamente IDs.

Debes guardar objetos.

￼

package com.example.lab7.beans;
public class Mascota {
    private int idmascota;
    private String nombre;
    private int edad;
    private double peso;
    private Especie especie;
    private Veterinario veterinario;
    private Dueno dueno;
    // getters y setters
}

⸻

MascotaDao

Método listar()

public ArrayList<Mascota> listar(){
    ArrayList<Mascota> lista =
            new ArrayList<>();
    String sql =
            """
            SELECT
            m.idmascota,
            m.nombre,
            m.edad,
            m.peso,
            e.idespecie,
            e.nombre especie,
            v.idveterinario,
            v.nombre veterinario,
            d.iddueno,
            d.nombre dueno
            FROM mascota m
            INNER JOIN especie e
            ON m.especie_id=e.idespecie
            INNER JOIN veterinario v
            ON m.veterinario_id=v.idveterinario
            INNER JOIN dueno d
            ON m.dueno_id=d.iddueno
            """;
    try(Connection conn=getConnection();
        PreparedStatement pstmt=
                conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery()){
        while(rs.next()){
            Mascota m = new Mascota();
            m.setIdmascota(
                    rs.getInt("idmascota"));
            m.setNombre(
                    rs.getString("nombre"));
            m.setEdad(
                    rs.getInt("edad"));
            m.setPeso(
                    rs.getDouble("peso"));
            Especie e = new Especie();
            e.setIdespecie(
                    rs.getInt("idespecie"));
            e.setNombre(
                    rs.getString("especie"));
            Veterinario v =
                    new Veterinario();
            v.setIdveterinario(
                    rs.getInt("idveterinario"));
            v.setNombre(
                    rs.getString("veterinario"));
            Dueno d = new Dueno();
            d.setIddueno(
                    rs.getInt("iddueno"));
            d.setNombre(
                    rs.getString("dueno"));
            m.setEspecie(e);
            m.setVeterinario(v);
            m.setDueno(d);
            lista.add(m);
        }
    }catch(Exception ex){
        ex.printStackTrace();
    }
    return lista;
}

⸻

PREGUNTA 3 (Crear y Borrar)

￼

Crear

@Override
public void crear(Object obj){
    Mascota m = (Mascota) obj;
    String sql =
            """
            INSERT INTO mascota
            (
            nombre,
            edad,
            peso,
            especie_id,
            veterinario_id,
            dueno_id
            )
            VALUES (?,?,?,?,?,?)
            """;
    try(Connection conn=getConnection();
        PreparedStatement pstmt=
                conn.prepareStatement(sql)){
        pstmt.setString(1,m.getNombre());
        pstmt.setInt(2,m.getEdad());
        pstmt.setDouble(3,m.getPeso());
        pstmt.setInt(4,
                m.getEspecie()
                 .getIdespecie());
        pstmt.setInt(5,
                m.getVeterinario()
                 .getIdveterinario());
        pstmt.setInt(6,
                m.getDueno()
                 .getIddueno());
        pstmt.executeUpdate();
    }catch(Exception ex){
        ex.printStackTrace();
    }
}

⸻

Borrar

@Override
public void borrar(int id){
    String sql =
            "delete from mascota where idmascota=?";
    try(Connection conn=getConnection();
        PreparedStatement pstmt=
                conn.prepareStatement(sql)){
        pstmt.setInt(1,id);
        pstmt.executeUpdate();
    }catch(Exception ex){
        ex.printStackTrace();
    }
}

⸻

PREGUNTA 4 (ComboBox)

￼

DAO Especie

public ArrayList<Especie> listar(){
    ArrayList<Especie> lista =
            new ArrayList<>();
    String sql =
            "select * from especie";
    ...
}

⸻

ComboBox JSP

<select name="idespecie"
        class="form-select">
<% for(Especie e : listaEspecies){ %>
<option value="<%=e.getIdespecie()%>">
    <%=e.getNombre()%>
</option>
<% } %>
</select>

⸻

Filtro por especie

SQL:

String sql =
"""
SELECT ...
FROM mascota m
INNER JOIN especie e
ON m.especie_id=e.idespecie
WHERE e.idespecie=?
""";

Servlet:

String idEspecie =
        request.getParameter("idespecie");
ArrayList<Mascota> lista =
        mascotaDao.filtrarPorEspecie(
                Integer.parseInt(idEspecie));

⸻

PREGUNTA 5 (GitHub)

￼

git init
git add .
git commit -m "Laboratorio 7"
git branch -M main
git remote add origin \
https://github.com/TUUSUARIO/LAB7_IWEB_20231234.git
git push -u origin main

Lo que normalmente evalúan

Pregunta	Archivo principal
P1	DaoBase.java
P2	MascotaDao + lista.jsp
P3	MascotaDao + form_new.jsp + Servlet
P4	EspecieDao + ComboBox + filtro
P5	GitHub ￼
