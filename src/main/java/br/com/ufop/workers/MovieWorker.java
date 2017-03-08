package br.com.ufop.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.ufop.Worker;
import br.com.ufop.classes.Movie;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.Methods;
import br.com.ufop.utils.Timer;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MovieWorker extends Worker implements Runnable {
	private static final String API_KEY = "1b08e93ed5aad04387a2907950f90cb3";
	private static final String POSTER_PATH = "https://image.tmdb.org/t/p/w185_and_h278_bestv2";
	
	private static AtomicInteger breakpoint;
	private static final String BREAKPOINT_FILE = "movies.breakpoint";
	
	public MovieWorker() {
		breakpoint = getBreakpoint();
	}

	public void run() {
		while(true) {
			try {
				int random = new Random().nextInt(11);
				
				if(random < 8) {
					add();
				} else {
					update();
				}
				
				Thread.sleep(5000);
				setBreakpoint(breakpoint.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<Movie> getMovies() {
		List<Movie> movies = new ArrayList<Movie>();
		
		TmdbMovies moviesResult = new TmdbApi(API_KEY).getMovies();
		
		MovieResultsPage results = moviesResult.getTopRatedMovies("en", breakpoint.getAndIncrement());
		
		for(MovieDb movie : results.getResults()) {
			long codigo = movie.getId();
			String titulo = movie.getTitle();
			
			String[] splitedReleaseDate = movie.getReleaseDate().split("[-\\/]");
			
			int lancamento = Integer.parseInt(splitedReleaseDate[0]);
			
			int duracao = new Random().nextInt(20) + 101;
			
			String genero = RandomStringUtils.randomAlphabetic(20);
			
			String sinopse = movie.getOverview();
			
			String diretor = RandomStringUtils.randomAlphabetic(30);
			
			int quantidade = new Random().nextInt(10) + 1;
			
			String capa = POSTER_PATH + movie.getPosterPath();
			
			Movie filme = new Movie(codigo, titulo, lancamento, duracao, genero, sinopse, diretor, quantidade, PostgresData.converterLongParaData(System.currentTimeMillis()), capa, new Random().nextDouble());
			
			movies.add(filme);
		}
		return movies;
	}

	@Override
	protected String getBreakpointFile() {
		return BREAKPOINT_FILE;
	}

	@Override
	protected void add() throws Exception {
		Timer timer = new Timer();
		
		List<Movie> movies = getMovies();
		
		for(Movie movie : movies) {
			PostgresData.getInstance().addMovie(movie);
		}
		Methods.log(getClass(), movies.size() + " Added! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Timer timer = new Timer();
		
		Movie movie = PostgresData.getInstance().getRandomMovie();
		
		PostgresData.getInstance().removeMovie(movie.getCodigo());
		
		Methods.log(getClass(), "Movie removed! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void update() throws Exception {
		Timer timer = new Timer();
		
		Movie movie = PostgresData.getInstance().getRandomMovie();
		
		int value = new Random().nextInt(4);
		
		HashMap<String, Object> updates = new HashMap<String, Object>();
		
		switch (value) {
			case 0:
				movie.setDiretor(RandomStringUtils.randomAlphabetic(30));
				updates.put("diretor", movie.getDiretor());
				break;
			case 1:
				movie.setGenero(RandomStringUtils.randomAlphabetic(20));
				updates.put("genero", movie.getGenero());
				break;
			case 2:
				movie.setDuracao(new Random().nextInt(20) + 101);
				updates.put("duracao", movie.getDuracao());
				break;
			case 3:
				movie.setQuantidade(new Random().nextInt(10) + 1);
				updates.put("quantidade", movie.getQuantidade());
				break;
		}
		PostgresData.updateMovie(movie, updates);
		
		Methods.log(getClass(), "Movie Updated! Timer: " + timer.getTime() + " s.");
	}
}
