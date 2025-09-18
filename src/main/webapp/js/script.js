// Funciones para comunicación con el servidor mediante AJAX
function sacarBola() {
    fetch('BolaServlet', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            actualizarBolas();
        }
    });
}

function marcarCasilla(numero, elemento) {
    fetch('CartonServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'numero=' + numero
    })
    .then(response => response.json())
    .then(data => {
        if (data.marcado) {
            elemento.classList.add('marcado');
        }
    });
}

function validarLinea() {
    fetch('ValidacionServlet?tipo=linea', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.ganador) {
            alert('¡Felicidades! Has cantado línea correctamente.');
        } else {
            alert('Lo siento, aún no tienes línea.');
        }
    });
}

function validarBingo() {
    fetch('ValidacionServlet?tipo=fullhouse', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.ganador) {
            alert('¡BINGO! Has ganado la partida.');
        } else {
            alert('Lo siento, aún no tienes bingo.');
        }
    });
}

function actualizarBolas() {
    // Actualizar últimas bolas y tablero
    fetch('BolaServlet')
    .then(response => response.json())
    .then(data => {
        // Actualizar últimas bolas
        const ultimasBolasContainer = document.getElementById('ultimasBolasContainer');
        ultimasBolasContainer.innerHTML = '';
        data.ultimasBolas.forEach(bola => {
            const bolaElement = document.createElement('div');
            bolaElement.className = 'bola';
            bolaElement.textContent = bola;
            ultimasBolasContainer.appendChild(bolaElement);
        });
        
        // Actualizar tablero
        const tableroContainer = document.getElementById('tableroContainer');
        tableroContainer.innerHTML = '';
        for (let i = 1; i <= 75; i++) {
            const numeroElement = document.createElement('div');
            numeroElement.className = 'numero';
            if (data.bolasSorteadas.includes(i)) {
                numeroElement.classList.add('sorteado');
            }
            numeroElement.textContent = i;
            tableroContainer.appendChild(numeroElement);
        }
    });
}

// Actualizar cada 3 segundos
setInterval(actualizarBolas, 3000);