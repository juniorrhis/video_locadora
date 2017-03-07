package br.com.ufop.workers;

import java.util.List;
import java.util.Random;

import br.com.ufop.classes.Aluguel;
import br.com.ufop.classes.Cliente;
import br.com.ufop.classes.Filme;
import br.com.ufop.database.PostgresData;

public class Rent {
	public static void makeRent() throws Exception {
		Cliente client = PostgresData.getInstance().getRandomClient();
		
		System.out.println(client);
		
		int moviesAmount = new Random().nextInt(10);
		
		List<Filme> movies = PostgresData.getInstance().getRandomMovies(moviesAmount);
		
		for(Filme movie : movies) {
		}
	}
}
