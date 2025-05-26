package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;
import java.util.Date;

public class Funcion {

    private Sede sede;
    private Date fecha;
    private double precioBase;
    // ArrayList que almacena todas las entradas vendidas para la función.
    private ArrayList<Entrada> entradasVendidas;
    
    public Funcion(Sede sede, Date fecha, double precioBase) {
        if (sede == null) {
            throw new IllegalArgumentException("Sede inválida.");
        }
        if (fecha == null || fecha.before(new Date())) {
            throw new IllegalArgumentException("Fecha inválida.");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException("Precio base inválido.");
        }
        this.sede = sede;
        this.fecha = fecha;
        this.precioBase = precioBase;
        this.entradasVendidas = new ArrayList<>();
    }
    
    /**
     * Verifica que los asientos solicitados en un sector estén disponibles.
     * Se delega la verificación a la Sede.
     * 
     * @param sector Nombre del sector.
     * @param asientos Lista de números de asientos a verificar.
     * @return true si los asientos están disponibles; false en caso contrario.
     */
    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {
        return sede.verificarDisponibilidad(sector, asientos);
    }
    
    /**
     * Registra una entrada vendida para la función.
     * 
     * @param entrada La entrada a registrar.
     */
    public void registrarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada no puede ser nula.");
        }
        entradasVendidas.add(entrada);
    }
    
    /**
     * Calcula el precio final de una entrada para un determinado sector.
     * Se utiliza el precio base de la función y se le suma un porcentaje adicional definido por el sector.
     * 
     * @param sector El objeto Sector que contiene el porcentaje a aplicar.
     * @return El precio final de la entrada.
     */
    public double calcularPrecioEntrada(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo.");
        }
        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }
    
    /**
     * Libera la ubicación asignada en el sector especificado.
     * Se delega la acción a la Sede.
     * 
     * @param sector El Sector en el que se liberará la ubicación.
     */
    public void liberarUbicacion(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo.");
        }
        sede.liberarUbicacion(sector);
    }
    
    /**
     * Devuelve una copia de la lista de entradas vendidas.
     * 
     * @return Un ArrayList de Entrada con las entradas registradas.
     */
    public ArrayList<Entrada> obtenerEntradasVendidas() {
        return new ArrayList<>(entradasVendidas);
    }
    
    // Getters
    public Sede getSede() {
        return sede;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public double getPrecioBase() {
        return precioBase;
    }
}