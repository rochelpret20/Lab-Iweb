<%-- ============================================================
     empleados.jsp — Listado de empleados con acciones CRUD
     ============================================================
     CÓMO ADAPTAR A OTRO TEMA:
       1. Cambia "listaEmpleados" por el nombre de tu atributo.
       2. Ajusta las columnas de la tabla (th y td) a tu entidad.
       3. Cambia los links de Editar/Eliminar/Asignar según tus rutas.
     ============================================================ --%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Empleados</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<%-- Incluimos la navbar (reutilizable en todos los JSPs) --%>
<%@ include file="includes/navbar.jspf" %>

<div class="contenedor">

    <h1>Listado de Empleados</h1>

    <%-- Barra de acciones: botón para crear nuevo empleado --%>
    <div class="barra-superior">
        <a class="btn btn-nuevo"
           href="${pageContext.request.contextPath}/empleados?action=nuevo">
            + Nuevo Empleado
        </a>
    </div>

    <%-- Tabla principal de datos --%>
    <table>

        <thead>
        <tr>
            <th>ID</th>
            <th>Nombres</th>
            <th>Apellidos</th>
            <th>Departamento</th>
            <th>Cargo</th>
            <th>Salario</th>
            <th>Proyectos Disp.</th>
            <th>Acciones</th>
        </tr>
        </thead>

        <tbody>

        <%--
            c:forEach itera sobre la lista "listaEmpleados" que cargó
            EmpleadoServlet con request.setAttribute("listaEmpleados", ...)
            La variable "emp" representa cada objeto Empleado de la lista.
        --%>
        <c:forEach var="emp" items="${listaEmpleados}">
            <tr>
                <td>${emp.idEmpleado}</td>
                <td>${emp.nombres}</td>
                <td>${emp.apellidos}</td>
                <td>${emp.departamentoNombre}</td>
                <td>${emp.cargo}</td>
                <td>S/ ${emp.salario}</td>
                <td>${emp.proyectos}</td>

                <td>

                    <%-- Editar: lleva al formulario pre-cargado --%>
                    <a class="btn btn-editar"
                       href="${pageContext.request.contextPath}/empleados?action=editar&id=${emp.idEmpleado}">
                        Editar
                    </a>

                    <%-- Eliminar: confirm() pide confirmación al usuario antes de ejecutar --%>
                    <a class="btn btn-eliminar"
                       href="${pageContext.request.contextPath}/empleados?action=eliminar&id=${emp.idEmpleado}"
                       onclick="return confirm('¿Eliminar al empleado ${emp.nombres} ${emp.apellidos}?')">
                        Eliminar
                    </a>

                    <%-- Asignar: agrega el empleado a la lista de asignaciones del usuario --%>
                    <a class="btn btn-detalle"
                       href="${pageContext.request.contextPath}/asignaciones?action=agregar&id=${emp.idEmpleado}">
                        Asignar
                    </a>

                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>

</div>

</body>
</html>
