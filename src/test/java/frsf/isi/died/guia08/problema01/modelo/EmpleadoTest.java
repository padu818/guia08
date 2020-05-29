package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import frsf.isi.died.guia08.problema01.exception.TareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	
	@Test
	public void testCostoTarea() {
	
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Empleado e2 = new Empleado(556,"Marta",Tipo.EFECTIVO, 100.0, true);
		Tarea t1 = new Tarea(1,"Limpiar", 8, e1, "01-01-2020 00:22", "03-01-2020 00:22", true);
		Tarea t2 = new Tarea(2,"Limpiar", 8, e2, "25-05-2020 00:22","27-05-2020 00:23", false);
		Tarea t3 = new Tarea(3,"Limpiar", 8, e1, "25-05-2020 00:22","28-05-2020 00:22",  false);
		Tarea t4 = new Tarea(3,"Limpiar", 8, e1, "25-05-2020 00:22","26-05-2020 00:22",  false);
		Tarea t5 = new Tarea(3,"Limpiar", 8, e2, "25-05-2020 00:22","29-05-2020 00:22",  false);
		Tarea t6 = new Tarea(3,"Limpiar", 8, e2, "25-05-2020 00:22","26-05-2020 00:22",  false);
		Double sal1 = 4000.0;
		Double sal2 = 800.0;
		Double sal3 = 3000.0;
		Double sal4 = 5200.0;
		Double sal5 = 800.0;
		Double sal6 = 960.0;
		assertEquals(sal1, e1.costoTarea(t1)); //contratado -> completo en tiempo
		assertEquals(sal2, e2.costoTarea(t2));//efectivo -> completo en tiempo
		assertEquals(sal3, e1.costoTarea(t3)); //contratado -> se atraso(25% disminuye)
		assertEquals(sal4, e1.costoTarea(t4)); //contratado -> se adelanto (30% de aumento)
		assertEquals(sal5, e2.costoTarea(t5));//efectivo -> se atraso(no cambia el salario)
		assertEquals(sal6, e2.costoTarea(t6));//efectivo -> se adelanto(20% aumento)
	}
	
	
	
	@Test
	public void testSalario() {
	
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Empleado e2 = new Empleado(556,"Marta",Tipo.EFECTIVO, 100.0, true);
		Tarea t1 = new Tarea(1,"Limpiar", 8, e1, "01-01-2020 00:22", "03-01-2020 00:22", true);
		Tarea t2 = new Tarea(2,"Limpiar", 8, e1, "25-05-2020 00:22","27-05-2020 00:23", false);
		Tarea t3 = new Tarea(3,"Limpiar", 8, e2, "25-05-2020 00:22","27-05-2020 00:22",  false);
		Double sal1 = 4000.0;
		Double sal2 = 800.0;
		assertEquals(sal1, e1.salario());
		assertEquals(sal2, e2.salario());
	}


	@Test
	public void testAsignarTarea() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Empleado e2 = new Empleado(556,"Marta",Tipo.EFECTIVO, 100.0, true);
		Tarea t1 = new Tarea(1,"Limpiar", 8, e1, "01-01-2020 00:22", true);
		Tarea t2 = new Tarea(2,"Limpiar", 8, e1, "25-05-2020 00:22", false);
		Tarea t3 = new Tarea(3,"Limpiar", 8, e2, "28-05-2020 00:22", false);
		Tarea t4 = new Tarea(4,"Limpiar", 8,  false);
		Tarea t5 = new Tarea(5,"Limpiar", 8,false);
		assertTrue( e1.asignarTarea(t5));
		assertTrue( e2.asignarTarea(t4));
	}
	
	
	@Test

	public void testNoAsignarTarea() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Empleado e2 = new Empleado(556,"Marta",Tipo.EFECTIVO, 100.0, true);
		Tarea t1 = new Tarea(1,"Limpiar", 8, e1, "25-05-2020 00:22", false);
		Tarea t2 = new Tarea(2,"Limpiar", 8, e1, "25-05-2020 00:22", false);
		Tarea t3 = new Tarea(3,"Limpiar", 16, e2, "28-05-2020 00:22", false);
		Tarea t4 = new Tarea(4,"Limpiar", 8,  false);
		Tarea t5 = new Tarea(5,"Limpiar", 8,false);
		Tarea t7 = new Tarea(6,"Limpiar", 8,false);
		Tarea t8 = new Tarea(7,"Limpiar", 8,false);
		Tarea t9 = new Tarea(8,"Limpiar", 8,false);
		Tarea t10 = new Tarea(9,"Limpiar", 8,false);
		e1.asignarTarea(t7);e1.asignarTarea(t8);e1.asignarTarea(t9);
	//	System.out.println(e1.tareasAdquiridas());
		assertFalse( e1.asignarTarea(t5));
		//System.out.println(e2.horasTareasAdquiridas());
		assertFalse( e2.asignarTarea(t4));
	}
//
	@Test
	public void testComenzarInteger() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Tarea t8 = new Tarea(7,"Limpiar", 8,false);
		e1.asignarTarea(t8);
		try {
			e1.comenzar(t8.getId());
	//		System.out.println(t8.getFechaInicio());  //porque salen los segundos?
			assertNotNull(t8.getFechaInicio());
		} catch (TareaException e) {
			e.printStackTrace();
		}

	}
//
	@Test
	public void testFinalizarInteger() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Tarea t1 = new Tarea(1,"Limpiar", 8, e1, "25-05-2020 00:22", false);
		try {
			e1.finalizar(t1.getId());
//			System.out.println(t1.getFechaFin()); 
			assertNotNull(t1.getFechaFin());
		} catch (TareaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testComenzarIntegerString() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Tarea t8 = new Tarea(7,"Limpiar", 8,false);
		e1.asignarTarea(t8);
		try {
			e1.comenzar(t8.getId(),"20-05-2020 08:00");
//			System.out.println(t8.getFechaInicio());  
			assertNotNull(t8.getFechaInicio());
		} catch (TareaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFinalizarIntegerString() {
		Empleado e1 = new Empleado(555,"Martin",Tipo.CONTRATADO, 500.0, true);
		Tarea t1 = new Tarea(1,"Limpiar", 8, e1, "25-05-2020 00:22", false);
		try {
			e1.finalizar(t1.getId(),"20-05-2020 18:00");
	//		System.out.println(t1.getFechaFin()); 
			assertNotNull(t1.getFechaFin());
		} catch (TareaException e) {
			e.printStackTrace();
		}
	}

}
