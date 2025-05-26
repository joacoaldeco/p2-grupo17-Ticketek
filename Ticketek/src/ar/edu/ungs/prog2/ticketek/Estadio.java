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
        // En un estadio, no se manejan asientos individualmente; se valida por cantidad.
        return asientos.size() <= calcularCapacidadRestante();
    }
    

    @Override
    public void liberarUbicacion(Sector sector) {
        // En un estadio, es posible que no se administre la liberación de ubicaciones individualizadas.
    }
}