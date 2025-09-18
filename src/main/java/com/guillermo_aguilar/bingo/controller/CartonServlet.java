package com.guillermo_aguilar.bingo.controller;

import com.guillermo_aguilar.bingo.model.Jugador;
import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.service.JugadorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/CartonServlet")
public class CartonServlet extends HttpServlet {
    private final JugadorService jugadorService;
    
    public CartonServlet() {
        this.jugadorService = new JugadorService();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Jugador jugador = (Jugador) session.getAttribute("jugador");
        Partida partida = (Partida) session.getAttribute("partida");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (jugador == null || partida == null) {
            out.print("{\"success\": false, \"error\": \"Sesión inválida\"}");
            return;
        }
        
        try {
            String numeroStr = request.getParameter("numero");
            if (numeroStr == null || numeroStr.trim().isEmpty()) {
                out.print("{\"success\": false, \"error\": \"Número requerido\"}");
                return;
            }
            
            int numero = Integer.parseInt(numeroStr);
            
            // Verificar si el número está en el cartón
            if (!jugador.getCarton().contieneNumero(numero)) {
                out.print("{\"success\": false, \"error\": \"Número no está en el cartón\"}");
                return;
            }
            
            // Verificar si el número ha sido sorteado
            if (!partida.getBolasSalidas().contains(numero)) {
                out.print("{\"success\": false, \"error\": \"Número no sorteado\"}");
                return;
            }
            
            // Marcar el número en el cartón
            jugador.getCarton().marcarNumero(numero);
            
            // Actualizar el jugador en la sesión
            session.setAttribute("jugador", jugador);
            
            out.print("{\"success\": true, \"marcado\": true}");
            
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"error\": \"Número inválido\"}");
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Error interno: \" + e.getMessage()}");
        }
    }
}