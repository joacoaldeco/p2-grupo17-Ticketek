package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketek implements ITicketek {

	private Map<String, Sede> sedes = new HashMap<>();
	private Map<String, Usuario> usuarios = new HashMap<>();
	private Map<String, Espectaculo> espectaculos = new HashMap<>();

	private Map<String, Double> recaudacionGlobal = new HashMap<>();
	private Map<String, Map<String, Double>> recaudacionPorSede = new HashMap<>();
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

		espectaculo.agregarFuncion(sedeObj, fechaFuncion, precioBase);
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
			int cantidadEntradas) {
		Usuario usuario = usuarios.get(email);
		if (usuario == null)
			throw new IllegalArgumentException("Usuario no registrado.");
		if (!usuario.getContrasenia().equals(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida.");

		Espectaculo esp = espectaculos.get(nombreEspectaculo);
		if (esp == null)
			throw new IllegalArgumentException("Espectáculo no registrado.");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate fechaFuncion = LocalDate.parse(fecha, formato);

		Funcion funcion = esp.getFunciones().get(fechaFuncion);
		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha.");

		Sede sedeFuncion = funcion.getSede();
		if (!(sedeFuncion instanceof Estadio))
			throw new IllegalArgumentException(
					"La función se realiza en una sede numerada. Use el método correspondiente.");

		List<IEntrada> entradasVendidas = new ArrayList();

		for (int i = 0; i < cantidadEntradas; i++) {
			contadorCodigoEntrada++;

			double precio = funcion.calcularPrecioEntrada();

			Entrada entrada = new Entrada(contadorCodigoEntrada, esp, funcion, null, precio);

			funcion.agregarEntradaVendida(entrada);

			usuario.agregarEntrada(contadorCodigoEntrada, (Entrada) entrada);

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
			throw new IllegalArgumentException("Usuario no registrado.");
		if (!usuario.getContrasenia().equals(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida.");

		Espectaculo esp = espectaculos.get(nombreEspectaculo);
		if (esp == null)
			throw new IllegalArgumentException("Espectáculo no registrado.");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate fechaFuncion = LocalDate.parse(fecha, formato);

		Funcion funcion = esp.getFunciones().get(fechaFuncion);
		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha.");

		Sede sedeFuncion = funcion.getSede();
		if (!(sedeFuncion instanceof Teatro || sedeFuncion instanceof MiniEstadio))
			throw new IllegalArgumentException(
					"La función no se realiza en una sede numerada. Use el otro método de venta.");

		if (!sedeFuncion.sectorExiste(sector))
			throw new IllegalArgumentException("Sector no existente en la sede.");

		if (!sedeFuncion.asientosDisponibles(sector, asientos))
			throw new IllegalArgumentException("Algunos asientos solicitados no están disponibles.");

		List<IEntrada> entradasVendidas = new ArrayList<>();

		for (int asiento : asientos) {
			contadorCodigoEntrada++;

			double precio = funcion.calcularPrecioEntrada(sector);

			Entrada entrada = new Entrada(contadorCodigoEntrada, esp, funcion, sedeFuncion.getSector(sector), precio);

			sedeFuncion.asignarAsiento(sector, asiento);

			funcion.agregarEntradaVendida(entrada);

			usuario.agregarEntrada(contadorCodigoEntrada, (Entrada) entrada);

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
			throw new IllegalArgumentException("Espectáculo no encontrado.");

		StringBuilder sb = new StringBuilder();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");

		for (Map.Entry<LocalDate, Funcion> entry : espectaculo.getFunciones().entrySet()) {
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

				List<String> partesSector = new ArrayList<>();
				for (Sector sector : sede.getSectores()) {
					String nombreSector = sector.getNombre();
					int vendidas = vendidasPorSector.getOrDefault(nombreSector, 0);
					int capacidad = ((Teatro) sede).getCapacidadPorSector(nombreSector);
					partesSector.add(nombreSector + ": " + vendidas + "/" + capacidad);
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
			throw new IllegalArgumentException("Espectáculo no encontrado.");

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
			throw new IllegalArgumentException("Usuario no registrado.");
		if (!usuario.getContrasenia().equals(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida.");

		Map<Integer, Entrada> entradas = usuario.getEntradas();

		List<IEntrada> resultado = new ArrayList<>();

		for (Map.Entry<Integer, Entrada> entrada : entradas.entrySet()) {
			Entrada entradaValue = entrada.getValue();

			if (entradaValue.getFuncion().getFecha().isAfter(LocalDate.now())) {
				resultado.add(entradaValue);
			}
		}

		return resultado;
	}

	@Override
	public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
		Usuario usuario = usuarios.get(email);
		if (usuario == null)
			throw new IllegalArgumentException("Usuario no registrado.");
		if (!usuario.getContrasenia().equals(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida.");

		List<IEntrada> resultado = (List<IEntrada>) usuario.getEntradas();

		return resultado;
	}

	@Override
	public boolean anularEntrada(IEntrada entrada, String contrasenia) {
		if (entrada == null)
			throw new IllegalArgumentException("Entrada inválida.");

		LocalDate hoy = LocalDate.now();
		if (entrada.getFuncion().getFecha().isBefore(hoy)) {
			return false;
		}

		Integer codigo = ((Entrada) entrada).getCodigoEntrada();
		Usuario dueño = null;

		for (Usuario u : usuarios.values()) {
			if (u.getEntradas().containsKey(codigo)) {
				dueño = u;
				break;
			}
		}

		if (dueño == null)
			throw new IllegalArgumentException("Ningún usuario posee esa entrada.");

		dueño.anularEntrada(codigo, contrasenia);

		return true;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
		if (entrada == null)
			throw new IllegalArgumentException("Entrada inválida.");

		if (entrada.getFuncion().getFecha().isBefore(LocalDate.now()))
			throw new IllegalArgumentException("La entrada ya está vencida.");

		int codigo = entrada.getCodigoEntrada();
		Usuario dueño = null;

		for (Usuario u : usuarios.values()) {
			if (u.getEntradas().containsKey(codigo)) {
				dueño = u;
				break;
			}
		}

		if (dueño == null)
			throw new IllegalArgumentException("Ningún usuario posee esa entrada.");

		if (!dueño.getContrasenia().equals(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida.");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate nuevaFecha = LocalDate.parse(fecha, formato);

		Espectaculo espectaculo = entrada.getEspectaculo();
		Funcion nuevaFuncion = espectaculo.getFunciones().get(nuevaFecha);

		if (nuevaFuncion == null)
			throw new IllegalArgumentException("No hay función en la nueva fecha.");

		Sede sede = nuevaFuncion.getSede();
		if (!(sede instanceof Teatro || sede instanceof MiniEstadio))
			throw new IllegalArgumentException("La sede no es numerada.");

		if (!sede.sectorExiste(sector))
			throw new IllegalArgumentException("El sector no existe.");

		if (!sede.asientosDisponibles(sector, new int[] { asiento }))
			throw new IllegalArgumentException("El asiento no está disponible.");

		this.anularEntrada(entrada, contrasenia);

		contadorCodigoEntrada++;
		double precio = nuevaFuncion.calcularPrecioEntrada(sector);
		Sector sectorObj = sede.getSector(sector);
		IEntrada nuevaEntrada = new Entrada(contadorCodigoEntrada, espectaculo, nuevaFuncion, sectorObj, precio);

		sede.asignarAsiento(sector, asiento);

		nuevaFuncion.agregarEntradaVendida((Entrada) nuevaEntrada);
		dueño.agregarEntrada(contadorCodigoEntrada, (Entrada) nuevaEntrada);

		return nuevaEntrada;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
		if (entrada == null)
			throw new IllegalArgumentException("Entrada inválida.");

		if (entrada.getFuncion().getFecha().isBefore(LocalDate.now()))
			throw new IllegalArgumentException("La entrada ya está vencida.");

		int codigoOriginal = entrada.getCodigoEntrada();
		Usuario dueño = null;

		for (Usuario u : usuarios.values()) {
			if (u.getEntradas().containsKey(codigoOriginal)) {
				dueño = u;
				break;
			}
		}

		if (dueño == null)
			throw new IllegalArgumentException("Ningún usuario posee esa entrada.");

		if (!dueño.getContrasenia().equals(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida.");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate nuevaFecha = LocalDate.parse(fecha, formato);

		Espectaculo espectaculo = entrada.getEspectaculo();
		Funcion nuevaFuncion = espectaculo.getFunciones().get(nuevaFecha);

		if (nuevaFuncion == null)
			throw new IllegalArgumentException("No hay función en la nueva fecha.");

		if (!(nuevaFuncion.getSede() instanceof Estadio))
			throw new IllegalArgumentException("La nueva función no es en un estadio.");

		this.anularEntrada(entrada, contrasenia);

		contadorCodigoEntrada++;
		double precio = nuevaFuncion.calcularPrecioEntrada();
		IEntrada nuevaEntrada = new Entrada(contadorCodigoEntrada, espectaculo, nuevaFuncion,
				nuevaFuncion.getSede().getSector("CAMPO"), precio);

		nuevaFuncion.agregarEntradaVendida((Entrada) nuevaEntrada);
		dueño.agregarEntrada(contadorCodigoEntrada, (Entrada) nuevaEntrada);

		return nuevaEntrada;
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha) {
		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado.");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate fechaFuncion = LocalDate.parse(fecha, formato);

		Funcion funcion = espectaculo.getFunciones().get(fechaFuncion);
		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha.");

		if (!(funcion.getSede() instanceof Estadio))
			throw new IllegalArgumentException("La función no se realiza en un estadio.");

		return funcion.calcularPrecioEntrada();
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		if (espectaculo == null)
			throw new IllegalArgumentException("Espectáculo no encontrado.");

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy");
		LocalDate fechaFuncion = LocalDate.parse(fecha, formato);

		Funcion funcion = espectaculo.getFunciones().get(fechaFuncion);
		if (funcion == null)
			throw new IllegalArgumentException("No hay función en esa fecha.");

		Sede sede = funcion.getSede();
		if (!(sede instanceof Teatro || sede instanceof MiniEstadio))
			throw new IllegalArgumentException("La sede no es numerada.");

		if (!sede.sectorExiste(sector))
			throw new IllegalArgumentException("El sector no existe.");

		return funcion.calcularPrecioEntrada(sector);
	}

	@Override
	public double totalRecaudado(String nombreEspectaculo) {
		if (!espectaculos.containsKey(nombreEspectaculo))
			throw new IllegalArgumentException("Espectáculo no encontrado.");

		return recaudacionGlobal.get(nombreEspectaculo);
	}

	@Override
	public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
		if (!espectaculos.containsKey(nombreEspectaculo))
			throw new IllegalArgumentException("Espectáculo no encontrado.");

		if (!sedes.containsKey(nombreSede))
			throw new IllegalArgumentException("Sede no encontrada.");

		Map<String, Double> recaudacionSede = recaudacionPorSede.getOrDefault(nombreEspectaculo, new HashMap<>());
		return recaudacionSede.get(nombreSede);
	}

}
