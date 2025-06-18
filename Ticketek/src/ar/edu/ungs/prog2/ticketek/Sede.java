package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;

public abstract class Sede {

    private String nombre;
    private int capacidad;
    private String direccion;
    protected ArrayList<Sector> sectores;

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

    public double calcularPrecioEntrada(Double precioBase, Sector sector) {
        if (!esSectorValido(sector)) {
            throw new IllegalArgumentException("Sector no válido en esta sede.");
        }
        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }

    public boolean esSectorValido(Sector sector) {
        return sector != null && sectores.contains(sector);
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public ArrayList<Sector> getSectores() {
        return sectores;
    }

    public void agregarSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("Sector nulo");
        }
        if (sectores.contains(sector)) {
            throw new IllegalArgumentException("Sector ya existe en la sede.");
        }
        sectores.add(sector);
    }

    protected ArrayList<Sector> clonarSectores() {
        ArrayList<Sector> sectoresClonados = new ArrayList<>();
        for (Sector sector : this.sectores) {
            sectoresClonados.add(clonarSector(sector));
        }
        return sectoresClonados;
    }

    private Sector clonarSector(Sector sectorOriginal) {
        return new Sector(
                sectorOriginal.getNumeroAsiento(),
                sectorOriginal.getNumeroFila(),
                sectorOriginal.getUbicacion(),
                false,
                sectorOriginal.getTipo(),
                sectorOriginal.getPorcentajeAdicional());
    }

    public abstract Sede clonar();

    public abstract boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos);

    public abstract void liberarUbicacion(Sector sector);

    public abstract boolean sectorExiste(String nombreSector);

    public abstract Sector getSector(String nombreSector);

    public abstract boolean asientosDisponibles(String sector, int[] asientos);

    public abstract void asignarAsiento(String sector, int asiento);
}
