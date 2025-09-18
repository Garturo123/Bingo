package com.guillermo_aguilar.bingo.service;

import com.guillermo_aguilar.bingo.model.Jugador;
import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.model.Validador;
import com.guillermo_aguilar.bingo.repository.PartidaRepository;

public class PartidaService {
    private final PartidaRepository partidaRepository;
    
    public PartidaService() {
        this.partidaRepository = new PartidaRepository();
    }
    
    public Partida crearPartida(String id) {
        Partida partida = new Partida(id);
        partidaRepository.guardar(partida);
        return partida;
    }
    
    public Partida obtenerPartida(String id) {
        return partidaRepository.encontrarPorId(id);
    }
    
    public void actualizarPartida(Partida partida) {
        partidaRepository.actualizar(partida);
    }
    
    public void iniciarPartida(String id) {
        Partida partida = partidaRepository.encontrarPorId(id);
        if (partida != null) {
            partida.setEstado("en_juego");
            partidaRepository.actualizar(partida);
        }
    }
    public boolean tieneGanadores(String partidaId) {
    Partida partida = partidaRepository.encontrarPorId(partidaId);
        if (partida != null && "en_juego".equals(partida.getEstado())) {
            Validador validador = new Validador();
            
            for (Jugador jugador : partida.getJugadores()) {
                boolean esGanador = false;
                
                if ("linea".equals(partida.getTipo())) {
                    esGanador = validador.tieneLinea(jugador.getCarton());
                } else if ("fullhouse".equals(partida.getTipo())) {
                    esGanador = validador.tieneFullHouse(jugador.getCarton());
                }
                
                if (esGanador) {
                    return true;
                }
            }
        }
        return false;
    }
    public void finalizarPartida(String id) {
        Partida partida = partidaRepository.encontrarPorId(id);
        if (partida != null) {
            partida.setEstado("finalizada");
            partidaRepository.actualizar(partida);
        }
    }
    
    public void cambiarTipoJuego(String id, String tipo) {
        Partida partida = partidaRepository.encontrarPorId(id);
        if (partida != null && "espera".equals(partida.getEstado())) {
            partida.setTipo(tipo);
            partidaRepository.actualizar(partida);
        }
    }
}
