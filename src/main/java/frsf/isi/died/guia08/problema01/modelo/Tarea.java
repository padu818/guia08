package frsf.isi.died.guia08.problema01.modelo;


import static java.time.temporal.ChronoUnit.DAYS;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import frsf.isi.died.guia08.problema01.exception.TareaException;


public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	public Tarea(Integer i, String desc, Integer tim, Empleado emp, String fInic, String ffin, Boolean fact) {
		super();
		id = i;
		descripcion = desc;
		duracionEstimada = tim;
		empleadoAsignado= emp;
		fechaInicio =  LocalDateTime.parse(fInic,this.formatter);
		fechaFin = LocalDateTime.parse(ffin,this.formatter);
		facturada = fact;
		emp.getTareasAsignadas().add(this);
	}
	//sin fecha finicio
	public Tarea(Integer i, String desc, Integer tim, Empleado emp, String fInic,  Boolean fact) {
		super();
		id = i;
		descripcion = desc;
		duracionEstimada = tim;
		fechaInicio =  LocalDateTime.parse(fInic,this.formatter);
		facturada = fact;
		empleadoAsignado= emp;
		emp.getTareasAsignadas().add(this);
	}
	//sin fecha comienzo,fin y empleado
	public Tarea(Integer i, String desc, Integer tim,   Boolean fact) {
		super();
		id = i;
		descripcion = desc;
		duracionEstimada = tim;
		facturada = fact;
	}
	
	public Tarea(Integer i, String desc, Integer duracion, Empleado emp) {
		super();
		id = i;
		descripcion = desc;
		duracionEstimada = duracion;
		empleadoAsignado= emp;
		facturada = false;
	}
	
	
	public int cantHorasEmpleadas() {
		return ((int) DAYS.between(fechaInicio,fechaFin))*4;
	}
	
	
	public int cantHorasTarea() {
		return ((int) DAYS.between(fechaInicio,fechaFin))*4; 
	}
	//metodo para simplificar el calculoPagoPorTarea
	public Double seAtraso() {
		int aux =(int) DAYS.between(fechaInicio,fechaFin)*4;
		if(aux > this.duracionEstimada) {
			return 0.75;
		}
		else
			return 1.0;
	}
	
	public void asignarEmpleado(Empleado e) throws TareaException {
		if(this.getEmpleadoAsignado() != null && this.getFechaFin() != null) 
			throw new TareaException("Error, la tarea que se quiere asignar es incorrecta. Seleccione otra tarea. "); 
		else
			this.empleadoAsignado= e;
	
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}



	public String asCsv() {
		return this.id+";\""+this.descripcion+"\";"+this.duracionEstimada+"\";"
				+this.fechaInicio+"\";"+this.fechaFin+"\";"+this.getEmpleadoAsignado().getCuil()+"\";"+this.getEmpleadoAsignado().getNombre();
	}
	
	
}
