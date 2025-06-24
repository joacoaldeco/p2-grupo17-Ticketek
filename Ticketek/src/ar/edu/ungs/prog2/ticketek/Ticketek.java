package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketek implements ITicketek {

	private Map<String, Sede> sedes = new HashMap<>();
	private Map<String, Usuario> usuarios = new HashMap<>();
	private Map<Integer, Usuario> entradaUsuarioMap = new HashMap<>();

	private Map<String, Espectaculo> espectaculos = new HashMap<>();

	private Map<String, Double> recaudacionGlobal = new HashMap<>();
	private Map<String, Map<String, Double>> recaudacionPorSede = new HashMap<>();
	private int contadorCodigoEntrada = 0;

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima) {

		if (sedes.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe una sede con ese nombre");

		Sede nuevaSede = new Estadio(nombre, direccion, capacidadMaxima);
		sedes.put(nombre, nuevaSede);
	}

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			String[] sectores, int[] capacidad, int[] porcentajeAdicional) {

		if (sedes.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe una sede con ese nombre");

		Sede nuevaSede = new Teatro(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad,
				porcentajeAdicional);
		sedes.put(nombre, nuevaSede);
	}

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			int cantidadPuestos, double precioConsumicion,
			String[] sectores, int[] capacidad, int[] porcentajeAdicional) {

		if (sedes.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe una sede con ese nombre");

		Sede nuevaSede = new MiniEstadio(nombre, direccion, capacidadMaxima, asientosPorFila,
				cantidadPuestos, precioConsumicion,
				sectores, capacidad, porcentajeAdicional);

		sedes.put(nombre, nuevaSede);
	}

	@Override
	public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {

		if (usuarios.containsKey(email))
			throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico");

		Usuario usuario = new Usuario(email, nombre, apellido, contrasenia);

		usuarios.put(email, usuario);
	}

	@Override
	public void registrarEspectaculo(String nombre) {

		if (espectaculos.containsKey(nombre))
			throw new IllegalArgumentException("Ya existe un espectáculo con ese nombre");

		Espectaculo espectaculo = new Espectaculo(nombre);

		espectaculos.put(nombre, espectaculo);

	}

	@Override
	public void agregarFuncion(String nombreEspectaculo, String fecha, String sede, double precioBase) {

		if (!espectaculos.containsKey(nombreEspectaculo))
			throw new IllegalArgumentException("Espectáculo no encontrado");

		if (!sedes.containsKey(sede))
			throw new IllegalArgumentException("Sede no encontrada");

		LocalDate fechaParseada = this.parsearFecha(fecha);

		for (Map.Entry<String, Espectaculo> espectaculo : espectaculos.entrySet()) {

			String nombreOtroEspectaculo = espectaculo.getKey();
			Espectaculo otroEspectaculo = espectaculo.getValue();

			if (!nombreOtroEspectaculo.equals(nombreEspectaculo)) {

				Funcion funcion = otroEspectaculo.getFunciones().get(fechaParseada);

				if (funcion != null && funcion.getSede().getNombre().equals(sede)) {
					throw new IllegalArgumentException("Ya hay otro espectáculo ese día en esa sede");
				}
			}
		}

		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);

		Sede sedeObj = sedes.get(sede);

		espectaculo.agregarFuncion(sedeObj, fechaParseada, precioBase);
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
			int cantidadEntradas) {

		Usuario usuario = usuarios.get(email);

		if (usuario == null)
			throw new IllegalArgumentException("Usuario no registrado");

		usuario.credencialesCorrectas(contrasenia);

		Espectaculo esp = espectaculos.get(nombreEspectaculo);

		if (esp == null)
			throw new IllegalArgumentException("Espectáculo no registrado");

		LocalDate fechaParseada = this.parsearFecha(fecha);

		Funcion funcion = esp.getFunciones().get(fechaParseada);

		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha");

		Sede sedeFuncion = funcion.getSede();

		List<IEntrada> entradasVendidas = new ArrayList();

		for (int i = 0; i < cantidadEntradas; i++) {
			contadorCodigoEntrada++;

			double precio = funcion.calcularPrecioEntrada();

			Sector sectorGeneral = new Sector("CAMPO", 0, 0.0);

			Entrada entrada = new Entrada(
					contadorCodigoEntrada,
					esp.getNombre(),
					fechaParseada,
					sectorGeneral,
					sedeFuncion.getNombre(),
					precio);

			funcion.agregarEntradaVendida(entrada);

			usuario.agregarEntrada(contadorCodigoEntrada, (Entrada) entrada);
			entradaUsuarioMap.put(contadorCodigoEntrada, usuario);

			entradasVendidas.add(entrada);
		}

		double totalVenta = 0;

		for (IEntrada entrada : entradasVendidas) {
			totalVenta += entrada.obtenerPrecioFinal();
		}

		recaudacionGlobal.put(nombreEspectaculo, recaudacionGlobal.getOrDefault(nombreEspectaculo, 0.0) + totalVenta);

		Map<String, Double> recaudacionSede = recaudacionPorSede.getOrDefault(nombreEspectaculo, new HashMap<>());

		recaudacionSede.put(sedeFuncion.getNombre(),
				recaudacionSede.getOrDefault(sedeFuncion.getNombre(), 0.0) + totalVenta);

		recaudacionPorSede.put(nombreEspectaculo, recaudacionSede);

		return entradasVendidas;
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
			String sector, int[] asientos) {

		Usuario usuario = usuarios.get(email);

		if (usuario == null)
			throw new IllegalArgumentException("Usuario no registrado");

		usuario.credencialesCorrectas(contrasenia);

		Espectaculo esp = espectaculos.get(nombreEspectaculo);

		if (esp == null)
			throw new IllegalArgumentException("Espectáculo no registrado");

		LocalDate fechaParseada = this.parsearFecha(fecha);

		Funcion funcion = esp.getFunciones().get(fechaParseada);

		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha");

		Sede sedeFuncion = funcion.getSede();

		if (!sedeFuncion.sectorExiste(sector))
			throw new IllegalArgumentException("Sector no existente en la sede");

		if (!sedeFuncion.asientosDisponibles(sector, asientos))
			throw new IllegalArgumentException("Asientos no están disponibles");

		List<IEntrada> entradasVendidas = new ArrayList<>();

		List<Integer> listaAsientos = new ArrayList<>();

		for (int asiento : asientos) {
			contadorCodigoEntrada++;

			Sector sectorElegido = sedeFuncion.getSector(sector);

			double precio = sedeFuncion.calcularPrecioEntrada(funcion.calcularPrecioEntrada(), sectorElegido);

			sedeFuncion.asignarAsiento(sector, asiento);
			listaAsientos.add(asiento);

			Entrada entrada = new Entrada(
					contadorCodigoEntrada,
					esp.getNombre(),
					fechaParseada,
					sectorElegido,
					sedeFuncion.getNombre(),
					precio,
					(List<Integer>) listaAsientos);

			funcion.agregarEntradaVendida(entrada);

			usuario.agregarEntrada(contadorCodigoEntrada, (Entrada) entrada);
			entradaUsuarioMap.put(contadorCodigoEntrada, usuario);

			entradasVendidas.add(entrada);
		}

		double totalVenta = 0;

		for (IEntrada entrada : entradasVendidas) {
			totalVenta += entrada.obtenerPrecioFinal();
		}

		recaudacionGlobal.put(nombreEspectaculo, recaudacionGlobal.getOrDefault(nombreEspectaculo, 0.0) + totalVenta);

		Map<String, Double> recaudacionSede = recaudacionPorSede.getOrDefault(nombreEspectaculo, new HashMap<>());

		recaudacionSede.put(sedeFuncion.getNombre(),
				recaudacionSede.getOrDefault(sedeFuncion.getNombre(), 0.0) + totalVenta);

		recaudacionPorSede.put(nombreEspectaculo, recaudacionSede);

		return entradasVendidas;
	}

	@Override
	public String listarFunciones(String nombreEspectaculo) {

		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);

		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado");

		StringBuilder sb = new StringBuilder();

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");

		List<Map.Entry<LocalDate, Funcion>> funcionesOrdenadas = new ArrayList<>(espectaculo.getFunciones().entrySet());
		funcionesOrdenadas.sort(Map.Entry.comparingByKey());

		for (Map.Entry<LocalDate, Funcion> entry : funcionesOrdenadas) {

			LocalDate fecha = entry.getKey();
			Funcion funcion = entry.getValue();
			Sede sede = funcion.getSede();

			sb.append(" - (").append(fecha.format(formato)).append(") ").append(sede.getNombre()).append(" - ");

			if (sede instanceof Estadio) {
				int entradas = funcion.obtenerEntradasVendidas().size();
				sb.append(entradas).append("/").append(sede.getCapacidad());
			} else {
				Map<String, Integer> vendidasPorSector = new HashMap<>();

				for (Entrada entrada : funcion.obtenerEntradasVendidas()) {
					String nombreSector = entrada.getSector().getNombre();
					vendidasPorSector.put(nombreSector, vendidasPorSector.getOrDefault(nombreSector, 0) + 1);
				}

				String[] ordenSectores = { "VIP", "Comun", "Baja", "Alta" };
				List<String> partesSector = new ArrayList<>();

				for (String nombreSector : ordenSectores) {
					boolean sectorExiste = false;
					for (Sector sector : sede.getSectores()) {
						if (sector.getNombre().equals(nombreSector)) {
							sectorExiste = true;
							break;
						}
					}

					if (sectorExiste) {
						int vendidas = vendidasPorSector.getOrDefault(nombreSector, 0);
						int capacidad = ((Teatro) sede).getCapacidadPorSector(nombreSector);
						partesSector.add(nombreSector + ": " + vendidas + "/" + capacidad);
					}
				}

				sb.append(String.join(" | ", partesSector));
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado");

		List<IEntrada> resultado = new ArrayList<>();

		for (Funcion funcion : espectaculo.getFunciones().values()) {
			resultado.addAll(funcion.obtenerEntradasVendidas());
		}

		return resultado;
	}

	@Override
	public List<IEntrada> listarEntradasFuturas(String email, String contrasenia) {

		Usuario usuario = usuarios.get(email);

		if (usuario == null)
			throw new IllegalArgumentException("Usuario no registrado");

		usuario.credencialesCorrectas(contrasenia);

		Map<Integer, Entrada> entradas = usuario.getEntradas();
		List<IEntrada> resultado = new ArrayList<>();

		for (Entrada entrada : entradas.values()) {
			if (entrada.obtenerFecha().isAfter(LocalDate.now())) {
				resultado.add(entrada);
			}
		}

		return resultado;
	}

	@Override
	public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {

		Usuario usuario = usuarios.get(email);

		if (usuario == null)
			throw new IllegalArgumentException("Usuario no registrado");

		usuario.credencialesCorrectas(contrasenia);

		Map<Integer, Entrada> entradasMap = usuario.getEntradas();
		return new ArrayList<IEntrada>(entradasMap.values());
	}

	@Override
	public boolean anularEntrada(IEntrada entrada, String contrasenia) {

		if (entrada == null)
			throw new IllegalArgumentException("Entrada inválida");

		if (entrada.obtenerFecha().isBefore(LocalDate.now())) {
			return false;
		}

		Integer codigo = entrada.getCodigoEntrada();
		Usuario dueño = entradaUsuarioMap.get(codigo);

		if (dueño == null)
			throw new IllegalArgumentException("Ningún usuario posee esa entrada");

		dueño.anularEntrada(codigo, contrasenia, espectaculos);

		entradaUsuarioMap.remove(codigo);

		return true;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {

		if (entrada == null)
			throw new IllegalArgumentException("Entrada inválida");

		if (entrada.obtenerFecha().isBefore(LocalDate.now()))
			throw new IllegalArgumentException("La entrada ya está vencida");

		int codigo = entrada.getCodigoEntrada();

		Usuario dueño = null;

		for (Usuario u : usuarios.values()) {
			if (u.getEntradas().containsKey(codigo)) {
				dueño = u;
				break;
			}
		}

		if (dueño == null)
			throw new IllegalArgumentException("Ningún usuario posee esa entrada");

		dueño.credencialesCorrectas(contrasenia);

		LocalDate fechaParseada = this.parsearFecha(fecha);

		String nombreEsp = entrada.obtenerNombreEspectaculo();
		Espectaculo espectaculo = espectaculos.get(nombreEsp);

		if (espectaculo == null) {
			throw new IllegalArgumentException("Espectáculo no encontrado para la entrada");
		}

		Funcion nuevaFuncion = espectaculo.getFunciones().get(fechaParseada);

		if (nuevaFuncion == null)
			throw new IllegalArgumentException("No hay función en la nueva fecha");

		Sede sede = nuevaFuncion.getSede();

		if (!sede.sectorExiste(sector))
			throw new IllegalArgumentException("El sector no existe");

		if (!sede.asientosDisponibles(sector, new int[] { asiento }))
			throw new IllegalArgumentException("El asiento no está disponible");

		this.anularEntrada(entrada, contrasenia);

		contadorCodigoEntrada++;

		double precio = nuevaFuncion.calcularPrecioEntrada(sector);

		Sector sectorObj = sede.getSector(sector);

		List<Integer> asientos = Arrays.asList(asiento);

		IEntrada nuevaEntrada = new Entrada(
				contadorCodigoEntrada,
				espectaculo.getNombre(),
				fechaParseada,
				sectorObj,
				sede.getNombre(),
				precio,
				asientos);

		sede.asignarAsiento(sector, asiento);

		nuevaFuncion.agregarEntradaVendida((Entrada) nuevaEntrada);

		dueño.agregarEntrada(contadorCodigoEntrada, (Entrada) nuevaEntrada);

		return nuevaEntrada;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {

		if (entrada == null)
			throw new IllegalArgumentException("Entrada inválida");

		String nombreEspectaculo = entrada.obtenerNombreEspectaculo();
		LocalDate fechaOriginal = entrada.obtenerFecha();

		if (fechaOriginal.isBefore(LocalDate.now()))
			throw new IllegalArgumentException("La entrada ya está vencida");

		int codigoOriginal = entrada.getCodigoEntrada();
		Usuario dueño = null;

		for (Usuario u : usuarios.values()) {
			if (u.getEntradas().containsKey(codigoOriginal)) {
				dueño = u;
				break;
			}
		}

		if (dueño == null)
			throw new IllegalArgumentException("Ningún usuario posee esa entrada");

		dueño.credencialesCorrectas(contrasenia);

		LocalDate fechaParseada = this.parsearFecha(fecha);

		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado");

		Funcion nuevaFuncion = espectaculo.getFunciones().get(fechaParseada);

		if (nuevaFuncion == null)
			throw new IllegalArgumentException("No hay función en la nueva fecha");

		Sede sede = nuevaFuncion.getSede();

		this.anularEntrada(entrada, contrasenia);

		contadorCodigoEntrada++;
		double precio = nuevaFuncion.calcularPrecioEntrada();
		Sector nuevoSector = sede.getSector("CAMPO");

		IEntrada nuevaEntrada = new Entrada(
				contadorCodigoEntrada,
				nombreEspectaculo,
				fechaParseada,
				nuevoSector,
				sede.getNombre(),
				precio);

		nuevaFuncion.agregarEntradaVendida((Entrada) nuevaEntrada);

		dueño.agregarEntrada(contadorCodigoEntrada, (Entrada) nuevaEntrada);

		return nuevaEntrada;
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha) {

		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);

		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado");

		LocalDate fechaParseada = this.parsearFecha(fecha);

		Funcion funcion = espectaculo.getFunciones().get(fechaParseada);

		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha");

		return funcion.calcularPrecioEntrada();
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {

		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);

		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado");

		LocalDate fechaParseada = this.parsearFecha(fecha);

		Funcion funcion = espectaculo.getFunciones().get(fechaParseada);

		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha");

		Sede sede = funcion.getSede();

		if (!sede.sectorExiste(sector))
			throw new IllegalArgumentException("El sector no existe");

		return funcion.calcularPrecioEntrada(sector);
	}

	@Override
	public double totalRecaudado(String nombreEspectaculo) {

		if (!espectaculos.containsKey(nombreEspectaculo))
			throw new IllegalArgumentException("Espectáculo no encontrado");

		return recaudacionGlobal.get(nombreEspectaculo);
	}

	@Override
	public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {

		if (!espectaculos.containsKey(nombreEspectaculo))
			throw new IllegalArgumentException("Espectáculo no encontrado");

		if (!sedes.containsKey(nombreSede))
			throw new IllegalArgumentException("Sede no encontrada");

		Map<String, Double> recaudacionSede = recaudacionPorSede.getOrDefault(nombreEspectaculo, new HashMap<>());
		return recaudacionSede.get(nombreSede);
	}

	private LocalDate parsearFecha(String fecha) {
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate fechaFuncion = LocalDate.parse(fecha, formato);
		return fechaFuncion;
	}

}
