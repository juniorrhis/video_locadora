package br.com.ufop.workers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.ufop.classes.Filme;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.Utils;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class AddMovie implements Runnable {
	private static final String API_KEY = "1b08e93ed5aad04387a2907950f90cb3";
	private static final String POSTER_PATH = "https://image.tmdb.org/t/p/w185_and_h278_bestv2";
	
	private static int BREAKPOINT = 1;
	private static final String BREAKPOINT_FILE = "movies.breakpoint";
	
	public AddMovie() {
		getBreakpoint();
	}
	
	private void getBreakpoint() {
		try {
			BREAKPOINT = Integer.parseInt(Utils.readFile(BREAKPOINT_FILE));
		} catch (Exception e) {
			BREAKPOINT = 1;
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				List<Filme> filmes = getFilmes();
				
				for(Filme filme : filmes) {
					PostgresData.getInstance().inserirFilme(filme);
				}
				Utils.log(getClass(), filmes.size() + " Filmes Adicionados!");
				
				Thread.sleep(5000);
				setBreakpoint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setBreakpoint() {
		try {
			Utils.writeFile(BREAKPOINT_FILE, String.valueOf(BREAKPOINT), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		List<Filme> filmes = getFilmes();
		
		for(Filme filme : filmes) {
			PostgresData.getInstance().inserirFilme(filme);
		}
		Utils.log(AddMovie.class, filmes.size() + " Filmes Adicionados!");
	}
	
	private static List<Filme> getFilmes() {
		List<Filme> filmes = new ArrayList<>();
		
		TmdbMovies movies = new TmdbApi(API_KEY).getMovies();
		
		MovieResultsPage results = movies.getTopRatedMovies("en", BREAKPOINT++);
		
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
			
			Filme filme = new Filme(codigo, titulo, lancamento, duracao, genero, sinopse, diretor, quantidade, PostgresData.converterLongParaData(System.currentTimeMillis()), capa, new Random().nextDouble());
			
			filmes.add(filme);
		}
		return filmes;
	}
}
