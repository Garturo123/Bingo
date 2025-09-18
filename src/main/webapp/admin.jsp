<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.guillermo_aguilar.bingo.model.Partida" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Jugador" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administrador de Partida</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <% 
        Partida partida = (Partida) getServletContext().getAttribute("partidaGlobal");
        Jugador jugadorAdmin = (Jugador) session.getAttribute("jugador");
        if (partida == null || jugadorAdmin == null || !jugadorAdmin.esAdmin()) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<Jugador> jugadores = partida.getJugadores();
        String estadoPartida = partida.getEstado();
        int ultimaBolaSorteada = partida.getUltimaBolaSorteada();
    %>
    <div class="container">
        <h1>Panel de Administración</h1>
        <div class="card">
            <h2>Estado de la Partida</h2>
            <p>Estado: **<%= estadoPartida %>**</p>
            <% if (estadoPartida.equals("espera")) { %>
                <form action="partida" method="post">
                    <input type="hidden" name="action" value="iniciar">
                    <button type="submit" <%= jugadores.size() < 2 ? "disabled" : "" %>>
                        Iniciar Partida
                    </button>
                </form>
                <% if (jugadores.size() < 2) { %>
                    <p style="color:red;">Se necesitan al menos 2 jugadores para iniciar.</p>
                <% } %>
            <% } else if (estadoPartida.equals("en_curso")) { %>
                <form action="partida" method="post">
                    <input type="hidden" name="action" value="sortear">
                    <button type="submit">Sortear Nueva Bola</button>
                </form>
                <% if (ultimaBolaSorteada != 0) { %>
                    <p>Última bola sorteada: <strong><%= ultimaBolaSorteada %></strong></p>
                <% } %>
            <% } %>
        </div>
        <div class="card">
            <h2>Jugadores Conectados</h2>
            <% if (jugadores.isEmpty()) { %>
                <p>No hay jugadores conectados.</p>
            <% } else { %>
                <% for (Jugador jugador : jugadores) { %>
                    <p>👤 <%= jugador.getNombre() %></p>
                <% } %>
            <% } %>
        </div>
    </div>
</body>
</html>