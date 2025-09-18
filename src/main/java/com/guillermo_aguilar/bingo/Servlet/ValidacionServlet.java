package com.guillermo_aguilar.bingo.Servlet;

import com.guillermo_aguilar.bingo.model.Jugador;
import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.model.Validador;
import com.guillermo_aguilar.bingo.service.DesempateService;
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

@WebServlet("/ValidacionServlet")
public class ValidacionServlet extends HttpServlet {
    private final Validador validador;
    private final DesempateService desempateService;
    private static final List<String> ganadores; // Para detectar empates
    
    static {
        ganadores = new ArrayList<>();
    }
    
    public ValidacionServlet() {
        this.validador = new Validador();
        this.desempateService = new DesempateService();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Jugador jugador = (Jugador) session.getAttribute("jugador");
        Partida partida = (Partida) session.getAttribute("partida");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (jugador == null || partida == null) {
            out.print("{\"ganador\": false, \"error\": \"Sesión inválida\"}");
            return;
        }
        
        String tipo = request.getParameter("tipo");
        boolean esGanador = false;
        boolean hayDesempate = false;
        int numeroDesempate = 0;
        
        try {
            if ("linea".equals(tipo)) {
                esGanador = validador.tieneLinea(jugador.getCarton());
            } else if ("fullhouse".equals(tipo)) {
                esGanador = validador.tieneFullHouse(jugador.getCarton());
            }
            
            if (esGanador) {
                synchronized (ganadores) {
                    // Verificar si ya hay ganadores (empate)
                    if (!ganadores.contains(partida.getId())) {
                        ganadores.add(partida.getId());
                    } else {
                        // Hay empate, activar desempate
                        hayDesempate = true;
                        numeroDesempate = desempateService.generarNumeroDesempate(partida.getId());
                    }
                }
            }
            
            out.print("{");
            out.print("\"ganador\": " + esGanador + ",");
            out.print("\"desempate\": " + hayDesempate + ",");
            out.print("\"numeroDesempate\": " + numeroDesempate);
            out.print("}");
            
        } catch (Exception e) {
            out.print("{\"ganador\": false, \"error\": \"Error en validación\"}");
        }
    }
}