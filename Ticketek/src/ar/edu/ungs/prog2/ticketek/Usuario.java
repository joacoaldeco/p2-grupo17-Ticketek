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
    // Diccionario donde se almacenan las entradas compradas: clave = código de entrada, valor = objeto Entrada.
    private Map<Integer, Entrada> entradas;

    // Constructor con validaciones básicas.
    public Usuario(String email, String nombre, String apellido, String contrasenia) {
        if(email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        if(nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        if(apellido == null || apellido.trim().isEmpty())
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío.");
        if(contrasenia == null || contrasenia.trim().isEmpty())
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.entradas = new HashMap<>();
    }
    
    // Métodos getters
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
    
    /**
     * Agrega una entrada al diccionario de entradas.
     * @param codigoEntrada El código único de la entrada.
     * @param entrada La entrada a agregar.
     */
    public void agregarEntrada(Integer codigoEntrada, Entrada entrada) {
        if(codigoEntrada == null)
            throw new IllegalArgumentException("El código de entrada no puede ser nulo.");
        if(entrada == null)
            throw new IllegalArgumentException("La entrada no puede ser nula.");
        if(entradas.containsKey(codigoEntrada))
            throw new IllegalArgumentException("Ya existe una entrada con ese código.");
        entradas.put(codigoEntrada, entrada);
    }
    
    /**
     * Retorna un ArrayList con todas las entradas cuya fecha sea posterior a la fecha actual.
     * Se asume que la clase Entrada tiene el método obtenerFecha() que devuelve un LocalDate.
     * @return ArrayList de entradas futuras.
     */
    public ArrayList<Entrada> listarEntradasFuturas() {
        ArrayList<Entrada> entradasFuturas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (Entrada entrada : entradas.values()) {
            if (entrada.obtenerFecha() != null && hoy.isBefore(entrada.obtenerFecha())) {
                entradasFuturas.add(entrada);
            }
        }
        return entradasFuturas;
    }
    
    /**
     * Retorna un ArrayList con todas las entradas registradas.
     * @return ArrayList de todas las entradas.
     */
    public ArrayList<Entrada> listarEntradas() {
        return new ArrayList<>(entradas.values());
    }
    
    /**
     * Cancela (anula) una entrada cuyo código sea el indicado,
     * siempre y cuando la contraseña ingresada sea correcta.
     * Opcionalmente, se puede verificar que la fecha de la función no haya pasado.
     * @param codigoEntrada El código de la entrada a anular.
     * @param contrasena La contraseña para autenticar al usuario.
     */
    public void anularEntrada(Integer codigoEntrada, String contrasena) {
        if (!this.contrasenia.equals(contrasena))
            throw new IllegalArgumentException("Contraseña incorrecta.");
        if (!entradas.containsKey(codigoEntrada))
            throw new IllegalArgumentException("No existe una entrada con el código proporcionado.");
        
        Entrada entrada = entradas.get(codigoEntrada);
        LocalDate hoy = LocalDate.now();
        // Se asume que no se pueden anular entradas cuyo evento ya haya ocurrido.
        if (entrada.obtenerFecha() != null && !hoy.isBefore(entrada.obtenerFecha()))
            throw new IllegalArgumentException("La entrada ya venció y no se puede anular.");
        
        entradas.remove(codigoEntrada);
    }
    
    /**
     * Verifica y retorna la ubicación de la entrada cuyo código se indica,
     * luego de autenticar usando la contraseña del usuario.
     * Se asume que la clase Entrada tiene el método getUbicacion() que retorna un String.
     * @param codigoEntrada El código de la entrada.
     * @param contrasena La contraseña para autenticar al usuario.
     * @return Un String que describe la ubicación asignada a la entrada.
     */
    public String verificarUbicacionEntrada(Integer codigoEntrada, String contrasena) {
        if (!this.contrasenia.equals(contrasena))
            throw new IllegalArgumentException("Contraseña incorrecta.");
        if (!entradas.containsKey(codigoEntrada))
            throw new IllegalArgumentException("No existe una entrada con el código proporcionado.");
        
        Entrada entrada = entradas.get(codigoEntrada);
        return entrada.getUbicacion();
    }
}