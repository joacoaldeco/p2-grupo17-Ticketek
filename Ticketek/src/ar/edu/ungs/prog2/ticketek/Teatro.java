package ar.edu.ungs.prog2.ticketek;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Teatro extends Sede {

    private int asientosPorFila;
    private Map<String, Sector> sectoresTeatro;
    private Map<String, Integer> capacidadPorSector;
    private Map<String, ArrayList<Integer>> reservas;

    public Teatro(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
            String[] sectores, int[] capacidadPorSector, int[] porcentajeAdicional) {
        super(nombre, direccion, capacidadMaxima);

        if (asientosPorFila <= 0)
            throw new IllegalArgumentException("Asientos por fila inválido");

        if (sectores == null || capacidadPorSector == null || porcentajeAdicional == null)
            throw new IllegalArgumentException("Parámetros de sectores inválidos");

        if (sectores.length != capacidadPorSector.length || sectores.length != porcentajeAdicional.length)
            throw new IllegalArgumentException("Datos de sectores inválidos");

        this.capacidadPorSector = new HashMap<>();
        this.asientosPorFila = asientosPorFila;
        this.sectoresTeatro = new HashMap<>();
        this.reservas = new HashMap<>();

        for (int i = 0; i < sectores.length; i++) {

            Sector s = new Sector(sectores[i], capacidadPorSector[i], porcentajeAdicional[i]);
            sectoresTeatro.put(sectores[i], s);

            this.capacidadPorSector.put(sectores[i], capacidadPorSector[i]);
            this.agregarSector(s);

            reservas.put(sectores[i], new ArrayList<Integer>());
        }
    }

    @Override
    public double calcularPrecioEntrada(double precioBase, Sector sector) {
        return precioBase * (1 + sector.getPorcentajeAdicional() / 100.0);
    }

    @Override
    public boolean esSectorValido(Sector sector) {
        return getSectores().contains(sector);
    }

    public int getAsientosPorFila() {
        return asientosPorFila;
    }

    public Map<String, Sector> getSectoresTeatro() {
        return sectoresTeatro;
    }

    @Override
    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {

        if (sector == null || sector.isBlank()) {
            throw new IllegalArgumentException("Nombre de sector inválido");
        }

        if (asientos == null) {
            throw new IllegalArgumentException("La lista de asientos no puede ser nula");
        }

        if (!sectoresTeatro.containsKey(sector)) {
            throw new IllegalArgumentException("El sector no existe en este teatro");
        }

        for (Integer asiento : asientos) {
            if (asiento == null || asiento < 1 || asiento > asientosPorFila) {
                return false;
            }
        }

        ArrayList<Integer> reservados = reservas.get(sector);

        for (Integer asiento : asientos) {
            if (reservados.contains(asiento)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void liberarUbicacion(Sector sector) {
        return;
    }

    @Override
    public void liberarUbicacion(Sector sector, List<Integer> asientos) {
        if (sector == null) {
            throw new IllegalArgumentException("Sector nulo");
        }
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Lista de asientos inválida");
        }

        String nombreSector = sector.getNombre();
        if (!reservas.containsKey(nombreSector)) {
            throw new IllegalArgumentException("El sector no existe en este teatro");
        }

        List<Integer> asientosReservados = reservas.get(nombreSector);
        for (Integer asiento : asientos) {
            asientosReservados.remove(asiento);
        }
    }

    public int getCapacidadPorSector(String nombreSector) {
        return capacidadPorSector.get(nombreSector);
    }

    @Override
    public Sede clonar() {

        String[] sectoresArray = sectoresTeatro.keySet().toArray(new String[0]);
        int[] capacidadArray = new int[sectoresArray.length];
        int[] porcentajeArray = new int[sectoresArray.length];

        for (int i = 0; i < sectoresArray.length; i++) {
            Sector sector = sectoresTeatro.get(sectoresArray[i]);
            capacidadArray[i] = capacidadPorSector.get(sectoresArray[i]);
            porcentajeArray[i] = (int) sector.getPorcentajeAdicional();
        }

        return new Teatro(
                this.getNombre(),
                this.getDireccion(),
                this.getCapacidad(),
                this.asientosPorFila,
                sectoresArray,
                capacidadArray,
                porcentajeArray);
    }

    @Override
    public boolean sectorExiste(String nombreSector) {
        return sectoresTeatro.containsKey(nombreSector);
    }

    @Override
    public Sector getSector(String nombreSector) {
        return sectoresTeatro.get(nombreSector);
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

        if (!sectoresTeatro.containsKey(sector)) {
            throw new IllegalArgumentException("El sector no existe en este teatro");
        }

        ArrayList<Integer> reservados = reservas.get(sector);
        if (!reservados.contains(asiento)) {
            reservados.add(asiento);
        }
    }
}