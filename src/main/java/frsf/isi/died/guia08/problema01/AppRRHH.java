package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.function.Predicate;


import frsf.isi.died.guia08.problema01.exception.EmpleadoException;
import frsf.isi.died.guia08.problema01.exception.TareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	private List<Empleado> empleados;
	// el constructor para generar los test
	public AppRRHH() {
		super();
		empleados= new ArrayList<Empleado>();
	}
	
	//lo cree para los test
	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public static void main(String[] args) {
		//aca pruebo los archivos;
		AppRRHH pruebas = new AppRRHH();
		try {
		pruebas.cargarEmpleadosContratadosCSV("EmpleadoContratado.csv");
		pruebas.cargarEmpleadosEfectivosCSV("EmpleadoEfectivo.csv");
		pruebas.cargarTareasCSV("TareasTerminadasNoFacturadas.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmpleadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		

	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {

		Empleado nuevo = new Empleado(cuil, nombre, Tipo.CONTRATADO,costoHora);
	
		nuevo.setCalculoPagoPorTarea((t -> t.getDuracionEstimada() > t.cantHorasTarea() ? 
				(t.getDuracionEstimada()*costoHora)*1.30 :((t.getDuracionEstimada()*costoHora)*t.seAtraso())));
		nuevo.setPuedeAsignarTarea( t -> nuevo.tareasAdquiridas() <5);
		
		empleados.add(nuevo);

	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		Empleado nuevo = new Empleado(cuil, nombre, Tipo.EFECTIVO,costoHora);
		
		nuevo.setCalculoPagoPorTarea((t -> t.getDuracionEstimada() > t.cantHorasTarea() ?
				(t.getDuracionEstimada()*costoHora)*1.20 : (t.getDuracionEstimada()*costoHora)));
		nuevo.setPuedeAsignarTarea(t ->  nuevo.horasTareasAdquiridas() < 15);
		
		empleados.add(nuevo);
		
	}
	
	public void asignarTarea(Integer cui,Integer idTarea,String descripcion,Integer duracionEstimada)  throws EmpleadoException {

		Optional<Empleado> aux = this.buscarEmpleado( e -> e.getCuil().equals(cui));
		if(aux.isPresent()) {
			Tarea nueva = new Tarea(idTarea,descripcion, duracionEstimada,aux.get());
			aux.get().asignarTarea(nueva);
		}
		else throw new EmpleadoException("No existe ningun empleado con el cuil "+cui+".");
		
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) throws EmpleadoException {
		
		Optional<Empleado> aux = this.buscarEmpleado( e -> e.getCuil().equals(cuil));
		if(aux.isPresent()) {
			try {
				aux.get().comenzar(idTarea);
			} catch (TareaException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		}
		else throw new EmpleadoException("No existe ningun empleado con el cuil "+cuil+".");
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) throws EmpleadoException  {
		
		Optional<Empleado> aux = this.buscarEmpleado( e -> e.getCuil().equals(cuil));
		if(aux.isPresent()) {
			try {
				aux.get().finalizar(idTarea);
			} catch (TareaException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
		}
		else throw new EmpleadoException("No existe ningun empleado con el cuil "+cuil+".");

	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException{
	//	FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)) {
				String linea = null;
				while((linea = in.readLine()) != null) {
					String [] fila = linea.split(";");
					agregarEmpleadoContratado(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
				}
			}
		}
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException{
//		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)) {
				String linea = null;
				while((linea = in.readLine()) != null) {
					String [] fila = linea.split(";");
					agregarEmpleadoContratado(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
				}
			}
		}
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException, IOException,  EmpleadoException {
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)) {
				String linea = null;
				while((linea = in.readLine()) != null) {
					String [] fila = linea.split(";");
					asignarTarea(Integer.valueOf(fila[5]), Integer.valueOf(fila[0]), fila[1], Integer.valueOf(fila[2]));
					Optional <Empleado> asignando = buscarEmpleado(e -> e.getCuil().equals(Integer.valueOf(fila[5])));
					try {
						asignando.get().comenzar( Integer.valueOf(fila[0]), fila[3]);
						asignando.get().finalizar( Integer.valueOf(fila[0]), fila[4]);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (TareaException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	//queria usar el stream para una busqueda mas compacta. no lo aplique por el tema de que se guarda una lista
	//de stream <empleados> o stream<tarea> y nose si al aplicar el cast puedo generar algun problema en el archivo
	private void guardarTareasTerminadasCSV() throws IOException {
		try(Writer fileWriter = new FileWriter("TareasTerminadasNoFacturadas.csv", true)){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				for(Empleado e: empleados) {
					//List<Tarea> tareasTerminadas = new ArrayList<Tarea>();
					for(Tarea t: e.getTareasAsignadas()) {
						if(t.getFacturada() == false) {
							
							out.write(t.asCsv()+System.getProperty("line.separator"));
						}
					}
				}
			}
		}
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
