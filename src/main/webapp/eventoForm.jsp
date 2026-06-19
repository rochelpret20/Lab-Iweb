<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nuevo Evento</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor-form">

    <div class="card">

        <h2>Añadir Evento</h2>

        <c:if test="${not empty error}" xmlns:c="http://java.sun.com/jsp/jstl/core">
            <div class="error">${error}</div>
        </c:if>

        <form method="post"
              action="${pageContext.request.contextPath}/eventos">

            <label>Título</label>
            <input type="text"
                   name="titulo"
                   required>

            <label>Descripción</label>
            <input type="text"
                   name="descripcion"
                   required>

            <label>Fecha</label>
            <input type="date"
                   name="fecha"
                   required>

            <label>Lugar</label>
            <input type="text"
                   name="lugar"
                   required>

            <div class="botones-form">

                <button type="submit">Guardar</button>

                <a class="btn btn-regresar"
                   href="${pageContext.request.contextPath}/eventos">

                    Cancelar

                </a>

            </div>

        </form>

    </div>

</div>

</body>
</html>
