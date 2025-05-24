package ar.edu.ungs.prog2.ticketek;

public class MiniEstadio extends Teatro {
    private int cantidadPuestos;
    private double precioConsumicion;

    public MiniEstadio(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
            int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad,
            int[] porcentajeAdicional) {
        super(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional);

        if (cantidadPuestos <= 0)
            throw new IllegalArgumentException("Cantidad de puestos inválida");
        if (precioConsumicion <= 0)
            throw new IllegalArgumentException("Precio de consumición inválido");

        this.cantidadPuestos = cantidadPuestos;
        this.precioConsumicion = precioConsumicion;
    }

    public int getCantidadPuestos() {
        return cantidadPuestos;
    }

    public double getPrecioConsumicion() {
        return precioConsumicion;
    }
}
