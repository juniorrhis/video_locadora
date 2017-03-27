package br.com.ufop.workers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.ufop.Worker;
import br.com.ufop.classes.Client;
import br.com.ufop.classes.Movie;
import br.com.ufop.classes.Payment;
import br.com.ufop.classes.Rent;
import br.com.ufop.classes.RentContent;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.K;
import br.com.ufop.utils.Methods;
import br.com.ufop.utils.Timer;

public class RentWorker extends Worker implements Runnable {
	private static AtomicInteger breakpoint;
	private static final String BREAKPOINT_FILE = "rent.breakpoint";
	
	public RentWorker() {
		Methods.log(getClass(), "Rent worker initialized!");
		
		breakpoint = getBreakpoint();
	}
	
	@Override
	protected String getBreakpointFile() {
		return BREAKPOINT_FILE;
	}
	
	public static void main(String[] args) {
		new Thread(new RentWorker()).start();
	}
	
	@Override
	protected void add() throws Exception {
		Timer timer = new Timer();
		
		Random random = new Random();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		if(client == null) {
			return;
		}
		
		int moviesAmount = random.nextInt(10);
		
		List<Movie> movies = PostgresData.getInstance().getRandomMovie(moviesAmount);
		
		if(movies == null) {
			return;
		}
		
		Rent rent = new Rent(breakpoint.getAndIncrement(), client.getCpf(), PostgresData.converterLongParaData(System.currentTimeMillis() + K.MILISEGUNDOS_IN_DAY * 3), true, random.nextDouble());
		
		PostgresData.getInstance().addRent(rent);
		
		for(Movie movie : movies) {
			try {
				RentContent rentContent = new RentContent(rent.getCodigo(), movie.getCodigo());
				
				PostgresData.getInstance().addRentContent(rentContent);

				movie.setQuantidade(movie.getQuantidade() - 1);
				HashMap<String, Object> updates = new HashMap<String, Object>();
				updates.put("quantidade", movie.getQuantidade());
				
				PostgresData.getInstance().updateMovie(movie, updates);
			} catch (Exception e) {}
		}
		
		Payment pagamento = new Payment(random.nextInt(Integer.MAX_VALUE), rent.getCodigo(), client.getCpf(), movies.size() * 5.0, PostgresData.converterLongParaData(System.currentTimeMillis() + K.MILISEGUNDOS_IN_DAY * 3), random.nextInt(11) < 6 ? "debito" : "credito");
		PostgresData.getInstance().addPayment(pagamento);
		
		setBreakpoint(breakpoint.get());
		
		Methods.log(getClass(), "Rent ( " + rent.getCodigo() + " ) Added. Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Timer timer = new Timer();
		
		Rent rent = PostgresData.getInstance().getRandomRent();
		
		if(rent == null) {
			return;
		}
		
		PostgresData.getInstance().removeRent(rent.getCodigo());
		
		Methods.log(getClass(), "Rent ( " + rent.getCodigo() + " ) Removed. Timer: " + timer.getTime() + " s.");
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
				int random = new Random().nextInt(11);
				
				// De 5 em 5 minutos
				if(System.currentTimeMillis() - timer > 1000*60*5) {
					addProbality = randomValue.nextInt(5) + 5;
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
				
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void update() throws Exception {
		Timer timer = new Timer();
		
		Rent rent = PostgresData.getInstance().getRandomRent();
		
		if(rent == null) {
			return;
		}
		
		HashMap<String, Object> updates = new HashMap<String, Object>();
		updates.put("pendente", false);
		
		PostgresData.getInstance().updateRent(rent, updates);
		
		Methods.log(getClass(), "Rent ( " + rent.getCodigo() + " ) Updated. Timer: " + timer.getTime() + " s.");
	}
}
