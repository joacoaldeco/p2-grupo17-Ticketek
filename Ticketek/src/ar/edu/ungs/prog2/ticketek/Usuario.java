package ar.edu.ungs.prog2.ticketek;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String email;
    private String nombre;
    private String apellido;
    private String contrasenia;
    private Map<Integer, Entrada> entradas = new HashMap<>();

    public Usuario(String email, String nombre, String apellido, String contrasenia) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("Apellido inválido.");
        }
        if (contrasenia == null || contrasenia.isBlank()) {
            throw new IllegalArgumentException("Contraseña inválida.");
        }

        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenia = contrasenia;
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
}
