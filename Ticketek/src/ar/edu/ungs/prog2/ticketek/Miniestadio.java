package ar.edu.ungs.prog2.ticketek;

public class MiniEstadio extends Teatro {

    private int cantidadPuestos;
    private double precioConsumicion;

    /**
     * Construye un MiniEstadio a partir de los parámetros especificados.
     * Se hereda toda la información de Teatro y además se reciben:
     * - cantidadPuestos: La cantidad de puestos de venta disponibles.
     * - precioConsumicion: El valor de la consumición libre (o adicional) a sumar al precio base.
     * 
     * Los arreglos de sectores, capacidades y porcentajes se usan para crear internamente los objetos Sector,
     * gracias al constructor de Teatro.
     * 
     * @param nombre             Nombre de la sede.
     * @param direccion          Dirección de la sede.
     * @param capacidadMaxima    Capacidad máxima de la sede.
     * @param asientosPorFila    Número de asientos por fila.
     * @param cantidadPuestos    Cantidad de puestos disponibles.
     * @param precioConsumicion  Precio de consumición (consumición libre).
     * @param sectores           Arreglo de nombres de sectores.
     * @param capacidad          Arreglo de capacidades de cada sector.
     * @param porcentajeAdicional Arreglo con el porcentaje de incremento para cada sector.
     * @throws IllegalArgumentException Si la cantidad de puestos o el precio de consumición son inválidos.
     */
    public MiniEstadio(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
            int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
        super(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional);

        if (cantidadPuestos <= 0)
            throw new IllegalArgumentException("Cantidad de puestos inválida");
        if (precioConsumicion <= 0)
            throw new IllegalArgumentException("Precio de consumición inválido");

        this.cantidadPuestos = cantidadPuestos;
        this.precioConsumicion = precioConsumicion;
    }

    public int getCantidadPuestos() {
        return cantidadPuestos;
    }

    public double getPrecioConsumicion() {
        return precioConsumicion;
    }

    /**
     * Calcula el precio final de la entrada para el MiniEstadio.
     * Se basa en el cálculo realizado en Teatro y se le suma el valor de consumición.
     * 
     * @param precioBase El precio base de la entrada.
     * @param sector     El sector para el cual se desea calcular el precio.
     * @return El precio final de la entrada.
     * @throws IllegalArgumentException Si el sector no es válido para este MiniEstadio.
     */
    @Override
    public double calcularPrecioEntrada(Double precioBase, Sector sector) {
        // Se usa la implementación de Teatro para obtener el precio en función del porcentaje
        double precio = super.calcularPrecioEntrada(precioBase, sector);
        // Se suma la consumición libre al precio calculado.
        return precio + this.precioConsumicion;
    }
}
