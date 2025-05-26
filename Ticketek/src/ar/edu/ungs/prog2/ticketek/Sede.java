package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;

public abstract class Sede {

    private String nombre;
    private int capacidad;
    private String direccion;
    private ArrayList<Sector> sectores;

    public Sede(String nombre, String direccion, int capacidad) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        if (direccion == null || direccion.isBlank() || !direccion.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("Dirección inválida");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("Capacidad inválida");
        }
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
        this.sectores = new ArrayList<>();
    }

    // Método para calcular el precio final de una entrada en un sector determinado.
    // Se utiliza el precio base y se le aplica el porcentaje adicional definido en el sector.
    public double calcularPrecioEntrada(Double precioBase, Sector sector) {
        if (!esSectorValido(sector)) {
            throw new IllegalArgumentException("Sector no válido en esta sede.");
        }
        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }

    // Verifica si el sector es válido en esta sede, es decir, pertenece al array de sectores.
    public boolean esSectorValido(Sector sector) {
        return sector != null && sectores.contains(sector);
    }

    // Retorna la capacidad total de la sede.
    public int getCapacidad() {
        return capacidad;
    }

    // Getter para el nombre de la sede.
    public String getNombre() {
        return nombre;
    }

    // Getter para la dirección de la sede.
    public String getDireccion() {
        return direccion;
    }

    // Getter para el listado de sectores.
    public ArrayList<Sector> getSectores() {
        return sectores;
    }

    // Agrega un nuevo sector a la sede. Si el sector ya existe, lanza una excepción.
    public void agregarSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("Sector nulo");
        }
        if (sectores.contains(sector)) {
            throw new IllegalArgumentException("Sector ya existe en la sede.");
        }
        sectores.add(sector);
    }

    // Métodos abstractos para que sean implementados por las subclases.
    
    /**
     * Verifica la disponibilidad de los asientos en un sector dado.
     *
     * @param sector  Nombre o identificador del sector.
     * @param asientos Lista de números de asientos a verificar.
     * @return true si los asientos están disponibles; false en caso contrario.
     */
    public abstract boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos);

    /**
     * Libera la ubicación (o asiento) asignada en el sector especificado.
     *
     * @param sector El objeto Sector en el que se libera la ubicación.
     */
    public abstract void liberarUbicacion(Sector sector);
}
