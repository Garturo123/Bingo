<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.guillermo_aguilar.bingo.model.Jugador" %>
<!DOCTYPE html>
<html>
<head>
    <title>Partida de Bingo</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <%
        Jugador jugador = (Jugador) session.getAttribute("jugador");
        if (jugador == null) {
            response.sendRedirect("index.jsp");
            return;
        }
    %>
    <div class="container">
        <h1>¡Estás en la partida!</h1>
        <div class="card">
            <p>¡Bienvenido, **<%= jugador.getNombre() %>**!</p>
            <p>Esperando que el administrador inicie la partida...</p>
        </div>
    </div>
</body>
</html>