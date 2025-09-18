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
    
    Boolean esAdmin = (Boolean) session.getAttribute("esAdmin");
    if (esAdmin == null || !esAdmin) {
        response.sendRedirect("carton.jsp");
        return;
    }
    
    List<Jugador> jugadores = partida.getJugadores();
    String estadoPartida = partida.getEstado();
    List<Integer> bolasSorteadas = partida.getBolasSalidas();
    
    // Guardar valores en variables para usar en JavaScript
    String tipoJuego = partida.getTipo();
    int totalBolas = bolasSorteadas.size();
    int bolasRestantes = partida.getBombo().getBolas().size();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Panel de Administración</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <div class="container">

        <h1>🎮 Panel de Administración - Bingo</h1>
        
        <!-- Estado de la Partida -->
        <div class="estado-partida <%= estadoPartida %>">
            <% if ("espera".equals(estadoPartida)) { %>
                ⏳ PARTIDA EN ESPERA - Esperando para iniciar
            <% } else if ("en_juego".equals(estadoPartida)) { %>
                🎯 PARTIDA EN JUEGO - <%= totalBolas %> bolas sorteadas
            <% } else if ("finalizada".equals(estadoPartida)) { %>
                ✅ PARTIDA FINALIZADA - <%= totalBolas %> bolas totales
            <% } %>
        </div>
        
        <div class="panel-admin">
            <!-- Panel de Control -->
            <div class="controles-admin">
                <h2>🛠️ Controles de Partida</h2>
                
                <div class="estadisticas">
                    <div class="estadistica-item">
                        <span class="numero-grande"><%= jugadores.size() %></span>
                        Jugadores
                    </div>
                    <div class="estadistica-item">
                        <span class="numero-grande"><%= totalBolas %></span>
                        Bolas
                    </div>
                    <div class="estadistica-item">
                        <span class="numero-grande"><%= bolasRestantes %></span>
                        Restantes
                    </div>
                </div>
                
                <div style="text-align: center;">
                    <button class="btn-admin btn-iniciar" 
                            onclick="cambiarEstado('iniciar')"
                            <%= !"espera".equals(estadoPartida) ? "disabled" : "" %>>
                        ▶️ Iniciar Partida
                    </button>
                    
                    <button class="btn-admin btn-sacar-bola" 
                            onclick="sacarBola()"
                            <%= !"en_juego".equals(estadoPartida) ? "disabled" : "" %>>
                        🎲 Sacar Bola
                    </button>
                    
                    <button class="btn-admin btn-finalizar" 
                            onclick="cambiarEstado('finalizar')"
                            <%= !"en_juego".equals(estadoPartida) ? "disabled" : "" %>>
                        ⏹️ Finalizar Partida
                    </button>
                </div>
                
                <div style="margin-top: 20px;">
                    <label for="tipoJuego"><strong>Modo de Juego:</strong></label>
                    <select id="tipoJuego" onchange="cambiarTipoJuego()"
                            style="padding: 8px; border-radius: 4px; border: 1px solid #ddd; margin-left: 10px;"
                            <%= !"espera".equals(estadoPartida) ? "disabled" : "" %>>
                        <option value="linea" <%= "linea".equals(tipoJuego) ? "selected" : "" %>>Línea</option>
                        <option value="fullhouse" <%= "fullhouse".equals(tipoJuego) ? "selected" : "" %>>Bingo Completo</option>
                    </select>
                </div>
                
                <!-- Últimas 5 Bolas -->
                <div style="margin-top: 20px;">
                    <h3>🎯 Últimas 5 Bolas Sorteadas</h3>
                    <div class="ultimas-bolas-container" id="ultimasBolasContainer">
                        <% 
                        int start = Math.max(0, bolasSorteadas.size() - 5);
                        for (int i = start; i < bolasSorteadas.size(); i++) { 
                        %>
                        <div class="bola-grande"><%= bolasSorteadas.get(i) %></div>
                        <% } %>
                        
                        <% if (bolasSorteadas.isEmpty()) { %>
                        <p>No se han sacado bolas todavía.</p>
                        <% } %>
                    </div>
                </div>
            </div>
            
            <!-- Información de Partida -->
            <div>
                <h2>👥 Jugadores Conectados (<%= jugadores.size() %>)</h2>
                <div style="max-height: 200px; overflow-y: auto; border: 1px solid #ddd; padding: 15px; border-radius: 8px; background-color: white;">
                    <% if (jugadores.isEmpty()) { %>
                        <p>No hay jugadores conectados.</p>
                    <% } else { %>
                        <% for (Jugador jugador : jugadores) { %>
                        <div style="padding: 8px; border-bottom: 1px solid #eee;">
                            👤 <%= jugador.getNombre() %>
                        </div>
                        <% } %>
                    <% } %>
                </div>
                
                <h3 style="margin-top: 20px;">📋 Código de Sala:</h3>
                <div style="background-color: #007bff; color: white; padding: 15px; border-radius: 8px; text-align: center; font-size: 20px; font-weight: bold; margin: 10px 0;">
                    <%= partida.getId() %>
                </div>
                <p>Comparte este código con los jugadores para que se unan.</p>
                
                <!-- Ganadores -->
                <% if ("finalizada".equals(estadoPartida)) { %>
                <div class="ganadores-container">
                    <h3>🏆 Ganadores</h3>
                    <div id="ganadoresList">
                        <!-- Se actualizará con los ganadores -->
                        <p>La partida ha finalizado.</p>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
        
        <!-- Tablero de Bolas Sorteadas 15x5 -->
        <h2>📊 Tablero de Números Sorteados (1-75)</h2>
        <div class="tablero-bolas" id="tableroBolas">
            <% for (int numero = 1; numero <= 75; numero++) { %>
            <div class="numero-tablero <%= bolasSorteadas.contains(numero) ? "sorteado" : "" %>" 
                 id="bola-<%= numero %>">
                <%= numero %>
            </div>
            <% } %>
        </div>
        
        <div style="margin-top: 20px; text-align: center;">
            <a href="index.jsp" style="color: #007bff; text-decoration: none; font-size: 16px;">← Volver al inicio</a>
        </div>
        <!-- Hidden inputs para pasar valores del servidor al JS -->
        <input id="estadoHidden" type="hidden" 
            value="<%= (estadoPartida!=null)? estadoPartida.replace("\\","\\\\").replace("\"","\\\"") : "" %>" />

        <input id="tipoHidden" type="hidden" 
            value="<%= (tipoJuego!=null)? tipoJuego.replace("\\","\\\\").replace("\"","\\\"") : "" %>" />

        <input id="autoRefreshHidden" type="hidden" 
            value="<%= ("espera".equals(estadoPartida) || "en_juego".equals(estadoPartida)) ? "true" : "false" %>" />

        <input id="checkGanadoresHidden" type="hidden" 
            value="<%= "en_juego".equals(estadoPartida) ? "true" : "false" %>" />

    </div>
    
    <script>
        // Inyección de variables seguras desde el servidor
        window.estadoPartida = "<%= estadoPartida %>";
        window.tipoJuego = "<%= tipoJuego %>";
        window.autoRefresh = <%= ("espera".equals(estadoPartida) || "en_juego".equals(estadoPartida)) ? "true" : "false" %>;
        window.checkGanadores = <%= "en_juego".equals(estadoPartida) ? "true" : "false" %>;
    </script>
    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>