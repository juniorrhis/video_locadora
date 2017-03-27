package br.com.ufop.workers;

import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.ufop.Worker;
import br.com.ufop.classes.Movie;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.Methods;
import br.com.ufop.utils.Timer;

public class MovieWorker extends Worker implements Runnable {
	public MovieWorker() {
		Methods.log(getClass(), "Movie worker initialized!");
	}
	
	public static void main(String[] args) {
		new Thread(new MovieWorker()).start();
	}

	public void run() {
		Random randomValue = new Random();
		
		long timer = System.currentTimeMillis();
		
		int addProbality = randomValue.nextInt(5) + 5;
		int removeProbality = randomValue.nextInt(10 - addProbality) + addProbality + 1;
		
		Methods.log(getClass(), "Add probability: " + addProbality);
		Methods.log(getClass(), "Remove probability: " + removeProbality);
		
		while(true) {
			try {
				int random = randomValue.nextInt(11);
				
				// De 5 em 5 minutos
				if(System.currentTimeMillis() - timer > 1000*60*5) {
					addProbality = randomValue.nextInt(6) + 5;
					removeProbality = randomValue.nextInt(10 - addProbality) + addProbality + 1;
					
					Methods.log(getClass(), "Add probability changed to: " + addProbality);
					Methods.log(getClass(), "Remove probability changed to: " + removeProbality);
					timer = System.currentTimeMillis();
				}
				
				if(random < addProbality) {
					add();
				} else {
					if(random < removeProbality) {
						remove();
					} else {
						update();
					}
				}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Movie getMovie() {
		Random random = new Random();
		
		long codigo = random.nextInt(Integer.MAX_VALUE);
		int length = random.nextInt(51) + 50;
		
		String titulo = RandomStringUtils.randomAlphabetic(length);
		
		//1500 ~ 2017 
		int lancamento = random.nextInt(1501) + 67;
		
		int duracao = random.nextInt(20) + 101;
		
		length = random.nextInt(26) + 25;
		String genero = RandomStringUtils.randomAlphabetic(length);
		
		length = random.nextInt(601) + 400;
		String sinopse = RandomStringUtils.randomAlphabetic(length);
		
		length = random.nextInt(61) + 40;
		String diretor = RandomStringUtils.randomAlphabetic(length);
		
		int quantidade = random.nextInt(10) + 1;
		
		length = random.nextInt(61) + 40;
		String capa = RandomStringUtils.randomAlphabetic(length);
		
		Movie filme = new Movie(codigo, titulo, lancamento, duracao, genero, sinopse, diretor, quantidade, PostgresData.converterLongParaData(System.currentTimeMillis()), capa, random.nextDouble());
		
		return filme;
	}

	@Override
	protected String getBreakpointFile() {
		return null;
	}

	@Override
	protected void add() throws Exception {
		Timer timer = new Timer();
		
		Movie movie = getMovie();
		
		PostgresData.getInstance().addMovie(movie);
		Methods.log(getClass(), "Movie ( " + movie.getCodigo() + " ) Added! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Timer timer = new Timer();
		
		Movie movie = PostgresData.getInstance().getRandomMovie();
		
		if(movie != null) {
			PostgresData.getInstance().removeMovie(movie.getCodigo());
			
			Methods.log(getClass(), "Movie ( " + movie.getCodigo() + " ) Removed! Timer: " + timer.getTime() + " s.");
		}
	}

	@Override
	protected void update() throws Exception {
		Timer timer = new Timer();
		
		Random random = new Random();
		
		Movie movie = PostgresData.getInstance().getRandomMovie();
		
		if(movie != null) {
			int value = random.nextInt(8);
			
			HashMap<String, Object> updates = new HashMap<String, Object>();
			
			int length;
			
			switch (value) {
				case 0:
					length = random.nextInt(61) + 40;
					String diretor = RandomStringUtils.randomAlphabetic(length);
					
					movie.setDiretor(diretor);
					updates.put("diretor", diretor);
					break;
				case 1:
					length = random.nextInt(26) + 25;
					String genero = RandomStringUtils.randomAlphabetic(length);
					
					movie.setGenero(genero);
					updates.put("genero", genero);
					break;
				case 2:
					int duracao = random.nextInt(20) + 101;
					
					movie.setDuracao(duracao);
					updates.put("duracao", duracao);
					break;
				case 3:
					int quantidade = random.nextInt(10) + 1;
					
					movie.setQuantidade(quantidade);
					updates.put("quantidade", quantidade);
					break;
				case 4:
					length = random.nextInt(51) + 50;
					
					String titulo = RandomStringUtils.randomAlphabetic(length);
					movie.setTitulo(titulo);
					updates.put("titulo", titulo);
					break;
				case 5:
					int lancamento = random.nextInt(1501) + 67;
					
					movie.setLancamento(lancamento);
					updates.put("lancamento", lancamento);
					break;
				case 6:
					length = random.nextInt(601) + 400;
					String sinopse = RandomStringUtils.randomAlphabetic(length);
					
					movie.setSinopse(sinopse);
					updates.put("sinopse", sinopse);
					break;
				case 7:
					length = random.nextInt(61) + 40;
					String capa = RandomStringUtils.randomAlphabetic(length);
					
					movie.setCapa(capa);
					updates.put("capa", capa);
					break;
			}
			
			PostgresData.getInstance().updateMovie(movie, updates);
			
			Methods.log(getClass(), updates.keySet() + " - Movie ( " + movie.getCodigo() + " ) Updated. Timer: " + timer.getTime() + " s.");
		}
	}
}
