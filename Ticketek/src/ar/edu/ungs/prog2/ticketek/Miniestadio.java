package ar.edu.ungs.prog2.ticketek;

import java.util.List;

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

    @Override
    public double calcularPrecioEntrada(double precioBase, Sector sector) {
        double precio = super.calcularPrecioEntrada(precioBase, sector);

        return precio + this.precioConsumicion;
    }

    @Override
    public void liberarUbicacion(Sector sector, List<Integer> asientos) {
        super.liberarUbicacion(sector, asientos);
    }

    @Override
    public Sede clonar() {

        String[] sectoresArray = getSectoresTeatro().keySet().toArray(new String[0]);

        int[] capacidadArray = new int[sectoresArray.length];

        int[] porcentajeArray = new int[sectoresArray.length];

        for (int i = 0; i < sectoresArray.length; i++) {

            Sector sector = getSectoresTeatro().get(sectoresArray[i]);
            capacidadArray[i] = getCapacidadPorSector(sectoresArray[i]);
            porcentajeArray[i] = (int) sector.getPorcentajeAdicional();

        }

        return new MiniEstadio(
                this.getNombre(),
                this.getDireccion(),
                this.getCapacidad(),
                this.getAsientosPorFila(),
                this.cantidadPuestos,
                this.precioConsumicion,
                sectoresArray,
                capacidadArray,
                porcentajeArray);
    }
}
