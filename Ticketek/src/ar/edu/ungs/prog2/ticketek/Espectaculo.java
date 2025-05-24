package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Espectaculo {
    private String nombre;
    private Map<LocalDate, Funcion> funciones = new HashMap<>();

    public Espectaculo(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido.");
        }

        this.nombre = nombre;
    }

    public void agregarFuncion(LocalDate fecha, Sede sede, double precioBase) {
        if (funciones.containsKey(fecha))
            throw new IllegalArgumentException("Ya existe una función para esa fecha.");

        for (Funcion f : funciones.values()) {
            if (f.getSede().getNombre().equals(sede.getNombre()))
                throw new IllegalArgumentException("Ya hay una función en esta sede para este espectáculo.");
        }

        funciones.put(fecha, new Funcion(sede, fecha, precioBase));
    }

    public String getNombre() {
        return nombre;
    }

    public Map<LocalDate, Funcion> getFunciones() {
        return funciones;
    }

}
