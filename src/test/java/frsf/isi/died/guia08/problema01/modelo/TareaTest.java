package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

import frsf.isi.died.guia08.problema01.exception.TareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	@Test
	public void asignarEmpleadoTest() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Empleado e2 = new Empleado(556,"Marta",Tipo.EFECTIVO, 100.0, true);
		Tarea t1 = new Tarea(8,"Limpiar", 8,false);
		Tarea t2 = new Tarea(9,"Limpiar", 8,false);
		try {
			t1.asignarEmpleado(e1);
			t2.asignarEmpleado(e2);
			assertNotNull(t1.getEmpleadoAsignado());
			assertNotNull(t2.getEmpleadoAsignado());
		} catch (TareaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
