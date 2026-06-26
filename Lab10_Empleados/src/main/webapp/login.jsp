<%-- ============================================================
     login.jsp — Página de inicio de sesión
     ============================================================
     CÓMO ADAPTAR A OTRO TEMA:
       No necesita cambios entre proyectos.
       Solo ajusta el título del <h2> si lo deseas.
     ============================================================ --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login — Sistema Empleados</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%-- contenedor-form centra el card verticalmente en la pantalla --%>
<div class="contenedor-form">

    <div class="card">

        <h2>Iniciar Sesión</h2>

        <%-- action apunta al LoginServlet (POST /login) --%>
        <form method="post"
              action="${pageContext.request.contextPath}/login">

            <label>Correo electrónico</label>
            <input type="email"
                   name="email"
                   placeholder="usuario@empresa.com"
                   required>

            <label>Contraseña</label>
            <input type="password"
                   name="password"
                   placeholder="••••••••"
                   required>

            <button type="submit">Ingresar</button>

        </form>

        <%--
            Scriptlet: lee el parámetro ?error=1 que envía LoginServlet
            cuando las credenciales son incorrectas.
            En proyectos reales se prefiere JSTL: <c:if test="${...}">
        --%>
        <%
            String error = request.getParameter("error");
            if (error != null) {
        %>
            <p style="color:red; margin-top:15px; text-align:center;">
                ⚠ Usuario o contraseña incorrectos
            </p>
        <%
            }
        %>

    </div>

</div>

</body>
</html>
