package com.guillermo_aguilar.bingo.model;

public class Jugador {
    private String nombre;
    private boolean esAdmin;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.esAdmin = false;
    }

    public Jugador(String nombre, boolean esAdmin) {
        this.nombre = nombre;
        this.esAdmin = esAdmin;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esAdmin() {
        return esAdmin;
    }
}
