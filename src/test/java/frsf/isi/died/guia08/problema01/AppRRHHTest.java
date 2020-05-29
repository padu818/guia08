package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;

import frsf.isi.died.guia08.problema01.exception.EmpleadoException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;


public class AppRRHHTest {

	

	@Test
	public void testAgregarEmpleadoContratado() {
		AppRRHH nuevo = new AppRRHH();
		nuevo.agregarEmpleadoContratado(555, "Martin", 500.0);
		assertEquals(nuevo.getEmpleados().size(), 1);
	}
	@Test
	public void testAgregarEmpleadoEfectivo() {
		AppRRHH nuevo = new AppRRHH();
		nuevo.agregarEmpleadoContratado(555, "Martin", 500.0);
		nuevo.agregarEmpleadoEfectivo(556,"Marta", 100.0);
		assertEquals(nuevo.getEmpleados().size(), 2);
	}
	
	@Test
	public void testAsignarTarea() {
		AppRRHH nuevo = new AppRRHH();
		nuevo.agregarEmpleadoContratado(555, "Martin", 500.0);
		nuevo.agregarEmpleadoEfectivo(556,"Marta", 100.0);
		try {

			nuevo.asignarTarea(555, 1, "Esto es una prueba", 8);
			nuevo.asignarTarea(556, 2, "Se repite la prueba", 8);

		} catch (EmpleadoException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testComenzarTarea() {
		AppRRHH nuevo = new AppRRHH();
		nuevo.agregarEmpleadoContratado(555, "Martin", 500.0);
		try {
			nuevo.asignarTarea(555, 1, "Esto es una prueba", 8);
			nuevo.empezarTarea(555, 1);
			Optional<Empleado> probando =nuevo.getEmpleados().stream().filter(e -> e.getCuil() == 555).findFirst();
			Boolean comenzoTarea = false;
			if(probando.get().getTareasAsignadas().get(0).getFechaInicio() != null) {
				comenzoTarea = true;
				//el empleado solo tiene una tarea, por lo tanto solo busco la tarea 0
			}
			assertTrue(comenzoTarea);
		} catch (EmpleadoException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testFinalizarTarea() {
		AppRRHH nuevo = new AppRRHH();
		nuevo.agregarEmpleadoContratado(555, "Martin", 500.0);
		try {
			nuevo.asignarTarea(555, 1, "Esto es una prueba", 8);
			nuevo.empezarTarea(555, 1);
			nuevo.terminarTarea(555, 1);
			Optional<Empleado> probando =nuevo.getEmpleados().stream().filter(e -> e.getCuil() == 555).findFirst();
			Boolean terminoTarea = false;
			if(probando.get().getTareasAsignadas().get(0).getFechaFin() != null) {
				terminoTarea = true;
				//el empleado solo tiene una tarea, por lo tanto solo busco la tarea 0
			}
			assertTrue(terminoTarea);
		} catch (EmpleadoException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testFacturar() {
		AppRRHH nuevo = new AppRRHH();
		nuevo.agregarEmpleadoContratado(555, "Martin", 500.0);
		try {
			nuevo.asignarTarea(555, 1, "Esto es una prueba", 8);
			nuevo.empezarTarea(555, 1);
			nuevo.terminarTarea(555, 1);
			Double f = nuevo.facturar();
			Double expected = 5200.0;
			assertEquals(expected, f);

		} catch (EmpleadoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
