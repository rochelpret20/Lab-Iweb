# Lab9 Empleados — Sistema Web Java + MySQL

## Índice
1. [Estructura del Proyecto](#estructura)
2. [Cómo adaptar a otro tema](#adaptar)
3. [Ejecución Local (Windows)](#local)
4. [Despliegue en AWS EC2 — Guía paso a paso](#aws)

---

## 1. Estructura del Proyecto {#estructura}

```
Lab9_Empleados/
├── pom.xml                          ← Dependencias Maven
├── script_bd.sql                    ← Script de base de datos
└── src/main/
    ├── java/com/lab/
    │   ├── beans/                   ← DTOs (datos entre capas)
    │   │   ├── Departamento.java
    │   │   ├── Empleado.java
    │   │   ├── Asignacion.java
    │   │   └── Usuario.java
    │   ├── config/
    │   │   └── ConexionDB.java      ← Configuración JDBC
    │   ├── dao/                     ← Acceso a datos (SQL)
    │   │   ├── DaoDepartamento.java
    │   │   ├── DaoEmpleado.java
    │   │   ├── DaoAsignacion.java
    │   │   └── DaoUsuario.java
    │   └── servlets/                ← Controladores HTTP
    │       ├── LoginServlet.java
    │       ├── LogoutServlet.java
    │       ├── EmpleadoServlet.java
    │       └── AsignacionServlet.java
    └── webapp/
        ├── css/estilos.css
        ├── includes/navbar.jspf
        ├── login.jsp
        ├── empleados.jsp
        ├── empleadoForm.jsp
        ├── asignaciones.jsp
        └── WEB-INF/web.xml
```

**Patrón de capas:**
```
Vista (JSP) ←→ Controlador (Servlet) ←→ DAO ←→ Base de datos (MySQL)
                                          ↕
                                     Bean (DTO)
```

---

## 2. Cómo adaptar a otro tema {#adaptar}

Cada archivo tiene un bloque `CÓMO ADAPTAR A OTRO TEMA` en sus comentarios.  
Resumen rápido de los cambios por archivo:

| Archivo | Qué cambiar |
|---------|-------------|
| `Empleado.java` | Renombrar clase y atributos a tu entidad |
| `Departamento.java` | Renombrar a tu categoría (Genero, Area, Tipo...) |
| `Asignacion.java` | Renombrar a tu relación (Prestamo, Pedido, Reserva...) |
| `ConexionDB.java` | URL, USER, PASSWORD de tu BD |
| `DaoEmpleado.java` | Nombre de tabla y columnas en el SQL |
| `EmpleadoServlet.java` | Nombres de parámetros del form y rutas JSP |
| `empleados.jsp` | Columnas de la tabla y rutas de botones |
| `empleadoForm.jsp` | Campos del formulario |
| `navbar.jspf` | Nombre del sistema y links de navegación |

---

## 3. Ejecución Local (Windows) {#local}

### Requisitos previos
- **JDK 17** — https://adoptium.net  
- **Maven** — https://maven.apache.org/download.cgi  
- **Apache Tomcat 10.x** — https://tomcat.apache.org/download-10.cgi  
- **MySQL 8.x** + MySQL Workbench  
- **IntelliJ IDEA** (recomendado) o Eclipse

### Paso 1 — Crear la base de datos

1. Abre **MySQL Workbench**
2. Conéctate a tu instancia local (`root`)
3. Ve a `File → Open SQL Script` y abre `script_bd.sql`
4. Ejecuta con `Ctrl+Shift+Enter` (o el botón del rayo)
5. Verifica que aparezcan las tablas en el esquema `lab9_empleados`

### Paso 2 — Configurar la conexión

Abre `src/main/java/com/lab/config/ConexionDB.java` y ajusta:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/lab9_empleados?serverTimezone=America/Lima";
private static final String USER     = "root";
private static final String PASSWORD = "TuPasswordAqui2025";  // ← tu contraseña
```

### Paso 3 — Compilar el proyecto

En la terminal (dentro de la carpeta del proyecto):
```bash
mvn clean package
```
Esto genera: `target/Lab9_Empleados-1.0-SNAPSHOT.war`

### Paso 4 — Desplegar en Tomcat local

1. Copia el archivo `.war` a la carpeta `webapps/` de Tomcat
2. Inicia Tomcat: ejecuta `bin/startup.bat`
3. Abre en el navegador: `http://localhost:8080/Lab9_Empleados-1.0-SNAPSHOT/`

**Credenciales de prueba:**
- Email: `admin@empresa.com` / Contraseña: `admin123`
- Email: `maria@empresa.com` / Contraseña: `maria2025`

---

## 4. Despliegue en AWS EC2 — Guía Completa (Windows) {#aws}

### Arquitectura que vamos a crear

```
Internet
   │
   ▼
[EC2 - Ubuntu]
   ├── Apache Tomcat 10   (puerto 8080 → expuesto en 80)
   └── MySQL 8            (puerto 3306, solo acceso local)
```

---

### FASE 1: Crear la instancia EC2

#### 1.1 Ingresar a la consola AWS
1. Ve a https://aws.amazon.com y haz clic en **Sign In**
2. Ingresa con tu cuenta AWS (o cuenta de laboratorio)
3. En la barra de búsqueda superior escribe `EC2` y haz clic en el servicio

#### 1.2 Lanzar una instancia
1. Haz clic en **Launch Instance** (botón naranja)
2. Configura los siguientes campos:

   **Name:** `servidor-lab9`

   **Application and OS Images (AMI):**
   - Selecciona: `Ubuntu Server 22.04 LTS (HVM), SSD Volume Type`
   - Architecture: `64-bit (x86)`

   **Instance type:**
   - Selecciona: `t2.micro` (elegible para capa gratuita)

   **Key pair (login):**
   - Haz clic en `Create new key pair`
   - Name: `llave-lab9`
   - Key pair type: `RSA`
   - Private key file format: `.ppk` (para usar con PuTTY en Windows)
   - Haz clic en `Create key pair` → se descargará `llave-lab9.ppk`
   - ⚠ **Guarda este archivo en un lugar seguro; no se puede volver a descargar**

   **Network settings** → haz clic en `Edit`:
   - VPC: dejar la predeterminada
   - Subnet: dejar la predeterminada
   - Auto-assign public IP: `Enable`
   - Firewall (Security Group): `Create security group`
   - Security group name: `sg-lab9`
   - Agrega las siguientes reglas de entrada:

   | Type       | Protocol | Port | Source    | Descripción        |
   |------------|----------|------|-----------|--------------------|
   | SSH        | TCP      | 22   | 0.0.0.0/0 | Conexión remota    |
   | HTTP       | TCP      | 80   | 0.0.0.0/0 | Web pública        |
   | Custom TCP | TCP      | 8080 | 0.0.0.0/0 | Tomcat directo     |

   **Configure storage:**
   - 20 GiB gp3 (suficiente para el laboratorio)

3. Haz clic en **Launch Instance**
4. Espera 1-2 minutos hasta que el estado cambie a `Running`
5. **Copia la IP pública** de la instancia (ejemplo: `54.123.45.67`)
   - La encuentras en EC2 → Instances → selecciona tu instancia → copia `Public IPv4 address`

---

### FASE 2: Conectarse a la instancia (desde Windows)

#### 2.1 Descargar e instalar PuTTY
1. Descarga PuTTY desde: https://www.putty.org
2. Instala con las opciones por defecto

#### 2.2 Conectarse con PuTTY
1. Abre **PuTTY**
2. En `Host Name`: escribe `ubuntu@54.123.45.67` (cambia por tu IP real)
3. Port: `22`
4. En el panel izquierdo ve a `Connection → SSH → Auth → Credentials`
5. En `Private key file for authentication` haz clic en `Browse`
6. Selecciona el archivo `llave-lab9.ppk` que descargaste
7. Haz clic en `Open`
8. Si aparece un aviso de seguridad, haz clic en `Accept`
9. Deberías ver el prompt: `ubuntu@ip-xxx-xxx-xxx:~$`

---

### FASE 3: Instalar Java, MySQL y Tomcat en EC2

Ejecuta estos comandos en la terminal de PuTTY uno por uno:

#### 3.1 Actualizar el sistema
```bash
sudo apt update && sudo apt upgrade -y
```
Espera a que termine (puede tardar 2-5 minutos).

#### 3.2 Instalar Java 17
```bash
sudo apt install -y openjdk-17-jdk
```
Verifica la instalación:
```bash
java -version
```
Deberías ver algo como: `openjdk version "17.0.x"`

#### 3.3 Instalar MySQL 8
```bash
sudo apt install -y mysql-server
```
Inicia el servicio y configúralo para que arranque automáticamente:
```bash
sudo systemctl start mysql
sudo systemctl enable mysql
```
Ejecuta el asistente de seguridad:
```bash
sudo mysql_secure_installation
```
Responde a las preguntas:
- `Validate password component?` → **N**
- `New password:` → escribe tu contraseña (ej: `Lab9Password2025!`)
- `Re-enter password:` → repite la contraseña
- `Remove anonymous users?` → **Y**
- `Disallow root login remotely?` → **Y**
- `Remove test database?` → **Y**
- `Reload privilege tables?` → **Y**

#### 3.4 Instalar Apache Tomcat 10
```bash
# Descargar Tomcat 10.1
cd /opt
sudo wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.18/bin/apache-tomcat-10.1.18.tar.gz

# Descomprimir
sudo tar -xzf apache-tomcat-10.1.18.tar.gz

# Renombrar para facilitar el acceso
sudo mv apache-tomcat-10.1.18 tomcat10

# Dar permisos de ejecución a los scripts
sudo chmod +x /opt/tomcat10/bin/*.sh
```

#### 3.5 Crear servicio de sistema para Tomcat
```bash
sudo nano /etc/systemd/system/tomcat.service
```
Pega el siguiente contenido (Ctrl+Shift+V en PuTTY):
```ini
[Unit]
Description=Apache Tomcat 10
After=network.target

[Service]
Type=forking
User=root
Group=root
Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
Environment="CATALINA_HOME=/opt/tomcat10"
ExecStart=/opt/tomcat10/bin/startup.sh
ExecStop=/opt/tomcat10/bin/shutdown.sh
Restart=on-failure

[Install]
WantedBy=multi-user.target
```
Guarda con `Ctrl+O`, `Enter`, y cierra con `Ctrl+X`.

Activa e inicia el servicio:
```bash
sudo systemctl daemon-reload
sudo systemctl start tomcat
sudo systemctl enable tomcat
```

Verifica que Tomcat corre:
```bash
sudo systemctl status tomcat
```
Deberías ver `Active: active (running)`.

Prueba en tu navegador: `http://54.123.45.67:8080` (usa tu IP real).  
Deberías ver la página de bienvenida de Apache Tomcat.

---

### FASE 4: Crear la base de datos en el servidor

#### 4.1 Acceder a MySQL en el servidor
```bash
sudo mysql
```

#### 4.2 Crear usuario para la aplicación
```bash
CREATE USER 'lab9user'@'localhost' IDENTIFIED BY 'Lab9Password2025!';
GRANT ALL PRIVILEGES ON lab9_empleados.* TO 'lab9user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 4.3 Crear la base de datos y tablas
```bash
sudo mysql -u lab9user -p'Lab9Password2025!' < /tmp/script_bd.sql
```
(primero debes subir el script, lo hacemos en el siguiente paso)

---

### FASE 5: Subir los archivos al servidor (desde Windows)

#### 5.1 Instalar WinSCP
1. Descarga WinSCP desde: https://winscp.net
2. Instala con las opciones por defecto

#### 5.2 Conectarse con WinSCP
1. Abre **WinSCP**
2. En `File protocol`: selecciona `SFTP`
3. `Host name`: tu IP pública de EC2 (ej: `54.123.45.67`)
4. `Port number`: `22`
5. `User name`: `ubuntu`
6. `Password`: déjalo vacío
7. Haz clic en `Advanced` → `SSH → Authentication`
8. En `Private key file` selecciona tu archivo `llave-lab9.ppk`
9. Haz clic en `OK` y luego en `Login`

#### 5.3 Subir el script SQL y el WAR

En WinSCP tendrás dos paneles:
- Panel izquierdo: tu computadora
- Panel derecho: el servidor EC2

**Subir el script SQL:**
- En el panel izquierdo, navega hasta `script_bd.sql`
- En el panel derecho, navega a `/tmp/`
- Arrastra `script_bd.sql` al panel derecho

**Compilar el WAR (en tu PC primero):**
- En tu PC, dentro de la carpeta del proyecto, ejecuta:
  ```bash
  mvn clean package
  ```
- Esto genera: `target/Lab9_Empleados-1.0-SNAPSHOT.war`

**Subir el WAR:**
- En WinSCP, en el panel derecho navega a `/opt/tomcat10/webapps/`
- Arrastra el archivo `.war` al panel derecho

#### 5.4 Importar la base de datos en el servidor

Vuelve a PuTTY y ejecuta:
```bash
sudo mysql -u lab9user -p'Lab9Password2025!' < /tmp/script_bd.sql
```

---

### FASE 6: Actualizar la configuración de conexión para producción

#### 6.1 Actualizar ConexionDB.java para el servidor

Antes de compilar el WAR, modifica `ConexionDB.java`:
```java
private static final String URL =
    "jdbc:mysql://localhost:3306/lab9_empleados?serverTimezone=America/Lima";
private static final String USER     = "lab9user";
private static final String PASSWORD = "Lab9Password2025!";
```

Luego recompila (`mvn clean package`) y sube el nuevo WAR.

#### 6.2 Verificar el despliegue en Tomcat

En PuTTY:
```bash
# Ver los logs de Tomcat en tiempo real
sudo tail -f /opt/tomcat10/logs/catalina.out
```
Espera unos segundos; deberías ver `Deployment of web application archive [...] has finished`.

Presiona `Ctrl+C` para salir del tail.

---

### FASE 7: Probar la aplicación

Abre tu navegador y escribe:
```
http://54.123.45.67:8080/Lab9_Empleados-1.0-SNAPSHOT/
```
(Reemplaza con tu IP real)

Deberías ver la pantalla de login. Ingresa con:
- Email: `admin@empresa.com`
- Contraseña: `admin123`

---

### FASE 8 (Opcional): Configurar redirección del puerto 80 al 8080

Para acceder sin escribir `:8080` en la URL:

```bash
# Redirigir tráfico del puerto 80 al 8080 con iptables
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080

# Hacer la regla permanente
sudo apt install -y iptables-persistent
sudo netfilter-persistent save
```

Ahora puedes acceder con: `http://54.123.45.67/Lab9_Empleados-1.0-SNAPSHOT/`

---

### Comandos útiles de administración

```bash
# Reiniciar Tomcat
sudo systemctl restart tomcat

# Ver logs de Tomcat
sudo tail -100 /opt/tomcat10/logs/catalina.out

# Ver estado de servicios
sudo systemctl status tomcat
sudo systemctl status mysql

# Reiniciar MySQL
sudo systemctl restart mysql

# Conectarse a MySQL
sudo mysql -u lab9user -p

# Ver WAR desplegados
ls /opt/tomcat10/webapps/
```

---

### Solución de problemas comunes

| Problema | Posible causa | Solución |
|----------|---------------|----------|
| No carga la app en el navegador | Puerto 8080 no abierto | Verificar Security Group en EC2 |
| Error de conexión a BD | Contraseña incorrecta en ConexionDB | Actualizar credenciales y redesplegar WAR |
| Página en blanco | Error en el WAR | Revisar `catalina.out` con `tail -f` |
| Error 404 | URL incorrecta | Verificar nombre del WAR con `ls /opt/tomcat10/webapps/` |
| Tomcat no inicia | Java no instalado | Ejecutar `java -version` para verificar |

---

### Costo estimado en AWS (Free Tier)

| Recurso | Tipo | Costo |
|---------|------|-------|
| EC2 | t2.micro | Gratis (750 h/mes primer año) |
| EBS | 20 GB gp3 | Gratis (30 GB/mes primer año) |
| Transferencia saliente | Hasta 100 GB/mes | Gratis primer año |

> ⚠ **Recuerda detener o terminar la instancia** cuando no la uses para evitar cargos.
> En EC2 → selecciona la instancia → Instance State → **Stop** (detener) o **Terminate** (eliminar).
