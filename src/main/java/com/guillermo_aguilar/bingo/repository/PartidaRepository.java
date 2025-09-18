package com.guillermo_aguilar.bingo.repository;


import com.guillermo_aguilar.bingo.model.Partida;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PartidaRepository {
    private static final Map<String, Partida> partidas = new ConcurrentHashMap<>();
    
    public Partida encontrarPorId(String id) {
        return partidas.get(id);
    }
    
    public void guardar(Partida partida) {
        partidas.put(partida.getId(), partida);
    }
    
    public void actualizar(Partida partida) {
        partidas.put(partida.getId(), partida);
    }
    
    public boolean existePartida(String id) {
        return partidas.containsKey(id);
    }
}
