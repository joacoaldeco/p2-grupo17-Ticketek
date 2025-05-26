package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;

public class Estadio extends Sede {

    private int entradasVendidas;
    
    public Estadio(String nombre, String direccion, int capacidad) {
        super(nombre, direccion, capacidad);
        this.entradasVendidas = 0;
    }
    
    /**
     * Calcula el precio de la entrada utilizando la implementación de Sede.
     * 
     * @param precioBase El precio base de la entrada.
     * @param sector El sector en el cual se adquiere la entrada.
     * @return El precio final calculado para la entrada.
     */
    @Override
    public double calcularPrecioEntrada(Double precioBase, Sector sector) {
        return super.calcularPrecioEntrada(precioBase, sector);
    }
    
    /**
     * Registra la venta de un determinado número de entradas, incrementando internamente el contador.
     * Se verifica que la cantidad solicitada no exceda la capacidad restante.
     * 
     * @param cantidad Número de entradas vendidas.
     */
    public void registrarVenta(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de entradas vendidas no puede ser negativa.");
        }
        if (cantidad > calcularCapacidadRestante()) {
            throw new IllegalArgumentException("No hay suficientes entradas disponibles en la sede.");
        }
        entradasVendidas += cantidad;
    }
    
    /**
     * Calcula la capacidad restante del estadio restando el número de entradas vendidas de la capacidad total.
     * 
     * @return La cantidad de asientos aún disponibles.
     */
    public int calcularCapacidadRestante() {
        return getCapacidad() - entradasVendidas;
    }
    
    /**
     * Devuelve la cantidad de entradas vendidas.
     * 
     * @return El número de entradas vendidas.
     */
    public int getEntradasVendidas() {
        return entradasVendidas;
    }
    
    /**
     * Implementación del método abstracto para verificar la disponibilidad de asientos en el estadio.
     * En un estadio sin numeración individual, se verifica que la cantidad solicitada no exceda la capacidad restante.
     * 
     * @param sector Identificador del sector (puede ser un valor predefinido, por ejemplo "Campo").
     * @param asientos Lista de números de asientos solicitados (se usa el tamaño de la lista).
     * @return true si la cantidad de asientos solicitados es menor o igual a la capacidad restante.
     */
    @Override
    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {
        if (asientos == null) {
            throw new IllegalArgumentException("La lista de asientos no puede ser nula.");
        }
        // En un estadio, no se manejan asientos individualmente; se valida por cantidad.
        return asientos.size() <= calcularCapacidadRestante();
    }
    
    /**
     * Implementación del método abstracto para liberar ubicación en el estadio.
     * En un estadio sin administración individualizada de asientos, este método puede no hacer nada.
     * 
     * @param sector El objeto Sector en el cual se libera la ocupación.
     */
    @Override
    public void liberarUbicacion(Sector sector) {
        // En un estadio, es posible que no se administre la liberación de ubicaciones individualizadas.
        // Se deja intencionalmente sin acción o se puede registrar
    }
}