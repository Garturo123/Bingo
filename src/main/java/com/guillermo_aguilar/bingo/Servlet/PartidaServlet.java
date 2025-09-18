package com.guillermo_aguilar.bingo.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.model.Jugador;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@WebServlet("/partida")
public class PartidaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        switch (action) {
            case "crear":
                handleCrearPartida(request, response, session);
                break;
            case "unirse":
                handleUnirsePartida(request, response, session);
                break;
            case "iniciar":
                handleIniciarPartida(request, response, session);
                break;
            case "sortear":
                handleSortearBola(request, response, session);
                break;
            default:
                response.sendRedirect("index.jsp");
                break;
        }
    }

    private void handleCrearPartida(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String nombreJugador = request.getParameter("nombre");
        String tipoJuego = request.getParameter("tipoJuego");

        // Crea la partida y la guarda en el contexto de la aplicación para que otros jugadores la encuentren.
        Partida partida = new Partida(tipoJuego);
        getServletContext().setAttribute("partidaGlobal", partida);

        // Agrega al jugador creador (admin) a la partida y a su sesión.
        Jugador jugador = new Jugador(nombreJugador, true); // Es el admin
        partida.agregarJugador(jugador);
        session.setAttribute("jugador", jugador);
        
        response.sendRedirect("admin.jsp");
    }

    private void handleUnirsePartida(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String nombreJugador = request.getParameter("nombre");
        // Obtiene la partida del contexto de la aplicación.
        Partida partida = (Partida) getServletContext().getAttribute("partidaGlobal");

        if (partida != null && partida.getEstado().equals("espera")) {
            Jugador jugador = new Jugador(nombreJugador);
            partida.agregarJugador(jugador);
            session.setAttribute("jugador", jugador);
            response.sendRedirect("jugador.jsp");
        } else {
            response.sendRedirect("index.jsp?error=no_partida");
        }
    }

    private void handleIniciarPartida(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Partida partida = (Partida) getServletContext().getAttribute("partidaGlobal");

        if (partida != null && partida.getJugadores().size() >= 2) {
            partida.iniciarJuego();
            response.sendRedirect("admin.jsp");
        } else {
            response.sendRedirect("admin.jsp?error=min_jugadores");
        }
    }
    
    private void handleSortearBola(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Partida partida = (Partida) getServletContext().getAttribute("partidaGlobal");
        
        if (partida != null && partida.getEstado().equals("en_curso")) {
            // Lógica para sortear una nueva bola
            Random random = new Random();
            List<Integer> bolasDisponibles = partida.getBolasDisponibles();
            
            if (!bolasDisponibles.isEmpty()) {
                int index = random.nextInt(bolasDisponibles.size());
                int bolaSorteada = bolasDisponibles.remove(index);
                partida.getBolasSalidas().add(bolaSorteada);
                partida.setUltimaBolaSorteada(bolaSorteada);
            }
        }
        response.sendRedirect("admin.jsp");
    }
}