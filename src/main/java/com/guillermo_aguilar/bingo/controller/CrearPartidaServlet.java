package com.guillermo_aguilar.bingo.controller;

import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.service.PartidaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CrearPartidaServlet")
public class CrearPartidaServlet extends HttpServlet {

    private final PartidaService partidaService = new PartidaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Generar un código de sala (opcional)
        String codigoSala = request.getParameter("codigoSala");
        if (codigoSala == null || codigoSala.isEmpty()) {
            codigoSala = "SALA-" + System.currentTimeMillis();
        }

        // Crear partida
        Partida partida = partidaService.crearPartida(codigoSala);

        // Guardar partida en sesión
        session.setAttribute("partida", partida);
        session.setAttribute("esAdmin", true);

        // Redirigir al panel de administración
        response.sendRedirect("admin.jsp");
    }
}

