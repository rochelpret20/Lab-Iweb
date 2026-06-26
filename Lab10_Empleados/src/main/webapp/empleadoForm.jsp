<%-- ============================================================
     empleadoForm.jsp — Formulario de registro y edición
     ============================================================
     CÓMO ADAPTAR A OTRO TEMA:
       1. Cambia los campos <input> y <select> según tu entidad.
       2. Asegúrate de que los atributos name coincidan con
          los request.getParameter() en tu Servlet (doPost).
       3. Ajusta ${empleado.campo} con los getters de tu bean.
     ============================================================

     Este JSP funciona para DOS operaciones:
       - Crear: el Servlet no envía ${empleado} → título "Registrar"
       - Editar: el Servlet envía ${empleado}   → título "Editar" + datos pre-cargados

     La detección se hace con: ${empty empleado ? "..." : "..."}
     --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulario Empleado</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%@ include file="includes/navbar.jspf" %>

<div class="contenedor-form">
    <div class="card">

        <%-- Título dinámico según si es creación o edición --%>
        <h2>
            ${empty empleado ? "Registrar Empleado" : "Editar Empleado"}
        </h2>

        <%--
            action: apunta al EmpleadoServlet (POST /empleados)
            El servlet decide INSERT vs UPDATE según el campo hidden idEmpleado.
        --%>
        <form method="post"
              action="${pageContext.request.contextPath}/empleados">

            <%--
                Campo hidden: transporta el ID del empleado en edición.
                En creación: value="" → el Servlet detecta que es nuevo.
                En edición:  value="5" → el Servlet hace UPDATE.
            --%>
            <input type="hidden"
                   name="idEmpleado"
                   value="${empleado.idEmpleado}">

            <%-- ── Departamento (SELECT) ─────────────────────── --%>
            <label>Departamento</label>
            <select name="idDepartamento">
                <%--
                    c:forEach itera la lista de departamentos que cargó
                    el Servlet con request.setAttribute("listaDepartamentos", ...)
                --%>
                <c:forEach var="dep" items="${listaDepartamentos}">
                    <option value="${dep.idDepartamento}"
                        ${empleado.idDepartamento == dep.idDepartamento
                            ? 'selected' : ''}>
                        ${dep.nombre}
                    </option>
                </c:forEach>
            </select>

            <%-- ── Nombres ─────────────────────────────────────── --%>
            <label>Nombres</label>
            <input type="text"
                   name="nombres"
                   value="${empleado.nombres}"
                   placeholder="Ej: Juan Carlos"
                   required>

            <%-- ── Apellidos ────────────────────────────────────── --%>
            <label>Apellidos</label>
            <input type="text"
                   name="apellidos"
                   value="${empleado.apellidos}"
                   placeholder="Ej: García Pérez"
                   required>

            <%-- ── Cargo ───────────────────────────────────────── --%>
            <label>Cargo</label>
            <input type="text"
                   name="cargo"
                   value="${empleado.cargo}"
                   placeholder="Ej: Desarrollador Senior"
                   required>

            <%-- ── Salario ──────────────────────────────────────── --%>
            <label>Salario (S/)</label>
            <input type="number"
                   step="0.01"
                   min="0"
                   name="salario"
                   value="${empleado.salario}"
                   placeholder="Ej: 3500.00"
                   required>

            <%-- ── Proyectos totales ───────────────────────────── --%>
            <label>Número de Proyectos Disponibles</label>
            <input type="number"
                   min="0"
                   name="proyectos"
                   value="${empleado.proyectos}"
                   placeholder="Ej: 5"
                   required>

            <%-- ── Botones de acción ───────────────────────────── --%>
            <div class="botones-form">

                <button type="submit">Guardar</button>

                <%-- Cancelar: vuelve al listado sin guardar --%>
                <a class="btn btn-regresar"
                   href="${pageContext.request.contextPath}/empleados">
                    Cancelar
                </a>

            </div>

        </form>

    </div>
</div>

</body>
</html>
