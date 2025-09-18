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
        
        <form action="JugadorServlet" method="POST">
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" required 
                       maxlength="20" placeholder="Ingresa tu nombre">
            </div>
            <div class="form-group">
                <label for="codigoSala">Código de Sala:</label>
                <input type="text" id="codigoSala" name="codigoSala" required 
                       maxlength="10" placeholder="Código de la sala">
            </div>
            <button type="submit">Unirse a la Partida</button>
        </form>
        
        <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #ccc;">
            <h3>¿Eres el administrador?</h3>
            <form action="AdminServlet" method="GET">
                <div class="form-group">
                    <label for="adminCodigoSala">Código de Sala para administrar:</label>
                    <input type="text" id="adminCodigoSala" name="codigoSala" required 
                           maxlength="10" placeholder="Código de sala">
                </div>
                <button type="submit" style="background-color: #f44336;">Acceder como Admin</button>
            </form>
        </div>
        
        <div style="margin-top: 20px; font-size: 14px; color: #666;">
            <p>💡 <strong>Para jugadores:</strong> Ingresa tu nombre y el código de sala que te proporcionó el administrador.</p>
            <p>🛠️ <strong>Para administradores:</strong> Ingresa el código de sala que quieres administrar.</p>
        </div>
    </div>
</body>
</html>