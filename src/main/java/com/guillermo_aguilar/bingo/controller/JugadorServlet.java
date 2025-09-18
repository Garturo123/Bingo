package com.guillermo_aguilar.bingo.controller;

import com.guillermo_aguilar.bingo.model.Jugador;
import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.model.Carton;
import com.guillermo_aguilar.bingo.service.JugadorService;
import com.guillermo_aguilar.bingo.service.PartidaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/JugadorServlet")
public class JugadorServlet extends HttpServlet {
    private final JugadorService jugadorService;
    private final PartidaService partidaService;
    
    public JugadorServlet() {
        this.jugadorService = new JugadorService();
        this.partidaService = new PartidaService();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String codigoSala = request.getParameter("codigoSala");
        
        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() || 
            codigoSala == null || codigoSala.trim().isEmpty()) {
            
            response.sendRedirect("index.jsp?error=Nombre y código de sala son obligatorios");
            return;
        }
        
        nombre = nombre.trim();
        codigoSala = codigoSala.trim();
        
        try {
            // Buscar o crear partida usando el servicio
            Partida partida = partidaService.obtenerPartida(codigoSala);
            
            // Verificar si la partida existe y está en estado válido
            if (partida == null) {
                response.sendRedirect("index.jsp?error=La sala no existe");
                return;
            }
            
            if (!partida.puedeAgregarJugador()) {
                response.sendRedirect("index.jsp?error=La partida ya ha comenzado");
                return;
            }
            
            // Verificar si el jugador ya existe en la partida
            if (jugadorService.existeJugadorEnPartida(nombre, codigoSala)) {
                response.sendRedirect("index.jsp?error=Ya existe un jugador con ese nombre en la sala");
                return;
            }
            
            // Crear jugador y cartón usando el servicio
            Jugador jugador = jugadorService.crearJugador(nombre);
            
            // Agregar jugador a la partida usando el servicio
            jugadorService.agregarJugadorAPartida(jugador, codigoSala);
            
            // Guardar en sesión
            HttpSession session = request.getSession();
            session.setAttribute("jugador", jugador);
            session.setAttribute("partida", partida);
            session.setAttribute("esAdmin", false);
            
            // Redirigir a sala de espera
            response.sendRedirect("sala.jsp");
            
        } catch (Exception e) {
            // Manejo de errores
            response.sendRedirect("index.jsp?error=Error interno: " + e.getMessage());
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigir al index si se accede por GET
        response.sendRedirect("index.jsp");
    }
}