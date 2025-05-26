package ar.edu.ungs.prog2.ticketek;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Espectaculo {
    
    private String nombre;
    // Diccionario que asocia una fecha a una función.
    private Map<Date, Funcion> funciones;
    
    public Espectaculo(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido.");
        }
        this.nombre = nombre;
        this.funciones = new HashMap<>();
    }
    
    /**
     * Recorre todas las funciones y retorna aquella cuya sede coincide
     * con el parámetro especificado.
     * 
     * @param nombreSede Nombre de la sede a buscar.
     * @return La función asociada a esa sede, o null si no se encuentra.
     */
    public Funcion obtenerFuncionPorSede(String nombreSede) {
        for (Funcion f : funciones.values()) {
            if (f.getNombreSede().equals(nombreSede)) {
                return f;
            }
        }
        return null;
    }
    
    /**
     * Indica si el espectáculo contiene alguna función en la sede cuyo nombre se pasa como parámetro.
     * 
     * @param nombreSede Nombre de la sede.
     * @return true si existe una función en esa sede, false en caso contrario.
     */
    public boolean contieneSede(String nombreSede) {
        return obtenerFuncionPorSede(nombreSede) != null;
    }
    
    /**
     * Obtiene el precio base de la función asociada a la sede indicada.
     * 
     * @param nombreSede Nombre de la sede.
     * @return El precio base de la función.
     * @throws IllegalArgumentException si no existe función en esa sede.
     */
    public Double obtenerPrecioBase(String nombreSede) {
        Funcion f = obtenerFuncionPorSede(nombreSede);
        if (f == null) {
            throw new IllegalArgumentException("No existe función en la sede: " + nombreSede);
        }
        return f.getPrecioBase();
    }
    
    /**
     * Calcula la recaudación total del espectáculo sumando la recaudación de cada función registrada.
     * 
     * @return La recaudación total.
     */
    public Double calcularRecaudacionTotal() {
        double total = 0.0;
        for (Funcion f : funciones.values()) {
            total += f.calcularRecaudacion();
        }
        return total;
    }
    
    /**
     * Agrega una nueva función al espectáculo. Se verifica que:
     * - Los parámetros no sean nulos y el precio base sea mayor que cero.
     * - No exista ya una función para la fecha indicada.
     * - No exista ya una función en la misma sede.
     * 
     * @param nombreSede Nombre de la sede donde se realizará la función.
     * @param fecha Fecha de la función.
     * @param precioBase Precio base de la función.
     * @throws IllegalArgumentException si algún parámetro es inválido o hay conflicto de funciones.
     */
    public void agregarFuncion(String nombreSede, Date fecha, Double precioBase) {
        if (nombreSede == null || nombreSede.isBlank()) {
            throw new IllegalArgumentException("Nombre de sede inválido.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (precioBase == null || precioBase <= 0) {
            throw new IllegalArgumentException("Precio base inválido.");
        }
        // Verificar que no exista ya una función para esa fecha.
        if (funciones.containsKey(fecha)) {
            throw new IllegalArgumentException("Ya existe una función en esa fecha.");
        }
        
        // Verificar que no exista ya una función en esa sede.
        if (contieneSede(nombreSede)) {
            throw new IllegalArgumentException("Ya existe una función en la sede: " + nombreSede);
        }
        
        // Crear y agregar la nueva función.
        Funcion nuevaFuncion = new Funcion(nombreSede, fecha, precioBase);
        funciones.put(fecha, nuevaFuncion);
    }
    
    // Getters
    public String getNombre() {
        return nombre;
    }
    
    public Map<Date, Funcion> getFunciones() {
        return funciones;
    }
}