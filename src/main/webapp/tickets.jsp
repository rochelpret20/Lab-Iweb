<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tickets</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Listado de Tickets</h1>

    <div class="barra-superior">

        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/tickets?action=nuevo">

            Crear Tipo de Ticket

        </a>

    </div>

    <table>

        <thead>
        <tr>
            <th>Título Evento</th>
            <th>Descripción Evento</th>
            <th>Fecha Evento</th>
            <th>Lugar Evento</th>
            <th>Nombre Ticket</th>
            <th>Precio</th>
            <th>Cupo Disponible</th>
            <th>Acciones</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="t" items="${listaTickets}">
            <tr>
                <td>${t.tituloEvento}</td>
                <td>${t.descripcionEvento}</td>
                <td>${t.fechaEvento}</td>
                <td>${t.lugarEvento}</td>
                <td>${t.nombreTicket}</td>
                <td>S/ ${t.precio}</td>
                <td>${t.cupoDisponible}</td>
                <td>
                    <a class="btn btn-eliminar"
                       href="${pageContext.request.contextPath}/tickets?action=eliminar&id=${t.idTicketTipo}"
                       onclick="return confirm('¿Eliminar ticket?')">

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
