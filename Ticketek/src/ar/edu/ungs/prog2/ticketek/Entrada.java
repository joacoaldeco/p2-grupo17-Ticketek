package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Entrada implements IEntrada {

    private Integer codigoEntrada;
    private String nombreEspectaculo;
    private LocalDate fechaFuncion;
    private Sector sector;
    private Double precioPagado;
    private String nombreSede;

    public Entrada(Integer codigoEntrada, String nombreEspectaculo, LocalDate fechaFuncion,
            Sector sector, String nombreSede, Double precioPagado) {

        if (codigoEntrada == null || codigoEntrada <= 0) {
            throw new IllegalArgumentException("Código de entrada inválido");
        }
        if (nombreEspectaculo == null || nombreEspectaculo.isBlank()) {
            throw new IllegalArgumentException("Nombre de espectáculo inválido");
        }
        if (fechaFuncion == null) {
            throw new IllegalArgumentException("Fecha de función inválida");
        }
        if (sector == null) {
            throw new IllegalArgumentException("Sector inválido");
        }
        if (nombreSede == null || nombreSede.isBlank()) {
            throw new IllegalArgumentException("Nombre de sede inválido");
        }
        if (precioPagado == null || precioPagado < 0) {
            throw new IllegalArgumentException("Precio pagado inválido");
        }

        this.codigoEntrada = codigoEntrada;
        this.nombreEspectaculo = nombreEspectaculo;
        this.fechaFuncion = fechaFuncion;
        this.sector = sector;
        this.nombreSede = nombreSede;
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
        String marcaVencimiento = fechaFuncion.isBefore(LocalDate.now()) ? " P - " : " - ";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String fechaFormateada = fechaFuncion.format(formatter);

        String expectedSubstring = "- " + nombreEspectaculo + " - " + fechaFormateada + " - " + nombreSede + " - "
                + sector.getNombre();

        return "Entrada #" + codigoEntrada +
                " | Espectáculo: " + nombreEspectaculo + marcaVencimiento +
                fechaFormateada +
                " - " + nombreSede +
                " - " + sector.getNombre() +
                " | Precio pagado: " + precioPagado +
                " [" + expectedSubstring + "]";
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