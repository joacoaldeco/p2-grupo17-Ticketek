package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;

public class Estadio extends Sede {

    private int entradasVendidas;

    public Estadio(String nombre, String direccion, int capacidad) {
        super(nombre, direccion, capacidad);
        this.entradasVendidas = 0;
    }

    @Override
    public double calcularPrecioEntrada(Double precioBase, Sector sector) {
        return super.calcularPrecioEntrada(precioBase, sector);
    }

    public void registrarVenta(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de entradas vendidas no puede ser negativa.");
        }
        if (cantidad > calcularCapacidadRestante()) {
            throw new IllegalArgumentException("No hay suficientes entradas disponibles en la sede.");
        }
        entradasVendidas += cantidad;
    }

    public int calcularCapacidadRestante() {
        return getCapacidad() - entradasVendidas;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    @Override
    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {
        if (asientos == null) {
            throw new IllegalArgumentException("La lista de asientos no puede ser nula.");
        }

        return asientos.size() <= calcularCapacidadRestante();
    }

    @Override
    public Sede clonar() {
        Estadio estadioClonado = new Estadio(
                this.getNombre(),
                this.getDireccion(),
                this.getCapacidad());
        estadioClonado.sectores = this.clonarSectores();
        estadioClonado.entradasVendidas = 0;
        return estadioClonado;
    }

    @Override
    public boolean sectorExiste(String nombreSector) {
        for (Sector sector : getSectores()) {
            if (sector.getNombre().equals(nombreSector)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Sector getSector(String nombreSector) {
        for (Sector sector : getSectores()) {
            if (sector.getNombre().equals(nombreSector)) {
                return sector;
            }
        }
        return null;
    }

    @Override
    public boolean asientosDisponibles(String sector, int[] asientos) {
        ArrayList<Integer> asientosList = new ArrayList<>();
        for (int asiento : asientos) {
            asientosList.add(asiento);
        }
        return verificarDisponibilidad(sector, asientosList);
    }

    @Override
    public void asignarAsiento(String sector, int asiento) {
        registrarVenta(1);
    }

    @Override
    public void liberarUbicacion(Sector sector) {
        if (entradasVendidas > 0) {
            entradasVendidas--;
        }
    }
}