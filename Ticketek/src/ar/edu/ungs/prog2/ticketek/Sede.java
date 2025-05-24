package ar.edu.ungs.prog2.ticketek;

public abstract class Sede {
    private String nombre;
    private String direccion;
    private int capacidadMaxima;

    public Sede(String nombre, String direccion, int capacidadMaxima) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre inválido");
        if (direccion == null || direccion.isBlank() || !direccion.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) // Expresión regular para validar que haya al menos una letra y un número en la dirección
            throw new IllegalArgumentException("Dirección inválida");
        if (capacidadMaxima <= 0)
            throw new IllegalArgumentException("Capacidad inválida");

        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getDireccion() {
        return direccion;
    }
}
