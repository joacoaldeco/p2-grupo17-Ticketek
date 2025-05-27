package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;

public class Entrada implements IEntrada {

    private Integer codigoEntrada;
    private Espectaculo espectaculo;
    private Funcion funcion;
    private Sector sector;
    private Double precioPagado;

    public Entrada(Integer codigoEntrada, Espectaculo espectaculo, Funcion funcion, Sector sector, Double precioPagado) {
        if (codigoEntrada == null || codigoEntrada <= 0) {
            throw new IllegalArgumentException("Código de entrada inválido.");
        }
        if (espectaculo == null) {
            throw new IllegalArgumentException("Espectáculo inválido.");
        }
        if (funcion == null) {
            throw new IllegalArgumentException("Función inválida.");
        }
        if (sector == null) {
            throw new IllegalArgumentException("Sector inválido.");
        }
        if (precioPagado == null || precioPagado < 0) {
            throw new IllegalArgumentException("Precio pagado inválido.");
        }
        this.codigoEntrada = codigoEntrada;
        this.espectaculo = espectaculo;
        this.funcion = funcion;
        this.sector = sector;
        this.precioPagado = precioPagado;
    }

    @Override
    public double precio() {
        return obtenerPrecioFinal();
    }

    @Override
    public String ubicacion() {
        return obtenerSector().getNombre();
    }

    @Override
    public String toString() {
        return "Entrada #" + codigoEntrada +
               " | Espectáculo: " + obtenerNombreEspectaculo() +
               " | Fecha: " + obtenerFecha() +
               " | Sector: " + obtenerSector().getNombre() +
               " | Precio pagado: " + obtenerPrecioFinal();
    }

    public double obtenerPrecioFinal() {
        return precioPagado;
    }

    public String obtenerNombreEspectaculo() {
        return espectaculo.getNombre();
    }

    public LocalDate obtenerFecha() {
        return funcion.getFecha();
    }

    public Sector obtenerSector() {
        return sector;
    }
    

    public Integer getCodigoEntrada() {
        return codigoEntrada;
    }

    public Espectaculo getEspectaculo() {
        return espectaculo;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Sector getSector() {
        return sector;
    }

    public Double getPrecioPagado() {
        return precioPagado;
    }
}