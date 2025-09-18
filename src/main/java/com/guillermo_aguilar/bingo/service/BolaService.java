package com.guillermo_aguilar.bingo.service;

import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.repository.PartidaRepository;
import java.util.List;

public class BolaService {
    private final PartidaRepository partidaRepository;
    
    public BolaService() {
        this.partidaRepository = new PartidaRepository();
    }
    
    public int sacarBola(String partidaId) {
        Partida partida = partidaRepository.encontrarPorId(partidaId);
        if (partida != null && partida.puedeSacarBola()) {
            int bola = partida.getBombo().sacarBola();
            if (bola != -1) {
                partida.getBolasSalidas().add(bola);
                partidaRepository.actualizar(partida);
                return bola;
            }
        }
        return -1; // Error
    }
    
    public List<Integer> obtenerBolasSorteadas(String partidaId) {
        Partida partida = partidaRepository.encontrarPorId(partidaId);
        if (partida != null) {
            return partida.getBolasSalidas();
        }
        return List.of();
    }
    
    public List<Integer> obtenerUltimasBolas(String partidaId, int cantidad) {
        Partida partida = partidaRepository.encontrarPorId(partidaId);
        if (partida != null) {
            List<Integer> bolas = partida.getBolasSalidas();
            int start = Math.max(0, bolas.size() - cantidad);
            return bolas.subList(start, bolas.size());
        }
        return List.of();
    }
}