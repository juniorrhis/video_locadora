package br.com.ufop.workers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import br.com.ufop.Worker;
import br.com.ufop.classes.Rent;
import br.com.ufop.classes.Client;
import br.com.ufop.classes.RentContent;
import br.com.ufop.classes.Movie;
import br.com.ufop.classes.Payment;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.K;
import br.com.ufop.utils.Timer;
import br.com.ufop.utils.Utils;

public class RentWorker extends Worker implements Runnable {
	private static int breakpoint;
	private static final String BREAKPOINT_FILE = "rent.breakpoint";
	
	public RentWorker() {
		breakpoint = getBreakpoint();
	}
	
	@Override
	protected String getBreakpointFile() {
		return BREAKPOINT_FILE;
	}
	
	@Override
	protected void add() throws Exception {
		Timer timer = new Timer();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		int moviesAmount = new Random().nextInt(10);
		
		List<Movie> movies = PostgresData.getInstance().getRandomMovie(moviesAmount);
		
		Rent rent = new Rent(breakpoint++, client.getCpf(), PostgresData.converterLongParaData(System.currentTimeMillis() + K.DIA_EM_MILISEGUNDOS * 3), true, new Random().nextDouble());
		
		PostgresData.getInstance().addRent(rent);
		
		for(Movie movie : movies) {
			RentContent conteudoAluguel = new RentContent(rent.getCodigo(), movie.getCodigo());
			
			PostgresData.addRentContent(conteudoAluguel);

			movie.setQuantidade(movie.getQuantidade() - 1);
			HashMap<String, Object> updates = new HashMap<String, Object>();
			updates.put("quantidade", movie.getQuantidade());
			
			PostgresData.updateMovie(movie, updates);
		}
		Payment pagamento = new Payment(rent.getCodigo(), client.getCpf(), movies.size() * 5.0, PostgresData.converterLongParaData(System.currentTimeMillis() + K.DIA_EM_MILISEGUNDOS * 3), new Random().nextInt(11) < 6 ? "debito" : "credito");
		PostgresData.getInstance().addPayment(pagamento);
		
		setBreakpoint(breakpoint);
		
		Utils.log(getClass(), "Rent Added! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Rent rent = PostgresData.getInstance().getRandomRent();
		
		HashMap<String, Object> updates = new HashMap<String, Object>();
		updates.put("pendente", false);
		
		PostgresData.getInstance().updateRent(rent, updates);
		
		Utils.log(getClass(), "Rent Removed!");
	}

	public void run() {
		while(true) {
			try {
				int random = new Random().nextInt(10) + 1;
				
				if(random < 6) {
					add();
				} else {
					remove();
				}
				
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
