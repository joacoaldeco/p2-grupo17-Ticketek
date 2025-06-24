package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;
import java.util.List;

public class Estadio extends Sede {

    private int entradasVendidas;

    public Estadio(String nombre, String direccion, int capacidad) {
        super(nombre, direccion, capacidad);
        this.entradasVendidas = 0;

        Sector sectorCampo = new Sector("CAMPO", capacidad, 0.0);
        this.sectores.add(sectorCampo);
    }

    @Override
    public double calcularPrecioEntrada(double precioBase, Sector sector) {
        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }

    public void registrarVenta(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de entradas vendidas no puede ser negativa");
        }
        if (cantidad > calcularCapacidadRestante()) {
            throw new IllegalArgumentException("No hay suficientes entradas disponibles en la sede");
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
            throw new IllegalArgumentException("La lista de asientos no puede ser nula");
        }

        return asientos.size() <= calcularCapacidadRestante();
    }

    @Override
    public Sede clonar() {
        Estadio estadioClonado = new Estadio(
                this.getNombre(),
                this.getDireccion(),
                this.getCapacidad());
        return estadioClonado;
    }

    @Override
    public boolean sectorExiste(String nombreSector) {
        return nombreSector.equals("CAMPO");
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

    @Override
    public void liberarUbicacion(Sector sector, List<Integer> asientos) {
        return;
    }
}