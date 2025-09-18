
// ⚡ Variables que JSP inyectará dinámicamente en <script> antes de este archivo

let estadoPartida = window.estadoPartida || "";
let tipoJuego = window.tipoJuego || "";

// Sacar bola
function sacarBola() {
    if (estadoPartida === "espera") {
        cambiarEstado("iniciar", true);
        return;
    }

    fetch("BolaServlet", { method: "POST" })
        .then(r => r.json())
        .then(data => {
            if (data.success) {
                setTimeout(() => location.reload(), 500);
            } else {
                alert("Error: " + data.error);
            }
        })
        .catch(e => alert("Error al sacar bola: " + e));
}

// Cambiar estado
function cambiarEstado(accion, fromSacarBola = false) {
    fetch("AdminServlet?accion=" + accion, { method: "POST" })
        .then(r => r.json())
        .then(data => {
            if (data.success) {
                if (fromSacarBola && accion === "iniciar") {
                    setTimeout(sacarBola, 1000);
                } else {
                    location.reload();
                }
            } else {
                alert("Error: " + data.error);
            }
        })
        .catch(e => alert("Error al cambiar estado: " + e));
}

// Cambiar tipo de juego
function cambiarTipoJuego() {
    const tipo = document.getElementById("tipoJuego").value;
    fetch("AdminServlet?accion=cambiarTipo&tipo=" + tipo, { method: "POST" })
        .then(r => r.json())
        .then(data => {
            if (!data.success) {
                alert("Error: " + data.error);
                location.reload();
            }
        });
}

// Verificar ganadores
function verificarGanadores() {
    fetch("AdminServlet?accion=verificarGanadores", { method: "POST" })
        .then(r => r.json())
        .then(data => {
            if (data.hayGanador) {
                cambiarEstado("finalizar");
            }
        });
}

// Auto-refresh segun estado (esto lo controla JSP con flags globales)
if (window.autoRefresh) {
    setInterval(() => location.reload(), 5000);
}
if (window.checkGanadores) {
    setInterval(verificarGanadores, 3000);
}
