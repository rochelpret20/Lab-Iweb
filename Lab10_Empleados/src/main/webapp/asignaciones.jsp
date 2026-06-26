<%-- ============================================================
     asignaciones.jsp — Lista de asignaciones del usuario
     ============================================================
     CÓMO ADAPTAR A OTRO TEMA:
       1. Cambia "listaAsignaciones" por el nombre de tu atributo.
       2. Ajusta las columnas de la tabla a los campos de tu bean.
       3. Cambia el label del total ("Costo Total" → "Total", etc.)
     ============================================================ --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Asignaciones</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Mis Asignaciones de Empleados</h1>

    <%-- Tabla de asignaciones --%>
    <table>
        <thead>
        <tr>
            <th>ID Asignación</th>
            <th>Empleado</th>
            <th>Asignado por</th>
            <th>Salario Base</th>
            <th>Meses</th>
            <th>Costo Total</th>
        </tr>
        </thead>

        <tbody>

        <%--
            Itera sobre la lista "listaAsignaciones" que
            AsignacionServlet puso en el request.
        --%>
        <c:forEach var="a" items="${listaAsignaciones}">
            <tr>
                <td>${a.idAsignacion}</td>
                <td>${a.nombreEmpleado}</td>
                <td>${a.nombreUsuario}</td>
                <td>S/ ${a.salarioBase}</td>
                <td>${a.meses}</td>
                <td>S/ ${a.costoTotal}</td>
            </tr>
        </c:forEach>

        </tbody>
    </table>

    <br>

    <%-- Tarjeta con el costo total general --%>
    <div class="card">
        <h2>Costo Total del Proyecto: S/ ${costoTotal}</h2>
    </div>

</div>

</body>
</html>
