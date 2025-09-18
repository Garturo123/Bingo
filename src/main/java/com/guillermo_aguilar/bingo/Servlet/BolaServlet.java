package com.guillermo_aguilar.bingo.Servlet;

import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.service.BolaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/BolaServlet")
public class BolaServlet extends HttpServlet {
    private final BolaService bolaService;
    
    public BolaServlet() {
        this.bolaService = new BolaService();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Partida partida = (Partida) session.getAttribute("partida");
        Boolean esAdmin = (Boolean) session.getAttribute("esAdmin");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (partida == null || esAdmin == null || !esAdmin) {
            out.print("{\"success\": false, \"error\": \"No autorizado\"}");
            return;
        }
        
        try {
            int nuevaBola = bolaService.sacarBola(partida.getId());
            if (nuevaBola != -1) {
                out.print("{\"success\": true, \"bola\": " + nuevaBola + "}");
            } else {
                out.print("{\"success\": false, \"error\": \"No se pudo sacar bola\"}");
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"Error: " + e.getMessage() + "\"}");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Partida partida = (Partida) session.getAttribute("partida");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (partida == null) {
            out.print("{\"bolasSorteadas\": [], \"ultimasBolas\": []}");
            return;
        }
        
        try {
            List<Integer> bolasSorteadas = partida.getBolasSalidas();
            
            // Obtener últimas 5 bolas
            int start = Math.max(0, bolasSorteadas.size() - 5);
            List<Integer> ultimasBolas = bolasSorteadas.subList(start, bolasSorteadas.size());
            
            out.print("{");
            out.print("\"bolasSorteadas\": " + listaToJsonArray(bolasSorteadas) + ",");
            out.print("\"ultimasBolas\": " + listaToJsonArray(ultimasBolas));
            out.print("}");
        } catch (Exception e) {
            out.print("{\"bolasSorteadas\": [], \"ultimasBolas\": []}");
        }
    }
    
    private String listaToJsonArray(List<Integer> lista) {
        if (lista == null || lista.isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(lista.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}