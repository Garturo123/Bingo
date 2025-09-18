package com.guillermo_aguilar.bingo.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BolaRepository {
    // Simulación de base de datos en memoria
    private static Map<String, List<Integer>> bolasPorPartida = new HashMap<>();
    
    public void guardarBola(String partidaId, int bola) {
        if (!bolasPorPartida.containsKey(partidaId)) {
            bolasPorPartida.put(partidaId, new ArrayList<>());
        }
        bolasPorPartida.get(partidaId).add(bola);
    }
    
    public List<Integer> obtenerBolas(String partidaId) {
        return bolasPorPartida.getOrDefault(partidaId, new ArrayList<>());
    }
}
