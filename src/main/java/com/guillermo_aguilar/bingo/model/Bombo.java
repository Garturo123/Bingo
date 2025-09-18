package com.guillermo_aguilar.bingo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bombo {
    private List<Integer> bolas;
    
    public Bombo() {
        bolas = new ArrayList<>();
        // Llenar el bombo con números del 1 al 75
        for (int i = 1; i <= 75; i++) {
            bolas.add(i);
        }
        Collections.shuffle(bolas);
    }
    
    public int sacarBola() {
        if (bolas.isEmpty()) {
            return -1; // No hay más bolas
        }
        return bolas.remove(0);
    }
    
    public boolean quedanBolas() {
        return !bolas.isEmpty();
    }
    
    // Getters y setters
    public List<Integer> getBolas() { return bolas; }
    public void setBolas(List<Integer> bolas) { this.bolas = bolas; }
}
