package com.guillermo_aguilar.bingo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carton {
    private String id;
    private List<Casilla> casillas;
    private static final String[] LETRAS = {"B", "I", "N", "G", "O"};
    private static final int[][] RANGOS = {
        {1, 15},    // B
        {16, 30},   // I
        {31, 45},   // N
        {46, 60},   // G
        {61, 75}    // O
    };
    
    public Carton() {
        this.id = java.util.UUID.randomUUID().toString();
        this.casillas = generarCartonValido();
    }
    
    private List<Casilla> generarCartonValido() {
        List<Casilla> casillas = new ArrayList<>();
        
        // Generar números para cada columna (B, I, N, G, O)
        for (int columna = 0; columna < 5; columna++) {
            List<Integer> numerosColumna = new ArrayList<>();
            int min = RANGOS[columna][0];
            int max = RANGOS[columna][1];
            
            // Llenar con todos los números del rango
            for (int i = min; i <= max; i++) {
                numerosColumna.add(i);
            }
            
            // Mezclar y tomar 5 números
            Collections.shuffle(numerosColumna);
            List<Integer> numerosFila = numerosColumna.subList(0, 5);
            
            // Ordenar los números de menor a mayor para mejor visualización
            Collections.sort(numerosFila);
            
            for (int fila = 0; fila < 5; fila++) {
                int numero = numerosFila.get(fila);
                boolean esLibre = (columna == 2 && fila == 2); // Casilla central FREE
                String letra = LETRAS[columna];
                casillas.add(new Casilla(numero, esLibre, letra));
            }
        }
        
        return casillas;
    }
    
    public void marcarNumero(int numero) {
        for (Casilla casilla : casillas) {
            if (casilla.getNumero() == numero) {
                casilla.setMarcado(true);
                break;
            }
        }
    }
    
    public boolean contieneNumero(int numero) {
        return casillas.stream().anyMatch(c -> c.getNumero() == numero);
    }
    
    public Casilla getCasilla(int fila, int columna) {
        int index = fila * 5 + columna;
        if (index >= 0 && index < casillas.size()) {
            return casillas.get(index);
        }
        return null;
    }
    
    public List<Casilla> getCasillasPorFila(int fila) {
        List<Casilla> casillasFila = new ArrayList<>();
        for (int col = 0; col < 5; col++) {
            casillasFila.add(getCasilla(fila, col));
        }
        return casillasFila;
    }
    
    // Getters
    public String getId() { return id; }
    public List<Casilla> getCasillas() { return casillas; }
    public static String[] getLetras() { return LETRAS; }
}
