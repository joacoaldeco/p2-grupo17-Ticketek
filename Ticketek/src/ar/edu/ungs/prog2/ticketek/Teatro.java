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

    /**
     * Construye un teatro a partir de los parámetros especificados, incluyendo el registro de sectores.
     * 
     * @param nombre              Nombre del teatro.
     * @param direccion           Dirección del teatro.
     * @param capacidadMaxima     Capacidad total del teatro.
     * @param asientosPorFila     Número de asientos por fila.
     * @param sectores            Arreglo de nombres de sectores.
     * @param capacidadPorSector  Arreglo de capacidades asignadas para cada sector.
     * @param porcentajeAdicional Arreglo con el porcentaje de incremento para cada sector.
     * @throws IllegalArgumentException Si alguno de los parámetros es inválido o los arreglos no tienen la misma longitud.
     */
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
            // Se asume que la clase Sector tiene un constructor: Sector(String nombre, int capacidad, int porcentajeAdicional)
            Sector s = new Sector(sectores[i], capacidadPorSector[i], porcentajeAdicional[i]);
            sectoresTeatro.put(sectores[i], s);
            this.agregarSector(s);
            // Inicializamos la lista de reservas para cada sector.
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

    /**
     * Implementa el método abstracto para verificar la disponibilidad de asientos en un sector.
     * Se verifica lo siguiente:
     * - El sector (por nombre) debe existir en este teatro.
     * - Cada número de asiento solicitado debe estar entre 1 y asientosPorFila.
     * - Ninguno de esos asientos debe estar ya reservado.
     * 
     * @param sector   Nombre del sector.
     * @param asientos Lista de números de asientos a verificar.
     * @return true si todos los asientos solicitados están disponibles; false en caso contrario.
     */
    @Override
    public boolean verificarDisponibilidad(String sector, ArrayList<Integer> asientos) {
        if (sector == null || sector.isBlank()) {
            throw new IllegalArgumentException("Nombre de sector inválido.");
        }
        if (asientos == null) {
            throw new IllegalArgumentException("La lista de asientos no puede ser nula.");
        }
        // Verificar que el sector exista.
        if (!sectoresTeatro.containsKey(sector)) {
            throw new IllegalArgumentException("El sector no existe en este teatro.");
        }
        // Verificar que cada número de asiento esté en el rango permitido.
        for (Integer asiento : asientos) {
            if (asiento == null || asiento < 1 || asiento > asientosPorFila) {
                return false;
            }
        }
        // Verificar que ningún asiento solicitado ya esté reservado.
        ArrayList<Integer> reservados = reservas.get(sector);
        for (Integer asiento : asientos) {
            if (reservados.contains(asiento)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implementa el método abstracto para liberar la ubicación en el sector.
     * En este ejemplo se "liberan" todas las reservas del sector, quedando disponibles para futuras ventas.
     * 
     * @param sector El objeto Sector para el cual se liberarán las ubicaciones.
     */
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