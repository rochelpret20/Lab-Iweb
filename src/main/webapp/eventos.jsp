<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Eventos</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Listado de Eventos</h1>

    <div class="barra-superior">

        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/eventos?action=nuevo">

            Añadir Evento

        </a>

    </div>

    <table>

        <thead>
        <tr>
            <th>Título</th>
            <th>Descripción</th>
            <th>Fecha</th>
            <th>Lugar</th>
            <th>Acciones</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="e" items="${listaEventos}">
            <tr>
                <td>${e.titulo}</td>
                <td>${e.descripcion}</td>
                <td>${e.fecha}</td>
                <td>${e.lugar}</td>
                <td>
                    <a class="btn btn-eliminar"
                       href="${pageContext.request.contextPath}/eventos?action=eliminar&id=${e.idEvento}"
                       onclick="return confirm('¿Eliminar evento?')">

                        Eliminar

                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>

</div>

</body>
</html>
