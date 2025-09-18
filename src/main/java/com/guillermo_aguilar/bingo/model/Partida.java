package com.guillermo_aguilar.bingo.model;

import java.util.ArrayList;
import java.util.List;

public class Partida {
    private String id;
    private List<Jugador> jugadores;
    private List<Integer> bolasSalidas;
    private Bombo bombo;
    private String estado; // "espera", "en_juego", "finalizada"
    private String tipo; // "linea" o "fullhouse"
    
    public Partida(String id) {
        this.id = id;
        this.jugadores = new ArrayList<>();
        this.bolasSalidas = new ArrayList<>();
        this.bombo = new Bombo();
        this.estado = "espera";
        this.tipo = "linea";
    }
    
    // Métodos de negocio
    public boolean puedeAgregarJugador() {
        return "espera".equals(estado);
    }
    
    public boolean puedeSacarBola() {
        return "en_juego".equals(estado);
    }
    
    // Getters y setters
    public String getId() { return id; }
    public List<Jugador> getJugadores() { return jugadores; }
    public List<Integer> getBolasSalidas() { return bolasSalidas; }
    public Bombo getBombo() { return bombo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}