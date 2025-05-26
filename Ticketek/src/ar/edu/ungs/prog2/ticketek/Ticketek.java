package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketek implements ITicketek {

	private Map<String, Sede> sedes = new HashMap<>();
	private Map<String, Usuario> usuarios = new HashMap<>();
	private Map<String, Espectaculo> espectaculos = new HashMap<>();

	 private Map<String, Double> recaudacionGlobal = new HashMap<>();
	 private Map<String, Map<String, Double>> recaudacionPorSede = new
	 HashMap<>();
	 private int contadorCodigoEntrada = 0;

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima) {
		if (sedes.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe una sede con ese nombre.");

		Sede nuevaSede = new Estadio(nombre, direccion, capacidadMaxima);
		sedes.put(nombre, nuevaSede);
	}

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			String[] sectores, int[] capacidad, int[] porcentajeAdicional) {

		if (sedes.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe una sede con ese nombre.");

		Sede nuevaSede = new Teatro(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad,
				porcentajeAdicional);
		sedes.put(nombre, nuevaSede);
	}

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			int cantidadPuestos, double precioConsumicion,
			String[] sectores, int[] capacidad, int[] porcentajeAdicional) {

		if (sedes.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe una sede con ese nombre.");

		Sede nuevaSede = new MiniEstadio(nombre, direccion, capacidadMaxima, asientosPorFila,
				cantidadPuestos, precioConsumicion,
				sectores, capacidad, porcentajeAdicional);

		sedes.put(nombre, nuevaSede);
	}

	@Override
	public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {
		if (usuarios.containsKey(email))
			throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico.");

		Usuario usuario = new Usuario(email, nombre, apellido, contrasenia);

		usuarios.put(email, usuario);
	}

	@Override
	public void registrarEspectaculo(String nombre) {
		if (espectaculos.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe un espectáculo con ese nombre.");

		Espectaculo espectaculo = new Espectaculo(nombre);

		espectaculos.put(nombre, espectaculo);

	}

	@Override
	public void agregarFuncion(String nombreEspectaculo, String fecha, String sede, double precioBase) {
		if (!espectaculos.containsKey(nombreEspectaculo))
			throw new IllegalArgumentException("Espectáculo no encontrado");

		if (!sedes.containsKey(sede))
			throw new IllegalArgumentException("Sede no encontrada");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate fechaFuncion = LocalDate.parse(fecha, formato);

		// Se valida que no haya otro espectáculo ese día en esa sede

		for (Map.Entry<String, Espectaculo> espectaculo : espectaculos.entrySet()) {
			String nombreOtroEspectaculo = espectaculo.getKey();
			Espectaculo otroEspectaculo = espectaculo.getValue();

			if (!nombreOtroEspectaculo.equals(nombreEspectaculo)) {
				Funcion funcion = otroEspectaculo.getFunciones().get(fechaFuncion);
				if (funcion != null && funcion.getSede().getNombre().equals(sede)) {
					throw new IllegalArgumentException("Ya hay otro espectáculo ese día en esa sede.");
				}
			}
		}

		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		Sede sedeObj = sedes.get(sede);

		espectaculo.agregarFuncion(fechaFuncion, sedeObj, precioBase);
	}
	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia, int cantidadEntradas) {
	    // Validar usuario y credenciales
	    Usuario usuario = usuarios.get(email);
	    if (usuario == null)
	        throw new IllegalArgumentException("Usuario no registrado.");
	    if (!usuario.getContrasenia().equals(contrasenia))
	        throw new IllegalArgumentException("Contraseña inválida.");

	    // Validar existencia del espectáculo
	    Espectaculo esp = espectaculos.get(nombreEspectaculo);
	    if (esp == null)
	        throw new IllegalArgumentException("Espectáculo no registrado.");

	    // Convertir el string de fecha a LocalDate
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
	    LocalDate fechaFuncion;
	    try {
	        fechaFuncion = LocalDate.parse(fecha, formatter);
	    } catch (DateTimeParseException ex) {
	        throw new IllegalArgumentException("Formato de fecha inválido.");
	    }

	    // Obtener la función programada para esa fecha
	    Funcion funcion = esp.getFunciones().get(fechaFuncion);
	    if (funcion == null)
	        throw new IllegalArgumentException("No hay función en esa fecha.");

	    // Verificar que la sede sea no numerada (por ejemplo, instancias de Estadio)
	    Sede sedeFuncion = funcion.getSede();
	    if (!(sedeFuncion instanceof Estadio))
	        throw new IllegalArgumentException("La función se realiza en una sede numerada. Use el método correspondiente.");

	    // Lista para ir acumulando las entradas vendidas (para devolverlas)
	    List<IEntrada> entradasVendidas = new ArrayList<>();

	    // Vender las entradas una por una
	    for (int i = 0; i < cantidadEntradas; i++) {
	        // Generar código único para la nueva entrada
	        contadorCodigoEntrada++;

	        // Calcular el precio de la entrada (método asumido en Funcion)
	        double precio = funcion.calcularPrecioEntrada();

	        // Crear la entrada. Para sedes no numeradas, el sector puede ser nulo o asignar un valor por defecto.
	        IEntrada entrada = new Entrada(contadorCodigoEntrada, esp, funcion, null, precio);

	        // Registrar la entrada en la función para actualizar el recuento de ventas
	        funcion.agregarEntradaVendida(entrada);

	        // Agregar la entrada individualmente al usuario (almacenándola en el diccionario)
	        usuario.agregarEntrada(contadorCodigoEntrada, (Entrada) entrada);

	        // También se agrega a la lista que se devolverá
	        entradasVendidas.add(entrada);
	    }

	    // Actualizar la recaudación global y por sede
	    double totalVenta = 0;
	    for (IEntrada entrada : entradasVendidas) {
	        totalVenta += entrada.obtenerPrecioFinal();
	    }
	    recaudacionGlobal.put(nombreEspectaculo, recaudacionGlobal.getOrDefault(nombreEspectaculo, 0.0) + totalVenta);
	    Map<String, Double> recaudacionSede = recaudacionPorSede.getOrDefault(nombreEspectaculo, new HashMap<>());
	    recaudacionSede.put(sedeFuncion.getNombre(), recaudacionSede.getOrDefault(sedeFuncion.getNombre(), 0.0) + totalVenta);
	    recaudacionPorSede.put(nombreEspectaculo, recaudacionSede);

	    return entradasVendidas;
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia, String sector, int[] asientos) {
	    // Validar usuario y credenciales
	    Usuario usuario = usuarios.get(email);
	    if (usuario == null)
	        throw new IllegalArgumentException("Usuario no registrado.");
	    if (!usuario.getContrasenia().equals(contrasenia))
	        throw new IllegalArgumentException("Contraseña inválida.");

	    // Validar existencia del espectáculo
	    Espectaculo esp = espectaculos.get(nombreEspectaculo);
	    if (esp == null)
	        throw new IllegalArgumentException("Espectáculo no registrado.");

	    // Convertir la fecha a LocalDate
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
	    LocalDate fechaFuncion;
	    try {
	        fechaFuncion = LocalDate.parse(fecha, formatter);
	    } catch (DateTimeParseException ex) {
	        throw new IllegalArgumentException("Formato de fecha inválido.");
	    }

	    // Obtener la función para esa fecha
	    Funcion funcion = esp.getFunciones().get(fechaFuncion);
	    if (funcion == null)
	        throw new IllegalArgumentException("No hay función en esa fecha.");

	    // Verificar que la sede sea numerada (por ejemplo, instancias de Teatro o MiniEstadio)
	    Sede sedeFuncion = funcion.getSede();
	    if (!(sedeFuncion instanceof Teatro || sedeFuncion instanceof MiniEstadio))
	        throw new IllegalArgumentException("La función no se realiza en una sede numerada. Use el otro método de venta.");

	    // Validar que el sector exista en la sede
	    if (!sedeFuncion.sectorExiste(sector))
	        throw new IllegalArgumentException("Sector no existente en la sede.");
	    
	    // Validar que los asientos solicitados estén disponibles
	    if (!sedeFuncion.asientosDisponibles(sector, asientos))
	        throw new IllegalArgumentException("Algunos asientos solicitados no están disponibles.");

	    // Lista para ir acumulando las entradas vendidas
	    List<IEntrada> entradasVendidas = new ArrayList<>();

	    // Vender entradas una por una, para cada número de asiento indicado
	    for (int asiento : asientos) {
	        contadorCodigoEntrada++;

	        // Calcular el precio, que puede variar según el sector (método sobrecargado de Funcion)
	        double precio = funcion.calcularPrecioEntrada(sector);

	        // Crear la entrada. Se asume que la sede puede proveer el objeto Sector mediante getSector(sector)
	        IEntrada entrada = new Entrada(contadorCodigoEntrada, esp, funcion, sedeFuncion.getSector(sector), precio);

	        // Marcar el asiento como ocupado en la sede
	        sedeFuncion.asignarAsiento(sector, asiento);

	        // Registrar la entrada en la función
	        funcion.agregarEntradaVendida(entrada);

	        // Agregar la entrada individualmente en el diccionario del usuario
	        usuario.agregarEntrada(contadorCodigoEntrada, (Entrada) entrada);

	        // Agregar la entrada a la lista que se retornará
	        entradasVendidas.add(entrada);
	    }

	    // Actualizar la recaudación global y por sede
	    double totalVenta = 0;
	    for (IEntrada entrada : entradasVendidas) {
	        totalVenta += entrada.obtenerPrecioFinal();
	    }
	    recaudacionGlobal.put(nombreEspectaculo, recaudacionGlobal.getOrDefault(nombreEspectaculo, 0.0) + totalVenta);
	    Map<String, Double> recaudacionSede = recaudacionPorSede.getOrDefault(nombreEspectaculo, new HashMap<>());
	    recaudacionSede.put(sedeFuncion.getNombre(), recaudacionSede.getOrDefault(sedeFuncion.getNombre(), 0.0) + totalVenta);
	    recaudacionPorSede.put(nombreEspectaculo, recaudacionSede);

	    return entradasVendidas;
	}
	@Override
	public String listarFunciones(String nombreEspectaculo) {
		//
		return null;
	}

	@Override
	public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
		//
		return null;
	}

	@Override
	public List<IEntrada> listarEntradasFuturas(String email, String contrasenia) {
		//
		return null;
	}

	@Override
	public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
		//
		return null;
	}

	@Override
	public boolean anularEntrada(IEntrada entrada, String contrasenia) {
		//
		return false;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
		//
		return null;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
		//
		return null;
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha) {
		//
		return 0;
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
		//
		return 0;
	}

	@Override
	public double totalRecaudado(String nombreEspectaculo) {
		//
		return 0;
	}

	@Override
	public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
		//
		return 0;
	}

}
