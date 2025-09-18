package com.guillermo_aguilar.bingo.controller;

import com.guillermo_aguilar.bingo.model.Jugador;
import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.model.Validador;
import com.guillermo_aguilar.bingo.service.PartidaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private final PartidaService partidaService;
    private final Validador validador;
    private static final List<String> partidasConGanador;
    
    static {
        partidasConGanador = new ArrayList<>();
    }
    
    public AdminServlet() {
        this.partidaService = new PartidaService();
        this.validador = new Validador();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Partida partida = (Partida) session.getAttribute("partida");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (partida == null) {
            out.print("{\"success\": false, \"error\": \"No hay partida activa\"}");
            return;
        }
        
        String accion = request.getParameter("accion");
        String tipo = request.getParameter("tipo");
        
        try {
            switch (accion) {
                case "iniciar":
                    partidaService.iniciarPartida(partida.getId());
                    out.print("{\"success\": true}");
                    break;
                    
                case "finalizar":
                    partidaService.finalizarPartida(partida.getId());
                    // Marcar como partida con ganador
                    partidasConGanador.add(partida.getId());
                    out.print("{\"success\": true}");
                    break;
                    
                case "cambiarTipo":
                    if (tipo != null && ("linea".equals(tipo) || "fullhouse".equals(tipo))) {
                        partidaService.cambiarTipoJuego(partida.getId(), tipo);
                        out.print("{\"success\": true}");
                    } else {
                        out.print("{\"success\": false, \"error\": \"Tipo no válido\"}");
                    }
                    break;
                    
                case "verificarGanadores":
                    boolean hayGanador = verificarGanadores(partida);
                    out.print("{\"hayGanador\": " + hayGanador + "}");
                    break;
                    
                default:
                    out.print("{\"success\": false, \"error\": \"Acción no válida\"}");
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Error interno: " + e.getMessage() + "\"}");
        }
    }
    
    private boolean verificarGanadores(Partida partida) {
        // Si ya hay ganador en esta partida, no verificar de nuevo
        if (partidasConGanador.contains(partida.getId())) {
            return true;
        }
        
        // Verificar cada jugador
        for (Jugador jugador : partida.getJugadores()) {
            boolean esGanador = false;
            
            if ("linea".equals(partida.getTipo())) {
                esGanador = validador.tieneLinea(jugador.getCarton());
            } else if ("fullhouse".equals(partida.getTipo())) {
                esGanador = validador.tieneFullHouse(jugador.getCarton());
            }
            
            if (esGanador) {
                // Marcar partida como con ganador
                partidasConGanador.add(partida.getId());
                return true;
            }
        }
        
        return false;
    }
}