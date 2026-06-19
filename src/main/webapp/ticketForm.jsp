<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nuevo Ticket</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor-form">

    <div class="card">

        <h2>Crear Tipo de Ticket</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form method="post"
              action="${pageContext.request.contextPath}/tickets">

            <label>Evento</label>
            <select name="idEvento" required>
                <option value="">-- Seleccione --</option>
                <c:forEach var="e" items="${listaEventos}">
                    <option value="${e.idEvento}">
                        ${e.titulo} (${e.fecha})
                    </option>
                </c:forEach>
            </select>

            <label>Nombre</label>
            <input type="text"
                   name="nombre"
                   required>

            <label>Precio</label>
            <input type="number"
                   step="0.01"
                   min="0"
                   name="precio"
                   required>

            <label>Cupo Total</label>
            <input type="number"
                   min="0"
                   name="cupoTotal"
                   required>

            <label>Cupo Disponible</label>
            <input type="number"
                   min="0"
                   name="cupoDisponible"
                   required>

            <div class="botones-form">

                <button type="submit">Guardar</button>

                <a class="btn btn-regresar"
                   href="${pageContext.request.contextPath}/tickets">

                    Cancelar

                </a>

            </div>

        </form>

    </div>

</div>

</body>
</html>
