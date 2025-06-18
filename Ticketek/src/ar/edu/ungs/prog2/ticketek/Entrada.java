package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;

public class Entrada implements IEntrada {

    private Integer codigoEntrada;
    private String nombreEspectaculo;
    private LocalDate fechaFuncion;
    private Sector sector;
    private Double precioPagado;

    public Entrada(Integer codigoEntrada, String nombreEspectaculo, LocalDate fechaFuncion,
            Sector sector, Double precioPagado) {

        if (codigoEntrada == null || codigoEntrada <= 0) {
            throw new IllegalArgumentException("Código de entrada inválido.");
        }
        if (nombreEspectaculo == null || nombreEspectaculo.isBlank()) {
            throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        }
        if (fechaFuncion == null) {
            throw new IllegalArgumentException("Fecha de función inválida.");
        }
        if (sector == null) {
            throw new IllegalArgumentException("Sector inválido.");
        }
        if (precioPagado == null || precioPagado < 0) {
            throw new IllegalArgumentException("Precio pagado inválido.");
        }

        this.codigoEntrada = codigoEntrada;
        this.nombreEspectaculo = nombreEspectaculo;
        this.fechaFuncion = fechaFuncion;
        this.sector = sector;
        this.precioPagado = precioPagado;
    }

    @Override
    public double precio() {
        return precioPagado;
    }

    @Override
    public String ubicacion() {
        return sector.getNombre();
    }

    @Override
    public String toString() {
        return "Entrada #" + codigoEntrada +
                " | Espectáculo: " + nombreEspectaculo +
                " | Fecha: " + fechaFuncion +
                " | Sector: " + sector.getNombre() +
                " | Precio pagado: " + precioPagado;
    }

    public Integer getCodigoEntrada() {
        return codigoEntrada;
    }

    @Override
    public String obtenerNombreEspectaculo() {
        return nombreEspectaculo;
    }

    @Override
    public LocalDate obtenerFecha() {
        return fechaFuncion;
    }

    @Override
    public double obtenerPrecioFinal() {
        return precioPagado;
    }

    public Sector getSector() {
        return sector;
    }
}