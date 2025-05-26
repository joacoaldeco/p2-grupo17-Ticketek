package ar.edu.ungs.prog2.ticketek;

public class Sector {
    private int numeroAsiento;
    private int numeroFila;
    private String ubicacion;
    private boolean ocupado;
    private String tipo;
    private double porcentajeAdicional;

    /**
     * Constructor detallado para crear un Sector con la información completa.
     *
     * @param numeroAsiento           Número específico de asiento.
     * @param numeroFila              Número de la fila.
     * @param ubicacion               Cadena que identifica la ubicación (por ejemplo, "Central", "Lateral").
     * @param ocupado                 Indica si el asiento está ocupado.
     * @param tipo                    Tipo de asiento (puede ser "VIP", "General", etc.).
     * @param porcentajeAdicional     Porcentaje de incremento a aplicar sobre el precio base.
     * @throws IllegalArgumentException Si algún parámetro es inválido.
     */
    public Sector(int numeroAsiento, int numeroFila, String ubicacion, boolean ocupado, String tipo, double porcentajeAdicional) {
        if (numeroAsiento <= 0)
            throw new IllegalArgumentException("Número de asiento inválido.");
        if (numeroFila <= 0)
            throw new IllegalArgumentException("Número de fila inválido.");
        if (ubicacion == null || ubicacion.isBlank())
            throw new IllegalArgumentException("Ubicación inválida.");
        if (tipo == null || tipo.isBlank())
            throw new IllegalArgumentException("Tipo inválido.");
        if (porcentajeAdicional < 0)
            throw new IllegalArgumentException("El porcentaje de incremento no puede ser negativo.");
        
        this.numeroAsiento = numeroAsiento;
        this.numeroFila = numeroFila;
        this.ubicacion = ubicacion;
        this.ocupado = ocupado;
        this.tipo = tipo;
        this.porcentajeAdicional = porcentajeAdicional;
    }

    /**
     * Constructor de conveniencia para crear un Sector "agrupado".
     * Se utiliza el valor de 'nombre' como ubicación, se asignan valores por defecto para
     * número de asiento (1), número de fila (1), ocupado (false) y tipo ("General").
     *
     * @param nombre               Nombre (y ubicación) del sector.
     * @param capacidad            Capacidad del sector (valor recibido, que en este constructor no se almacena).
     * @param porcentajeAdicional  Porcentaje de incremento a aplicar sobre el precio base.
     */
    public Sector(String nombre, int capacidad, double porcentajeAdicional) {
        // En este constructor, se usan valores por defecto para los parámetros que no se reciben.
        this(1, 1, nombre, false, "General", porcentajeAdicional);
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public int getNumeroFila() {
        return numeroFila;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPorcentajeAdicional() {
        return porcentajeAdicional;
    }
    
    /**
     * Devuelve el "nombre" del sector. En esta implementación se utiliza la propiedad 'ubicacion'
     * para identificar al sector de forma simplificada.
     *
     * @return La ubicación o nombre del sector.
     */
    public String getNombre() {
        return this.ubicacion;
    }

    /**
     * Calcula el precio adicional para el asiento a partir del precio base.
     * La fórmula es: precioBase * (porcentajeAdicional / 100)
     *
     * @param precioBase El precio base de la entrada.
     * @return El incremento a aplicar.
     */
    public double calcularPrecioAdicional(Double precioBase) {
        if (precioBase == null || precioBase < 0) {
            throw new IllegalArgumentException("Precio base inválido.");
        }
        return precioBase * (porcentajeAdicional / 100.0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Sector))
            return false;
        Sector other = (Sector) obj;
        return this.numeroAsiento == other.numeroAsiento &&
               this.numeroFila == other.numeroFila &&
               this.ubicacion.equals(other.ubicacion);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + numeroAsiento;
        result = 31 * result + numeroFila;
        result = 31 * result + ubicacion.hashCode();
        return result;
    }
}