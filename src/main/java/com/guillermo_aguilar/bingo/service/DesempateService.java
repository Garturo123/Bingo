package com.guillermo_aguilar.bingo.service;

import com.guillermo_aguilar.bingo.model.Validador;
import java.util.HashMap;
import java.util.Map;

public class DesempateService {
    private final Map<String, Integer> desempates; // partidaId -> número de desempate
    private final Validador validador;
    
    public DesempateService() {
        this.desempates = new HashMap<>();
        this.validador = new Validador();
    }
    
    public int generarNumeroDesempate(String partidaId) {
        int numero = validador.desempatar();
        desempates.put(partidaId, numero);
        return numero;
    }
    
    public Integer obtenerNumeroDesempate(String partidaId) {
        return desempates.get(partidaId);
    }
    
    public void limpiarDesempate(String partidaId) {
        desempates.remove(partidaId);
    }
}
