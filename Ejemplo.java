import java.sql.*;

public class SubconsultaJDBC {

    public static void main(String[] args) {

        // DATOS DE CONEXION
        String url =
        "jdbc:mysql://localhost:3306/empresa";

        String user = "root";
        String pass = "root";

        try {

            // 1. CARGAR DRIVER
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. CREAR CONEXION
            Connection conn =
            DriverManager.getConnection(url,user,pass);

            // 3. SQL CON SUBCONSULTA
            String sql =

            "SELECT * " +
            "FROM empleados " +
            "WHERE salario > ( " +
                "SELECT AVG(salario) " +
                "FROM empleados " +
            ")";

            // 4. PREPARED STATEMENT
            PreparedStatement pstmt =
            conn.prepareStatement(sql);

            // 5. EJECUTAR SQL
            ResultSet rs =
            pstmt.executeQuery();

            // 6. MOSTRAR RESULTADOS
            System.out.println(
            "EMPLEADOS CON SUELDO MAYOR AL PROMEDIO");

            while(rs.next()){

                int id =
                rs.getInt("id");

                String nombre =
                rs.getString("nombre");

                String dep =
                rs.getString("departamento");

                double salario =
                rs.getDouble("salario");

                System.out.println(
                    "ID: " + id +
                    " | Nombre: " + nombre +
                    " | Departamento: " + dep +
                    " | Salario: " + salario
                );
            }

            // 7. CERRAR OBJETOS
            rs.close();
            pstmt.close();
            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
