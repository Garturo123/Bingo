<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Bingo Online</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .alert {
            padding: 12px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1px solid transparent;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border-color: #f5c6cb;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border-color: #c3e6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎯 Bingo Online</h1>
        
        <% if (error != null) { %>
        <div class="alert alert-error">
            ⚠️ <%= error %>
        </div>
        <% } %>
        
        <% if (success != null) { %>
        <div class="alert alert-success">
            ✅ <%= success %>
        </div>
        <% } %>
        
        <h2>Ingresar como Admin</h2>
        <form action="CrearPartidaServlet" method="post">
            <label>Código de Sala (opcional):</label>
            <input type="text" name="codigoSala" placeholder="Dejar vacío para auto-generar">
            <button type="submit">Crear Partida</button>
        </form>

        <hr>

        <h2>Ingresar como Jugador</h2>
        <form action="UnirsePartidaServlet" method="post">
            <label>Nombre:</label>
            <input type="text" name="nombre" required>
            <label>Código de Sala:</label>
            <input type="text" name="codigoSala" required>
            <button type="submit">Unirse</button>
        </form>


            
        </div>
        
        <div style="margin-top: 20px; font-size: 14px; color: #666;">
            <p>💡 <strong>Para jugadores:</strong> Ingresa tu nombre y el código de sala que te proporcionó el administrador.</p>
            <p>🛠️ <strong>Para administradores:</strong> Ingresa el código de sala que quieres administrar.</p>
        </div>

    </div>
</body>
</html>