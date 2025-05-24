package ar.edu.ungs.prog2.ticketek;

import java.util.HashMap;
import java.util.Map;

public class Teatro extends Sede {

    private int asientosPorFila;
    private Map<String, Integer> capacidadPorSector;
    private Map<String, Integer> incrementoPorcentaje;

    public Teatro(String nombre, String direccion, int capacidadMaxima, int asientosPorFila, String[] sectores,
            int[] capacidad, int[] porcentajeAdicional) {
        super(nombre, direccion, capacidadMaxima);

        if (asientosPorFila <= 0)
            throw new IllegalArgumentException("Asientos por fila inválido");
        if (sectores == null || capacidad == null || porcentajeAdicional == null)
            throw new IllegalArgumentException("Parámetros de sectores inválidos.");
        if (sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)
            throw new IllegalArgumentException("Datos de sectores inválidos.");

        this.asientosPorFila = asientosPorFila;
        this.capacidadPorSector = new HashMap<>();
        this.incrementoPorcentaje = new HashMap<>();

        for (int i = 0; i < sectores.length; i++) {
            capacidadPorSector.put(sectores[i], capacidad[i]);
            incrementoPorcentaje.put(sectores[i], porcentajeAdicional[i]);
        }
    }

    public int getAsientosPorFila() {
        return asientosPorFila;
    }
}
