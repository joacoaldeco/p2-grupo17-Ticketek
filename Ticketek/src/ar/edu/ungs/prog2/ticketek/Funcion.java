package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;
import java.util.Date;

public class Funcion {

    private Sede sede;
    private Date fecha;
    private double precioBase;
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

    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {
        return sede.verificarDisponibilidad(sector, asientos);
    }

    public void registrarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada no puede ser nula.");
        }
        entradasVendidas.add(entrada);
    }


    public double calcularPrecioEntrada(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo.");
        }
        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }


    public void liberarUbicacion(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo.");
        }
        sede.liberarUbicacion(sector);
    }


    public ArrayList<Entrada> obtenerEntradasVendidas() {
        return new ArrayList<>(entradasVendidas);
    }

    public Sede getSede() {
        return sede;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getPrecioBase() {
        return precioBase;
    }
    
//Devuelve el nombre de la sede asociada a esta función
    public String getNombreSede() {
        return sede.getNombre();
    }
    
    /**
     * Calcula la recaudación total de la función.
     * Suma el precio final de cada entrada vendida.
     */
    public double calcularRecaudacion() {
        double total = 0.0;
        for (Entrada e : entradasVendidas) {
            total += e.obtenerPrecioFinal(); // Se asume que Entrada tiene el método obtenerPrecioFinal()
        }
        return total;
    }
}