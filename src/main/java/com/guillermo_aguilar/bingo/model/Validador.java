package com.guillermo_aguilar.bingo.model;

import java.util.List;

public class Validador {
    
    public boolean tieneLinea(Carton carton) {
        List<Casilla> casillas = carton.getCasillas();
        
        // Verificar líneas horizontales
        for (int fila = 0; fila < 5; fila++) {
            boolean lineaCompleta = true;
            for (int col = 0; col < 5; col++) {
                Casilla casilla = casillas.get(fila * 5 + col);
                if (!casilla.isMarcado()) {
                    lineaCompleta = false;
                    break;
                }
            }
            if (lineaCompleta) return true;
        }
        
        // Verificar líneas verticales
        for (int col = 0; col < 5; col++) {
            boolean lineaCompleta = true;
            for (int fila = 0; fila < 5; fila++) {
                Casilla casilla = casillas.get(fila * 5 + col);
                if (!casilla.isMarcado()) {
                    lineaCompleta = false;
                    break;
                }
            }
            if (lineaCompleta) return true;
        }
        
        // Verificar diagonales
        boolean diagonal1 = true;
        boolean diagonal2 = true;
        for (int i = 0; i < 5; i++) {
            Casilla casilla1 = casillas.get(i * 5 + i);
            Casilla casilla2 = casillas.get(i * 5 + (4 - i));
            
            if (!casilla1.isMarcado()) diagonal1 = false;
            if (!casilla2.isMarcado()) diagonal2 = false;
        }
        
        return diagonal1 || diagonal2;
    }
    
    public boolean tieneFullHouse(Carton carton) {
        for (Casilla casilla : carton.getCasillas()) {
            if (!casilla.isMarcado()) {
                return false;
            }
        }
        return true;
    }
    
    public int desempatar() {
        // Generar número aleatorio del 1 al 10 para desempate
        return (int) (Math.random() * 10) + 1;
    }
}
