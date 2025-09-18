package com.guillermo_aguilar.bingo.service;


import com.guillermo_aguilar.bingo.model.Jugador;
import com.guillermo_aguilar.bingo.model.Partida;
import com.guillermo_aguilar.bingo.model.Carton;
import com.guillermo_aguilar.bingo.repository.JugadorRepository;
import com.guillermo_aguilar.bingo.repository.PartidaRepository;

public class JugadorService {
    private final JugadorRepository jugadorRepository;
    private final PartidaRepository partidaRepository;
    
    public JugadorService() {
        this.jugadorRepository = new JugadorRepository();
        this.partidaRepository = new PartidaRepository();
    }
    
    public Jugador crearJugador(String nombre) {
        Carton carton = new Carton();
        String id = java.util.UUID.randomUUID().toString();
        Jugador jugador = new Jugador(id, nombre, carton);
        
        jugadorRepository.guardar(jugador);
        return jugador;
    }
    public void actualizarJugador(Jugador jugador) {
        jugadorRepository.guardar(jugador);
        
        // También actualizar en todas las partidas donde esté este jugador
        String partidaId = jugadorRepository.obtenerPartidaDeJugador(jugador.getId());
        if (partidaId != null) {
            Partida partida = partidaRepository.encontrarPorId(partidaId);
            if (partida != null) {
                // Reemplazar el jugador en la partida
                partida.getJugadores().removeIf(j -> j.getId().equals(jugador.getId()));
                partida.getJugadores().add(jugador);
                partidaRepository.actualizar(partida);
            }
        }
    }
    public void agregarJugadorAPartida(Jugador jugador, String partidaId) {
        Partida partida = partidaRepository.encontrarPorId(partidaId);
        if (partida != null && partida.puedeAgregarJugador()) {
            partida.getJugadores().add(jugador);
            partidaRepository.actualizar(partida);
            
            // También guardar la relación en el repositorio de jugadores
            jugadorRepository.agregarJugadorAPartida(jugador.getId(), partidaId);
        }
    }
    
    public boolean existeJugadorEnPartida(String nombre, String partidaId) {
        Partida partida = partidaRepository.encontrarPorId(partidaId);
        if (partida != null) {
            return partida.getJugadores().stream()
                    .anyMatch(j -> j.getNombre().equalsIgnoreCase(nombre));
        }
        return false;
    }
    
    public Jugador obtenerJugador(String id) {
        return jugadorRepository.encontrarPorId(id);
    }
}
