package ar.edu.ungs.prog2.ticketek;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Espectaculo {
    
    private String nombre;
    private Map<Date, Funcion> funciones;
    
    public Espectaculo(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido.");
        }
        this.nombre = nombre;
        this.funciones = new HashMap<>();
    }
    
    public Funcion obtenerFuncionPorSede(String nombreSede) {
        for (Funcion f : funciones.values()) {
            if (f.getNombreSede().equals(nombreSede)) {
                return f;
            }
        }
        return null;
    }
    
    public boolean contieneSede(String nombreSede) {
        return obtenerFuncionPorSede(nombreSede) != null;
    }
    
    public Double obtenerPrecioBase(String nombreSede) {
        Funcion f = obtenerFuncionPorSede(nombreSede);
        if (f == null) {
            throw new IllegalArgumentException("No existe función en la sede: " + nombreSede);
        }
        return f.getPrecioBase();
    }
    

    public Double calcularRecaudacionTotal() {
        double total = 0.0;
        for (Funcion f : funciones.values()) {
            total += f.calcularRecaudacion();
        }
        return total;
    }
    

    public void agregarFuncion(Sede sede, Date fecha, Double precioBase) {
        if (sede == null) {
            throw new IllegalArgumentException("Sede inválida.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (precioBase == null || precioBase <= 0) {
            throw new IllegalArgumentException("Precio base inválido.");
        }
        if (funciones.containsKey(fecha)) {
            throw new IllegalArgumentException("Ya existe una función en esa fecha.");
        }
        Funcion nuevaFuncion = new Funcion(sede, fecha, precioBase);
        funciones.put(fecha, nuevaFuncion);
    }
    

    public String getNombre() {
        return nombre;
    }
    
    public Map<Date, Funcion> getFunciones() {
        return funciones;
    }
}