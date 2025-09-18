package com.guillermo_aguilar.bingo.model;

public class Casilla {
    private int numero;
    private boolean marcado;
    private boolean libre;
    private String letra;
    
    public Casilla(int numero, boolean libre, String letra) {
        this.numero = numero;
        this.marcado = libre; // Si es libre, se marca automáticamente
        this.libre = libre;
        this.letra = letra;
    }
    
    // Getters y setters
    public int getNumero() { return numero; }
    public boolean isMarcado() { return marcado; }
    public void setMarcado(boolean marcado) { this.marcado = marcado; }
    public boolean isLibre() { return libre; }
    public String getLetra() { return letra; }
}