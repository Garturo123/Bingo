<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>⚠️ Error en la aplicación</h1>
        <p>Ha ocurrido un error inesperado.</p>
        <p>Mensaje: <%= exception != null ? exception.getMessage() : "Error desconocido" %></p>
        <a href="index.jsp">Volver al inicio</a>
        
        <% // Solo mostrar detalles en desarrollo %>
        <% if (request.getServletContext().getInitParameter("mode").equals("development")) { %>
        <div style="margin-top: 20px; padding: 10px; background-color: #f8d7da; color: #721c24;">
            <h3>Detalles del error:</h3>
            <pre><% if (exception != null) { 
                java.io.StringWriter sw = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                exception.printStackTrace(pw);
                out.print(sw.toString());
            } %></pre>
        </div>
        <% } %>
    </div>
</body>
</html>