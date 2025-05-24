package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Funcion {
    private LocalDate fecha;
    private Sede sede;
    private double precioBase;
    private List<Entrada> entradasVendidas;

    public Funcion(Sede sede, LocalDate fecha, double precioBase) {
        if (sede == null)
            throw new IllegalArgumentException("Sede inválida.");
        if (fecha == null || fecha.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Fecha inválida.");
        if (precioBase <= 0)
            throw new IllegalArgumentException("Precio base inválido.");

        this.fecha = fecha;
        this.sede = sede;
        this.precioBase = precioBase;
        this.entradasVendidas = new ArrayList<>();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Sede getSede() {
        return sede;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public List<Entrada> getEntradasVendidas() {
        return entradasVendidas;
    }
}
