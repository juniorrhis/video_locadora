package br.com.ufop.test;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ufop.workers.AddMovie;

public class VideoLocadoraTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@BeforeClass
	public void before() {
		new Thread(new AddMovie()).start();
	}
}
