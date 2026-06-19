<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reservas</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Listado de Reservas</h1>

    <div class="barra-superior">

        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/reservas?action=nuevo">

            Añadir Reserva

        </a>

    </div>

    <table>

        <thead>
        <tr>
            <th>Título Evento</th>
            <th>Fecha Evento</th>
            <th>Nombres</th>
            <th>Apellidos</th>
            <th>Email</th>
            <th>Ticket</th>
            <th>Cantidad</th>
            <th>Acciones</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="r" items="${listaReservas}">
            <tr>
                <td>${r.tituloEvento}</td>
                <td>${r.fechaEvento}</td>
                <td>${r.nombres}</td>
                <td>${r.apellidos}</td>
                <td>${r.email}</td>
                <td>${r.nombreTicket}</td>
                <td>${r.cantidad}</td>
                <td>
                    <a class="btn btn-eliminar"
                       href="${pageContext.request.contextPath}/reservas?action=eliminar&id=${r.idItem}"
                       onclick="return confirm('¿Cancelar reserva?')">

                        Cancelar

                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>

</div>

</body>
</html>
