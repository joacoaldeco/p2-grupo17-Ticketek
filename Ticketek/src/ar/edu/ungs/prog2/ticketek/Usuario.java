package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String email;
    private String nombre;
    private String apellido;
    private String contrasenia;
    private Map<Integer, Entrada> entradas;

    public Usuario(String email, String nombre, String apellido, String contrasenia) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        if (apellido == null || apellido.trim().isEmpty())
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío.");
        if (contrasenia == null || contrasenia.trim().isEmpty())
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");

        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.entradas = new HashMap<>();
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public Map<Integer, Entrada> getEntradas() {
        return entradas;
    }

    public void agregarEntrada(Integer codigoEntrada, Entrada entrada) {
        if (codigoEntrada == null)
            throw new IllegalArgumentException("El código de entrada no puede ser nulo.");
        if (entrada == null)
            throw new IllegalArgumentException("La entrada no puede ser nula.");
        if (entradas.containsKey(codigoEntrada))
            throw new IllegalArgumentException("Ya existe una entrada con ese código.");
        entradas.put(codigoEntrada, entrada);
    }

    public ArrayList<Entrada> listarEntradasFuturas() {
        ArrayList<Entrada> entradasFuturas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (Entrada entrada : entradas.values()) {
            LocalDate fechaDate = entrada.obtenerFecha();
            if (fechaDate != null) {
                if (hoy.isBefore(fechaDate)) {
                    entradasFuturas.add(entrada);
                }
            }
        }
        return entradasFuturas;
    }

    public ArrayList<Entrada> listarEntradas() {
        return new ArrayList<>(entradas.values());
    }

    public String verificarUbicacionEntrada(Integer codigoEntrada, String contrasena) {
        if (!this.contrasenia.equals(contrasena))
            throw new IllegalArgumentException("Contraseña incorrecta.");
        if (!entradas.containsKey(codigoEntrada))
            throw new IllegalArgumentException("No existe una entrada con el código proporcionado.");

        Entrada entrada = entradas.get(codigoEntrada);
        return entrada.ubicacion();
    }

    public boolean tieneEntrada(int codigo) {
        return entradas.containsKey(codigo);
    }

    public void eliminarEntrada(int codigo) {
        entradas.remove(codigo);
    }

    public void anularEntrada(Integer codigoEntrada, String contrasenia) {
        if (!this.contrasenia.equals(contrasenia))
            throw new IllegalArgumentException("Contraseña inválida.");

        Entrada entrada = entradas.get(codigoEntrada);
        if (entrada == null)
            throw new IllegalArgumentException("Entrada no encontrada.");

        entrada.getFuncion().liberarUbicacion(entrada.getSector());

        entradas.remove(codigoEntrada);
        entrada.getFuncion().obtenerEntradasVendidas().remove(entrada);
    }
}