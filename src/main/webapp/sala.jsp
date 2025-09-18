<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Partida" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Jugador" %>
<%@ page import="java.util.List" %>
<%
    Partida partida = (Partida) session.getAttribute("partida");
    if (partida == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    List<Jugador> jugadores = partida.getJugadores();
    String estadoPartida = partida.getEstado();
    Boolean esAdmin = (Boolean) session.getAttribute("esAdmin");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Sala de Espera</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Sala: <%= partida.getId() %></h1>
        
        <div class="estado-partida <%= estadoPartida %>">
            <% if ("espera".equals(estadoPartida)) { %>
                ⏳ Esperando que el administrador inicie la partida...
            <% } else if ("en_juego".equals(estadoPartida)) { %>
                🎯 La partida está en juego! Redirigiendo...
                <script>
                    setTimeout(() => {
                        window.location.href = 'carton.jsp';
                    }, 2000);
                </script>
            <% } else { %>
                ✅ Partida finalizada
            <% } %>
        </div>
        
        <h2>Jugadores en sala (<%= jugadores.size() %>):</h2>
        <div class="lista-jugadores">
            <% for (Jugador jugador : jugadores) { %>
            <div class="jugador-item">
                👤 <%= jugador.getNombre() %>
                <% if (jugador.equals(session.getAttribute("jugador"))) { %>
                <span style="color: #28a745;">(Tú)</span>
                <% } %>
            </div>
            <% } %>
        </div>
        
        <% if (esAdmin != null && esAdmin) { %>
        <div style="margin-top: 20px;">
            <a href="admin.jsp" class="btn-admin">Ir al Panel de Administración</a>
        </div>
        <% } else { %>
        <div style="margin-top: 20px;">
            <p>Comparte este código con otros jugadores: 
               <strong style="background-color: #007bff; color: white; padding: 5px 10px; border-radius: 3px;">
                   <%= partida.getId() %>
               </strong>
            </p>
        </div>
        <% } %>
        
        <!-- Auto-recargar cada 5 segundos si está en espera -->
        <% if ("espera".equals(estadoPartida)) { %>
        <script>
            setTimeout(() => {
                location.reload();
            }, 5000);
        </script>
        <% } %>
    </div>
</body>
</html>