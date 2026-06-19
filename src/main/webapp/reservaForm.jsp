<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nueva Reserva</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor-form">

    <div class="card">

        <h2>Añadir Reserva</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form method="post"
              action="${pageContext.request.contextPath}/reservas">

            <label>Usuario</label>
            <select name="idUsuario" required>
                <option value="">-- Seleccione usuario --</option>
                <c:forEach var="u" items="${listaUsuarios}">
                    <option value="${u.idUsuario}">
                        ${u.nombres} ${u.apellidos} - ${u.email}
                    </option>
                </c:forEach>
            </select>

            <label>Evento / Ticket</label>
            <select name="idTicketTipo" required>
                <option value="">-- Seleccione ticket --</option>
                <c:forEach var="t" items="${listaTickets}">
                    <option value="${t.idTicketTipo}">
                        ${t.nombre} (cupo: ${t.cupoDisponible})
                    </option>
                </c:forEach>
            </select>

            <label>Cantidad</label>
            <input type="number"
                   name="cantidad"
                   min="1"
                   value="1"
                   required>

            <div class="botones-form">

                <button type="submit">Guardar</button>

                <a class="btn btn-regresar"
                   href="${pageContext.request.contextPath}/reservas">

                    Cancelar

                </a>

            </div>

        </form>

    </div>

</div>

</body>
</html>
