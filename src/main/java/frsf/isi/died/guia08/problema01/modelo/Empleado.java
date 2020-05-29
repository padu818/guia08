package frsf.isi.died.guia08.problema01.modelo;


import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.exception.TareaException;


public class Empleado {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;
	
	


	public Empleado(Integer cui, String nomb, Tipo a, Double cost) {
		super();
		cuil = cui;
		nombre = nomb;
		tipo = a;
		costoHora = cost;
		tareasAsignadas = new ArrayList<Tarea>();
		

	}
	//CREO UN CONSTRUCTOR CON BOOLEAN TEST PARA VERIFICAR LOS METODOS. DEBIDO A QUE INICIALICE LOS CALCULOS
	// DE PAGO POR TAREA EN LA APPRRHH
	public Empleado(Integer cui, String nomb, Tipo a, Double cost,Boolean test) {
		super();
		cuil = cui;
		nombre = nomb;
		tipo = a;
		costoHora = cost;
		tareasAsignadas = new ArrayList<Tarea>();
		

		if(Tipo.CONTRATADO == a) {
			calculoPagoPorTarea = 
					(t -> t.getDuracionEstimada() > t.cantHorasTarea() ? 
							(t.getDuracionEstimada()*costoHora)*1.30 :(t.getDuracionEstimada()*costoHora)*t.seAtraso());
			puedeAsignarTarea = e -> this.tareasAdquiridas() <5;
		}
		else {
			calculoPagoPorTarea = 
					(t -> t.getDuracionEstimada() > t.cantHorasTarea() ?
							(t.getDuracionEstimada()*costoHora)*1.20 : (t.getDuracionEstimada()*costoHora)); 
			puedeAsignarTarea = e ->  this.horasTareasAdquiridas() < 15;
		}

	}
		
	
	
	public Empleado() {
		super();
	}
	
	
	/*	
		
		a. Ambos tipos de empleados cobran la duración estimada de la
		tarea multiplicado por el costo hora.
		b. Además, cobran un extra si terminan la tarea antes de lo
		planificado. Para determinar esto se calcula la diferencia en días,
		entre la fecha de inicio y la fecha final de la tarea, y se multiplica
		por 4 horas teóricas de trabajo diaria. Entonces si una tarea
		estaba estimada en 10 horas, y fue finalizada en 2 días, se asume
		que el empleado tardó 8 horas en resolverla.
		c. Los empleados efectivos aumentan el valor de su hora en un 20%
		en aquellas tareas que finalizaron antes.
		d. Los empleados contratados aumentan el valor de su hora en un
		30 % para estas tareas. En cambio, si un empleado contratado se
		demora más de 2 días, en finalizar su tarea respecto a lo que
		estaba estimado cobrará un 75% de su valor hora.
		
			
			*/
	public Double salario() {
		Double s=0.0;
		for(Tarea t: tareasAsignadas) {
			// cargar todas las tareas no facturadas
			if(t.getFacturada() == false) {
				// calcular el costo
				s+= this.costoTarea(t);
				// marcarlas como facturadas.
				t.setFacturada(true);
			}
		}
		return s;
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		return this.getCalculoPagoPorTarea().apply(t);
	}
		
	public long  tareasAdquiridas() {
	//	LocalDateTime aux = LocalDate.parse("01-01-1970",formatter).atStartOfDay();
		return this.tareasAsignadas.stream().filter( a ->  a.getFechaFin()== null).count();
	}
	
	public Integer horasTareasAdquiridas() {
		Integer auxiliar = 0;
		for(Tarea t: tareasAsignadas) {
			if(t.getFechaFin() == null) {
				//auxiliar+= ((int) DAYS.between(t.getFechaInicio(),LocalDateTime.now()))*4;
				auxiliar+= t.getDuracionEstimada();
			}
		}
		return auxiliar;
		
//		return  this.tareasAsignadas.stream().filter( a -> a.getFechaFin() == null).mapToInt(a  -> ).sum();

	}
	/*
	 * Un empleado puede ser asignado a un proyecto según las siguientes condiciones
		a. Si es contratado, no puede tener más de 5 tareas asignadas pendientes de finalizar.
		b. Si es Efectivo, no puede tener asignadas, tareas pendientes de finalizar que sumen más de 15 horas de trabajo estimadas.
		c. En cualquiera de los dos casos si el empleado no puede ser asignado se retorna un falso.
		d. En caso de que se quiera asignar un empleado a una tarea que ya tiene otro empleado asignado,
 			o a una tarea que ya fue finalizada, entonces se lanza una excepción indicando 
 			que la tarea que se quiere asignar es incorrecta que se seleccione otra tarea.
	 */
	
	
	public Boolean asignarTarea(Tarea t) {
		if(puedeAsignarTarea.test(t)) {
			tareasAsignadas.add(t);
			try {
				t.asignarEmpleado(this);
			} catch (TareaException e) {
				e.printStackTrace();
			}
			return true;
		}
		else{
			return false;
			}
		}
	
	
	public void comenzar(Integer idTarea) throws TareaException{
		Boolean noEncontrado = true;
		for(Tarea t: tareasAsignadas) {
			if(t.getId() == idTarea) {
				t.setFechaInicio(LocalDateTime.now());
				return;
			}
		}
		if(noEncontrado== true) 
			throw new TareaException("El empleado "
					+this.getNombre()+" no tiene asignado ninguna tarea con el id "+idTarea);
		
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea) throws TareaException{
		Boolean noEncontrado = true;
		for(Tarea t: tareasAsignadas) {
			if(t.getId() == idTarea) {
				t.setFechaFin(LocalDateTime.now());
				return;
			}
		}
		if(noEncontrado== true) 
			throw new TareaException("El empleado "
					+this.getNombre()+" no tiene asignado ninguna tarea con el id "+idTarea);
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}

	public void comenzar(Integer idTarea,String fecha) throws TareaException {
		LocalDateTime inicioi = LocalDateTime.parse(fecha,this.formatter);
		Boolean noEncontrado = true;
		for(Tarea t: tareasAsignadas) {
			if(t.getId() == idTarea) {
				t.setFechaInicio(inicioi);
				return;
			}
		}
		if(noEncontrado== true) 
			throw new TareaException("El empleado "
					+this.getNombre()+" no tiene asignado ninguna tarea con el id "+idTarea);

		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea,String fecha) throws TareaException {
		LocalDateTime finf = LocalDateTime.parse(fecha,this.formatter);
		Boolean noEncontrado = true;
		for(Tarea t: tareasAsignadas) {
			if(t.getId() == idTarea) {
				t.setFechaFin(finf);
				return;
			}
		}
		if(noEncontrado== true) 
			throw new TareaException("El empleado "
					+this.getNombre()+" no tiene asignado ninguna tarea con el id "+idTarea);
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	
	
	
	
	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {
		this.tareasAsignadas = tareasAsignadas;
	}
	

	public Function<Tarea, Double> getCalculoPagoPorTarea() {
		return calculoPagoPorTarea;
	}

	public void setCalculoPagoPorTarea(Function<Tarea, Double> calculoPagoPorTarea) {
		this.calculoPagoPorTarea = calculoPagoPorTarea;
	}

	public Predicate<Tarea> getPuedeAsignarTarea() {
		return puedeAsignarTarea;
	}

	public void setPuedeAsignarTarea(Predicate<Tarea> puedeAsignarTarea) {
		this.puedeAsignarTarea = puedeAsignarTarea;
	}
	
	public String asCsv() {
		return this.cuil+";\""+this.nombre+"\";"+this.costoHora;
	}
}
