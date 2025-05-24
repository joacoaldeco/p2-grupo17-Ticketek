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

	// private Map<String, Double> recaudacionGlobal = new HashMap<>();
	// private Map<String, Map<String, Double>> recaudacionPorSede = new
	// HashMap<>();
	// private int contadorCodigoEntrada = 0;

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
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
			int cantidadEntradas) {
		//
		return null;
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
			String sector, int[] asientos) {
		//
		return null;
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
