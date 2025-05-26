package ar.edu.ungs.prog2.ticketek;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class Teatro extends Sede {

    private int asientosPorFila;
    private Map<String, Sector> sectoresTeatro;
    // Nuevo: mapa para llevar el registro de reservas por sector (clave: nombre del sector,
    // valor: lista de números de asiento reservados).
    private Map<String, ArrayList<Integer>> reservas;


    public Teatro(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
                  String[] sectores, int[] capacidadPorSector, int[] porcentajeAdicional) {
        super(nombre, direccion, capacidadMaxima);
        if (asientosPorFila <= 0)
            throw new IllegalArgumentException("Asientos por fila inválido.");
        if (sectores == null || capacidadPorSector == null || porcentajeAdicional == null)
            throw new IllegalArgumentException("Parámetros de sectores inválidos.");
        if (sectores.length != capacidadPorSector.length || sectores.length != porcentajeAdicional.length)
            throw new IllegalArgumentException("Datos de sectores inválidos.");

        this.asientosPorFila = asientosPorFila;
        this.sectoresTeatro = new HashMap<>();
        this.reservas = new HashMap<>();

        // Crear cada sector y agregarlo tanto al mapa interno como a la lista de sectores de la Sede.
        for (int i = 0; i < sectores.length; i++) {
            Sector s = new Sector(sectores[i], capacidadPorSector[i], porcentajeAdicional[i]);
            sectoresTeatro.put(sectores[i], s);
            this.agregarSector(s);
            reservas.put(sectores[i], new ArrayList<Integer>());
        }
    }

    @Override
    public double calcularPrecioEntrada(Double precioBase, Sector sector) {
        if (!esSectorValido(sector)) {
            throw new IllegalArgumentException("Sector inválido para este teatro.");
        }
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
            throw new IllegalArgumentException("Nombre de sector inválido.");
        }
        if (asientos == null) {
            throw new IllegalArgumentException("La lista de asientos no puede ser nula.");
        }
        if (!sectoresTeatro.containsKey(sector)) {
            throw new IllegalArgumentException("El sector no existe en este teatro.");
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
        if (sector == null) {
            throw new IllegalArgumentException("Sector nulo.");
        }
        String nombreSector = sector.getNombre();
        if (!reservas.containsKey(nombreSector)) {
            throw new IllegalArgumentException("El sector no existe en este teatro.");
        }
        // Se libera (se borra) el registro de asientos reservados para el sector.
        reservas.put(nombreSector, new ArrayList<Integer>());
    }
}