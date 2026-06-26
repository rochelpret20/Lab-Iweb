//Para git
Crear una rama llamada “flowData” clonada de la rama “flowList” para esta pregunta:
● Se solicita implementar la lógica del formulario de registro para autos y seguros. ○ Visualmente, se debe solicitar cada campo de la imagen del diagrama de
Base de datos. ○ Debe almacenar el objeto y visualizarse en la respectiva vista de listado. PREGUNTA 3 (4 puntos)
● Crear una rama llamada “flowLogic” clonada de la rama “flowData” para esta
pregunta. ● Se desea implementar la lógica del boton de eliminación en la vista de autos y
seguros. PREGUNTA 3 (2 puntos)
● Mergear la rama flowLogic en develop (Debe estar completo por lo menos la
pregunta 1 y 2) 

//solu
1. Abrir terminal en la carpeta del proyecto
  
cd Lab10_RentaAutos
  
Verifica que estás en la carpeta correcta (debe tener pom.xml, src, etc.):
ls
                                           
2. Inicializar el repositorio Git
  
git init
  
3. Configurar identidad (si es la primera vez en esa máquina)
  
git config user.name "Tu Nombre"
git config user.email "tucorreo@pucp.edu.pe"
  
4. Verificar el .gitignore (ya viene incluido en el zip)
  
cat .gitignore
Debe excluir target/, .idea/, *.iml. Así no subes basura de IntelliJ/Maven.
  
5. Crear la rama main con el primer commit base
git add .
git commit -m "Estructura base del proyecto Lab2 - Renta de Autos"
git branch -M main
  
(-M main renombra la rama actual, que por defecto puede llamarse master, a main)
  
6. Crear la rama develop desde main
  
git checkout -b develop
  
7. Crear la rama flowList desde develop (Pregunta 1)
                                         
git checkout -b flowList
  
Aquí agregas el commit de la Pregunta 1 (listados con botones agregar/editar/eliminar). Como ya tienes todo el código completo, simplemente confirma que esos archivos estén tal cual (ya están en el working directory):
bashgit add src/main/webapp/autos.jsp
git add src/main/webapp/seguros.jsp
git add src/main/webapp/sedes.jsp
git add src/main/java/com/lab/servlets/AutoServlet.java
git add src/main/java/com/lab/servlets/SeguroServlet.java
git add src/main/java/com/lab/servlets/SedeServlet.java
git add src/main/java/com/lab/dao/
git add src/main/java/com/lab/beans/
git add src/main/java/com/lab/config/
git add src/main/webapp/includes/
git add src/main/webapp/css/
git add src/main/webapp/login.jsp
git add src/main/webapp/WEB-INF/
git add pom.xml database.sql README.md .gitignore
git commit -m "Pregunta 1: vistas de listado con botones agregar/editar/eliminar"

💡 Nota: como nada se commiteó antes en develop, este primer commit en flowList necesariamente incluye también beans/dao/config (no hay forma de evitarlo si arrancas de cero). Lo importante es que el mensaje del commit describa el alcance de la Pregunta 1.

8. Crear la rama flowData desde flowList (Pregunta 2)
                                          
git checkout -b flowData
  
Aquí agregas los formularios (ya existen en el proyecto, solo faltaba commitearlos):

git add src/main/webapp/autoForm.jsp
git add src/main/webapp/seguroForm.jsp
git add src/main/webapp/sedeForm.jsp
  
git commit -m "Pregunta 2: logica de registro de autos y seguros"
  
9. Crear la rama flowLogic desde flowData (Pregunta 3)
  
git checkout -b flowLogic
  
La lógica de eliminación ya está dentro de AutoServlet.java y SeguroServlet.java, que ya fueron commiteados en el paso 7. Para que quede un commit explícito de esta etapa, puedes hacer un commit "vacío" que documente la entrega, o (mejor) hacer un pequeño cambio real, como un comentario aclaratorio, y luego commitear:

git commit --allow-empty -m "Pregunta 3: logica de eliminacion de autos y seguros"
  
10. Volver a develop y mergear flowLogic
  
git checkout develop
git merge flowLogic
  
Como develop no tuvo commits propios después de crear flowList, este merge será fast-forward (lineal, sin conflictos).
  
11. Verificar que todo esté en orden
  
git log --oneline --graph --all
git branch
Deberías ver las 5 ramas: main, develop, flowList, flowData, flowLogic.

12. Crear el repositorio en GitHub y conectar el remoto

Ve a GitHub → New repository → ponle nombre (ej. Lab2-RentaAutos) → no inicialices con README (ya tienes uno) → Create.
Copia la URL que te da GitHub y ejecuta:

bashgit remote add origin https://github.com/TU_USUARIO/Lab2-RentaAutos.git
13. Subir todas las ramas
bashgit push -u origin main
git push -u origin develop
git push -u origin flowList
git push -u origin flowData
git push -u origin flowLogic
14. Verificar en GitHub
Entra a tu repo en GitHub → pestaña branches (o el dropdown de ramas) → confirma que aparecen las 5 ramas con sus commits respectivos.
