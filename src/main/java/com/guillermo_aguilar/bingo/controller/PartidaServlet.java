package com.guillermo_aguilar.bingo.controller;



import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.repository.PartidaRepository;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/PartidaServlet")
public class PartidaServlet extends HttpServlet {
    private PartidaRepository partidaDAO = new PartidaRepository();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Partida partida = (Partida) session.getAttribute("partida");
        
        String action = request.getParameter("action");
        
        if ("iniciar".equals(action)) {
            partida.setEstado("en_juego");
            partidaDAO.actualizar(partida);
            response.sendRedirect("admin.jsp");
        } else if ("unirse".equals(action)) {
            response.sendRedirect("carton.jsp");
        }
    }
}
