package br.com.ufop.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ufop.utils.K;
import br.com.ufop.workers.ClientWorker;
import br.com.ufop.workers.MovieWorker;
import br.com.ufop.workers.RentWorker;
import br.com.ufop.workers.ReserveWorker;
import br.com.ufop.workers.SearchWorker;

public class VideoLocadoraTest {
	
	private static ExecutorService executor;

	@BeforeClass
	public void before() {
		executor = Executors.newFixedThreadPool(K.THREADS);
	}
	
	@Test
	public void stressTest() {
		for(int i = 0; i < K.MOVIE_WORKERS; i++) {
			executor.execute(new MovieWorker());
		}
		
		for(int i = 0; i < K.CLIENT_WORKERS; i++) {
			executor.execute(new ClientWorker());
		}
		
		for(int i = 0; i < K.RENT_WORKERS; i++) {
			executor.execute(new RentWorker());
		}
		
		for(int i = 0; i < K.RESERVE_WORKERS; i++) {
			executor.execute(new ReserveWorker());
		}
		
		for(int i = 0; i < K.SEARCH_WORKERS; i++) {
			executor.execute(new SearchWorker());
		}
	}
}
