Lab10- desplegar en AWS

Paso 2 — Crear el servidor EC2
Ya dentro de la consola de AWS:
1. En la barra de búsqueda negra de arriba escribe EC2 y haz clic en EC2.
2. Botón naranja "Launch instance".
3. Rellena así:

Name: quintaola-server
AMI: Ubuntu Server 24.04 LTS (ya viene seleccionado, verifica que diga Ubuntu)
Instance type: t3.small
Key pair: clic en "Create new key pair" → nombre quintaola-key → tipo RSA → formato .pem → Create key pair (se descarga automáticamente a tu carpeta Descargas)
Network settings → clic en Edit:

SSH → My IP
HTTP → ✓ marcado
Clic "Add security group rule" → Type: Custom TCP → Port: 8080 → Source: 0.0.0.0/0


Storage: 20 GB

4. Clic "Launch instance". Espera 1 minuto.
5. Clic en "View all instances" → espera que diga Running → copia la Public IPv4 address (algo como 54.232.x.x).
  
Paso 3 — Conectarte desde tu Mac
Abre la app Terminal en tu Mac (búscala con Cmd+Space → escribe Terminal).
=============================================================
# Mueve la llave a un lugar seguro y dale permisos
chmod 400 ~/Downloads/quintaola-key.pem
icalcs C:\Users\TuusarioDownloads\quintaola-key.pem

# Conéctate al servidor
==========================================================================
//Para Mac:
ssh -i ~/Downloads/quintaola-key.pem ubuntu@TU_IP_PUBLICA

//comado:}
curl -4 ifconfig.me
  
//Para Windows:
ssh -i C:\Users\TuUsuario\Downloads\quintaola-key.pem ubuntu@TU_IP_PUBLICA
==========================================================================

Paso 4 — Instalar Java, MySQL y Tomcat
Pega estos comandos uno por uno:

==============================================================
sudo apt update && sudo apt upgrade -y
sudo apt install openjdk-21-jdk mysql-server -y
sudo systemctl enable --now mysql
cd /opt && sudo wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.55/bin/apache-tomcat-10.1.55.tar.gz
sudo tar -xzf apache-tomcat-10.1.55.tar.gz && sudo mv apache-tomcat-10.1.55 tomcat
sudo chmod +x /opt/tomcat/bin/*.sh
==============================================================

 Paso 5 — Configurar MySQL
========================
sudo mysql
=========================

Ya dentro de MySQL, pega esto completo (cambia ClaveFuerte123! por una tuya que recuerdes):

===================================================================================
CREATE USER 'quintaola'@'localhost' IDENTIFIED BY 'ClaveFuerte123!';
GRANT ALL PRIVILEGES ON inventario_quinta_ola.* TO 'quintaola'@'localhost';
FLUSH PRIVILEGES;
EXIT;
===================================================================================

Paso 6 — Subir tu SQL al servidor
  
Abre una Terminal NUEVA en tu Mac (no cierres la del servidor). Navega a donde está tu SQL y súbelo:
======================================================================================================================
//Para Mac:
scp -i ~/Downloads/quintaola-key.pem ~/Downloads/quintaola_inventario_schema.sql ubuntu@TU_IP_PUBLICA:/home/ubuntu/
//o si no funciona:
scp -i ~/Downloads/quintaola-key.pem ~/Downloads/QuintaOla-completo/target/quinta-ola-1.0-SNAPSHOT.war ubuntu@54.164.81.9:/home/ubuntu/
//Para Windows:
scp -i C:\Users\TuUsuario\Downloads\quintaola-key.pem C:\ruta\al\quintaola_inventario_schema.sql ubuntu@TU_IP_PUBLICA:/home/ubuntu/
======================================================================================================================
Reemplaza la ruta con donde tengas tu archivo. Si está en el Escritorio sería:
~/Desktop/quintaola_inventario_schema.sql. //para rutas
  
Vuelve a la terminal del servidor y carga la base de datos:
=======================================================================
mysql -u quintaola -p < /home/ubuntu/quintaola_inventario_schema.sql
========================================================================
Te pedirá la clave que pusiste en el paso 5.

Paso 7 — Ajustar tu proyecto y generar el WAR

Abre tu proyecto en IntelliJ y en tu archivo DBConnection.java cambia los datos de conexión:
===================================================================================================
// URL: apunta a localhost, base de datos inventario_quinta_ola
private static final String URL  = "jdbc:mysql://localhost:3306/inventario_quinta_ola";
private static final String USER = "quintaola";
private static final String PASS = "ClaveFuerte123!";  // la que pusiste
====================================================================================================
Luego genera el WAR: Build → Build Artifacts → quinta-ola:war → Build. El archivo queda en target/quinta-ola-1.0-SNAPSHOT.war.

Paso 8 — Subir el WAR al servidor
En una Terminal nueva:
=====================================================================================================
//Para Mac:
scp -i ~/Downloads/quintaola-key.pem ~/Downloads/QuintaOla-completo/target/quinta-ola-1.0-SNAPSHOT.war ubuntu@TU_IP_PUBLICA:/home/ubuntu/
//Para windows
scp -i C:\Users\TuUsuario\Downloads\quintaola-key.pem C:\ruta\al\target\quinta-ola-1.0-SNAPSHOT.war ubuntu@TU_IP_PUBLICA:/home/ubuntu/
=====================================================================================================

En la terminal del servidor:
=====================================================================================================
sudo mv /home/ubuntu/quinta-ola-1.0-SNAPSHOT.war /opt/tomcat/webapps/quintaola.war
=====================================================================================================

Paso 9 — Arrancar Tomcat
===============================
sudo /opt/tomcat/bin/startup.sh
===============================
Espera 30 segundos y abre en tu navegador:
http://TU_IP_PUBLICA:8080/quintaola/LoginServlet
