<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Jugador" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Carton" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Casilla" %>
<%@ page import="com.guillermo_aguilar.bingo.model.Partida" %>
<%
    // CAPA DE PRESENTACIÓN: Solo obtiene datos para mostrar
    Jugador jugador = (Jugador) session.getAttribute("jugador");
    Partida partida = (Partida) session.getAttribute("partida");
    
    if (jugador == null || partida == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    Carton carton = jugador.getCarton();
    String[] letras = Carton.getLetras();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cartón de Bingo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .carton-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 20px 0;
        }
        
        .carton-table {
            border-collapse: collapse;
            margin: 20px auto;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            background-color: white;
        }
        
        .carton-table th {
            background-color: #007bff;
            color: white;
            padding: 15px;
            text-align: center;
            font-weight: bold;
            font-size: 18px;
            width: 60px;
        }
        
        .carton-table td {
            width: 60px;
            height: 60px;
            border: 2px solid #333;
            text-align: center;
            vertical-align: middle;
            cursor: pointer;
            font-weight: bold;
            font-size: 16px;
            transition: all 0.3s ease;
        }
        
        .carton-table td.marcado {
            background-color: #ffeb3b;
            color: #333;
            text-decoration: line-through;
        }
        
        .carton-table td.libre {
            background-color: #4caf50;
            color: white;
            cursor: not-allowed;
            font-style: italic;
        }
        
        .carton-table td:hover:not(.libre):not(.marcado) {
            background-color: #e3f2fd;
            transform: scale(1.05);
        }
        
        .tablero-numeros {
            display: grid;
            grid-template-columns: repeat(15, 40px);
            grid-template-rows: repeat(5, 40px);
            gap: 2px;
            margin: 20px 0;
            max-width: 630px;
        }
        
        .numero-tablero {
            border: 1px solid #ccc;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: bold;
            background-color: #f8f9fa;
        }
        
        .numero-tablero.sorteado {
            background-color: #ff5722;
            color: white;
            transform: scale(1.1);
        }
        
        .bola {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background-color: #2196f3;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 18px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }
        
        .btn-validar {
            background-color: #28a745;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin: 10px;
        }
        
        .btn-validar:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎯 Cartón de <%= jugador.getNombre() %></h1>
        <h2>Sala: <%= partida.getId() %> - Modo: <%= "linea".equals(partida.getTipo()) ? "Línea" : "Bingo Completo" %></h2>
        
        <!-- Últimas bolas -->
        <div class="ultimas-bolas">
            <h3>Últimas bolas sorteadas:</h3>
            <div id="ultimasBolasContainer" style="display: flex; gap: 10px; margin: 10px 0;">
                <!-- Se actualizará con AJAX -->
            </div>
        </div>
        
        <!-- Cartón 5x5 -->
        <div class="carton-container">
            <table class="carton-table">
                <thead>
                    <tr>
                        <% for (String letra : letras) { %>
                        <th><%= letra %></th>
                        <% } %>
                    </tr>
                </thead>
                <tbody>
                    <% for (int fila = 0; fila < 5; fila++) { %>
                    <tr>
                        <% for (int col = 0; col < 5; col++) { 
                            Casilla casilla = carton.getCasilla(fila, col);
                            if (casilla != null) {
                        %>
                        <td class="<%= casilla.isMarcado() ? "marcado" : "" %> 
                                  <%= casilla.isLibre() ? "libre" : "" %>"
                            onclick="marcarCasilla(<%= casilla.getNumero() %>, this)"
                            title="<%= casilla.isLibre() ? "CASILLA LIBRE" : "Número: " + casilla.getNumero() %>">
                            <%= casilla.isLibre() ? "FREE" : casilla.getNumero() %>
                        </td>
                        <%   } %>
                        <% } %>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <!-- Tablero de números -->
        <h3>Números Sorteados (1-75)</h3>
        <div class="tablero-numeros" id="tableroContainer">
            <!-- Se generará con JavaScript -->
        </div>
        
        <!-- Botones -->
        <div style="margin: 20px 0; text-align: center;">
            <button onclick="validarJuego()" class="btn-validar">
                <%= "linea".equals(partida.getTipo()) ? "🎯 Cantar Línea" : "🎉 Cantar Bingo" %>
            </button>
            <button onclick="volverASala()" class="btn-secondary">← Volver a Sala</button>
        </div>
        
        <!-- Mensajes -->
        <div id="mensajeError" style="display: none; background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin: 10px 0;"></div>
        <div id="desempateContainer" style="display: none; background-color: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0; text-align: center;">
            <h3>⚔️ Desempate</h3>
            <p>¡Empate! Número de desempate:</p>
            <div id="numeroDesempate" style="font-size: 32px; font-weight: bold; color: #dc3545;"></div>
        </div>
    </div>

    <!-- JavaScript para la lógica de presentación -->
    <script>
    // Inicializar tablero
    function inicializarTablero() {
        const tablero = document.getElementById('tableroContainer');
        tablero.innerHTML = '';
        
        for (let numero = 1; numero <= 75; numero++) {
            const div = document.createElement('div');
            div.className = 'numero-tablero';
            div.id = 'numero-' + numero;
            div.textContent = numero;
            tablero.appendChild(div);
        }
    }
    
    // Marcar casilla
    function marcarCasilla(numero, elemento) {
        fetch('BolaServlet')
        .then(response => response.json())
        .then(data => {
            if (data.bolasSorteadas.includes(numero)) {
                fetch('CartonServlet', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: 'numero=' + numero
                })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        elemento.classList.add('marcado');
                        mostrarMensaje('✅ Casilla marcada correctamente', 'success');
                    } else {
                        mostrarMensaje('❌ ' + result.error, 'error');
                    }
                });
            } else {
                mostrarMensaje('❌ Este número no ha sido sorteado', 'error');
            }
        });
    }
    
    // Validar juego
    function validarJuego() {
        const tipo = '<%= partida.getTipo() %>';
        fetch('ValidacionServlet?tipo=' + tipo, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.ganador) {
                if (data.desempate) {
                    document.getElementById('desempateContainer').style.display = 'block';
                    document.getElementById('numeroDesempate').textContent = data.numeroDesempate;
                    mostrarMensaje('⚔️ ¡Empate! Se activó desempate', 'warning');
                } else {
                    mostrarMensaje('🎉 ¡Felicidades! Has ganado', 'success');
                }
            } else {
                mostrarMensaje('❌ Aún no tienes el patrón completo', 'error');
            }
        });
    }
    
    // Actualizar bolas
    function actualizarBolas() {
        fetch('BolaServlet')
        .then(response => response.json())
        .then(data => {
            // Últimas bolas
            const container = document.getElementById('ultimasBolasContainer');
            container.innerHTML = '';
            data.ultimasBolas.forEach(bola => {
                const div = document.createElement('div');
                div.className = 'bola';
                div.textContent = bola;
                container.appendChild(div);
            });
            
            // Tablero
            data.bolasSorteadas.forEach(numero => {
                const elemento = document.getElementById('numero-' + numero);
                if (elemento) elemento.classList.add('sorteado');
            });
        });
    }
    
    // Utilidades
    function mostrarMensaje(mensaje, tipo) {
        const div = document.getElementById('mensajeError');
        div.textContent = mensaje;
        div.style.display = 'block';
        div.style.backgroundColor = tipo === 'error' ? '#f8d7da' : 
                                  tipo === 'success' ? '#d4edda' : '#fff3cd';
        div.style.color = tipo === 'error' ? '#721c24' : 
                         tipo === 'success' ? '#155724' : '#856404';
        
        setTimeout(() => div.style.display = 'none', 3000);
    }
    
    function volverASala() {
        window.location.href = 'sala.jsp';
    }
    
    // Inicializar
    document.addEventListener('DOMContentLoaded', function() {
        inicializarTablero();
        actualizarBolas();
        setInterval(actualizarBolas, 2000);
    });
    </script>
</body>
</html>