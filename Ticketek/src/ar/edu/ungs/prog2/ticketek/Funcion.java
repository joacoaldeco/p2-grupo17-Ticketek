package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Funcion {
    private Sede sede;
    private LocalDate fecha;
    private double precioBase;
    private Set<Entrada> entradasVendidas = new HashSet<>();

    public Funcion(Sede sede, LocalDate fecha, double precioBase) {
        if (sede == null) {
            throw new IllegalArgumentException("Sede inválida");
        }

        if (fecha == null) {
            throw new IllegalArgumentException("Fecha inválida");
        }

        if (precioBase <= 0) {
            throw new IllegalArgumentException("Precio base inválido");
        }
        this.sede = sede.clonar();
        this.fecha = fecha;
        this.precioBase = precioBase;
        this.entradasVendidas = new HashSet<>();
    }

    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {
        return sede.verificarDisponibilidad(sector, asientos);
    }

    public void registrarEntrada(Entrada entrada) {

        if (entrada == null) {
            throw new IllegalArgumentException("La entrada no puede ser nula");
        }

        entradasVendidas.add(entrada);
    }

    public double calcularPrecioEntrada(Sector sector) {

        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo");
        }

        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }

    public void liberarUbicacion(Sector sector) {

        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo");
        }

        sede.liberarUbicacion(sector);
    }

    public void liberarUbicacion(Sector sector, List<Integer> asientos) {

        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo");
        }

        if (asientos == null || asientos.size() <= 0) {
            throw new IllegalArgumentException("Los asientos no pueden ser nulos");
        }

        sede.liberarUbicacion(sector, asientos);
    }

    public Set<Entrada> obtenerEntradasVendidas() {
        return new HashSet<>(entradasVendidas);
    }

    public Sede getSede() {
        return sede;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double calcularPrecioEntrada() {
        return precioBase;
    }

    public String getNombreSede() {
        return sede.getNombre();
    }

    public double calcularRecaudacion() {
        double total = 0.0;
        for (Entrada e : entradasVendidas) {
            total += e.obtenerPrecioFinal();
        }
        return total;
    }

    public void agregarEntradaVendida(Entrada entrada) {

        if (entrada == null)
            throw new IllegalArgumentException("La entrada no puede ser nula");

        entradasVendidas.add(entrada);
    }

    public double calcularPrecioEntrada(String nombreSector) {
        Sector sector = sede.getSector(nombreSector);

        return calcularPrecioEntrada(sector);
    }

    public void eliminarEntradaVendida(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada no puede ser nula");
        }
        entradasVendidas.remove(entrada);
    }

}