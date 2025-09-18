package com.guillermo_aguilar.bingo.model;

public class Jugador {
    private String id;
    private String nombre;
    private Carton carton;
    
    public Jugador(String id, String nombre, Carton carton) {
        this.id = id;
        this.nombre = nombre;
        this.carton = carton;
    }
    
    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Carton getCarton() { return carton; }
    public void setCarton(Carton carton) { this.carton = carton; }
}
