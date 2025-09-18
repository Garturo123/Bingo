package com.guillermo_aguilar.bingo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Partida {
    private String tipo;
    private List<Jugador> jugadores;
    private String estado; // "espera", "en_curso", "finalizada"
    private List<Integer> bolasDisponibles;
    private List<Integer> bolasSalidas;
    private int ultimaBolaSorteada;

    public Partida(String tipo) {
        this.tipo = tipo;
        this.jugadores = new ArrayList<>();
        this.estado = "espera";
        this.bolasDisponibles = new ArrayList<>();
        this.bolasSalidas = new ArrayList<>();
        this.ultimaBolaSorteada = 0;
        inicializarBolas();
    }

    private void inicializarBolas() {
        if (this.tipo.equals("90")) {
            for (int i = 1; i <= 90; i++) {
                this.bolasDisponibles.add(i);
            }
        }
        Collections.shuffle(this.bolasDisponibles);
    }

    public void agregarJugador(Jugador jugador) {
        this.jugadores.add(jugador);
    }

    public void iniciarJuego() {
        if (this.estado.equals("espera") && this.jugadores.size() >= 2) {
            this.estado = "en_curso";
        }
    }

    public int sortearBola() {
        if (this.estado.equals("en_curso") && !this.bolasDisponibles.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(this.bolasDisponibles.size());
            int bolaSorteada = this.bolasDisponibles.remove(index);
            this.bolasSalidas.add(bolaSorteada);
            this.ultimaBolaSorteada = bolaSorteada;
            return bolaSorteada;
        }
        return -1; // Retorna -1 si no se puede sortear una bola
    }
    
    public String getTipo() {
        return tipo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public String getEstado() {
        return estado;
    }

    public List<Integer> getBolasDisponibles() {
        return bolasDisponibles;
    }

    public List<Integer> getBolasSalidas() {
        return bolasSalidas;
    }

    public int getUltimaBolaSorteada() {
        return ultimaBolaSorteada;
    }
    
    public void setUltimaBolaSorteada(int ultimaBolaSorteada) {
        this.ultimaBolaSorteada = ultimaBolaSorteada;
    }
}