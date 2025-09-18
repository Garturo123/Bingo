package com.guillermo_aguilar.bingo.repository;

import com.guillermo_aguilar.bingo.model.Jugador;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JugadorRepository {
    private static final Map<String, Jugador> jugadores = new ConcurrentHashMap<>();
    private static final Map<String, String> jugadorPartida = new ConcurrentHashMap<>(); // jugadorId -> partidaId
    
    public Jugador encontrarPorId(String id) {
        return jugadores.get(id);
    }
    
    public void guardar(Jugador jugador) {
        jugadores.put(jugador.getId(), jugador);
    }
    
    public void agregarJugadorAPartida(String jugadorId, String partidaId) {
        jugadorPartida.put(jugadorId, partidaId);
    }
    
    public String obtenerPartidaDeJugador(String jugadorId) {
        return jugadorPartida.get(jugadorId);
    }
    
    public boolean existeJugador(String id) {
        return jugadores.containsKey(id);
    }
}
