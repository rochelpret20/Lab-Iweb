1. ¿Qué es JDBC?
Java Database Connectivity
Es la tecnología que permite que Java “hable” con una base de datos como MySQL.
Algo como:
Java ← JDBC → MySQL (JDBC es el puente)

Idea mental MÁS IMPORTANTE
Sin jdbc 
Java NO puede entender SQL directamente

Con jdbc
Java envía SQL
↓
MySQL ejecuta
↓
MySQL devuelve resultados
↓
Java procesa resultados


2. Funcionamiento REAL de JDBC
El proceso es:
1. Cargar Driver
2. Crear conexión
3. Crear Statement
4. Ejecutar SQL
5. Obtener ResultSet
6. Leer datos
7. Cerrar conexión

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.32</version>
</dependency>
