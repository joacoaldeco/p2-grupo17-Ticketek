package ar.edu.ungs.prog2.ticketek;

public class Sector {
    private int numeroAsiento;
    private int numeroFila;
    private String ubicacion;
    private boolean ocupado;
    private String tipo;
    private double porcentajeAdicional;

    public Sector(int numeroAsiento, int numeroFila, String ubicacion, boolean ocupado, String tipo,
            double porcentajeAdicional) {

        if (numeroAsiento <= 0)
            throw new IllegalArgumentException("Número de asiento inválido");

        if (numeroFila <= 0)
            throw new IllegalArgumentException("Número de fila inválido");

        if (ubicacion == null || ubicacion.isBlank())
            throw new IllegalArgumentException("Ubicación inválida");

        if (tipo == null || tipo.isBlank())
            throw new IllegalArgumentException("Tipo inválido");

        if (porcentajeAdicional < 0)
            throw new IllegalArgumentException("El porcentaje de incremento no puede ser negativo");

        this.numeroAsiento = numeroAsiento;
        this.numeroFila = numeroFila;
        this.ubicacion = ubicacion;
        this.ocupado = ocupado;
        this.tipo = tipo;
        this.porcentajeAdicional = porcentajeAdicional;
    }

    public Sector(String nombre, int capacidad, double porcentajeAdicional) {
        this(1, 1, nombre, false, "CAMPO", porcentajeAdicional);
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public int getNumeroFila() {
        return numeroFila;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPorcentajeAdicional() {
        return porcentajeAdicional;
    }

    public String getNombre() {
        return this.ubicacion;
    }

    public double calcularPrecioAdicional(Double precioBase) {

        if (precioBase == null || precioBase < 0) {
            throw new IllegalArgumentException("Precio base inválido");
        }

        return precioBase * (porcentajeAdicional / 100.0);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Sector))
            return false;

        Sector other = (Sector) obj;
        return this.numeroAsiento == other.numeroAsiento &&
                this.numeroFila == other.numeroFila &&
                this.ubicacion.equals(other.ubicacion);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + numeroAsiento;
        result = 31 * result + numeroFila;
        result = 31 * result + ubicacion.hashCode();
        return result;
    }
}