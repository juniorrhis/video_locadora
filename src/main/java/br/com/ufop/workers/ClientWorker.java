package br.com.ufop.workers;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.ufop.Worker;
import br.com.ufop.classes.Client;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.Timer;
import br.com.ufop.utils.Utils;

public class ClientWorker extends Worker implements Runnable {
	
	private static final String BREAKPOINT_FILE = "clients.breakpoint";
	private static int breakpoint;
	
	public ClientWorker() {
		breakpoint = getBreakpoint();
	}
	
	public void run() {
		while(true) {
			try {
				int random = new Random().nextInt(10) + 1;
				
				if(random < 8) {
					add();
				} else {
					remove();
				}
				
				Thread.sleep(2500);
				setBreakpoint(breakpoint);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new ClientWorker()).start();
	}
	
	@Override
	protected String getBreakpointFile() {
		return BREAKPOINT_FILE;
	}

	@Override
	protected void add() throws Exception {
		Timer timer = new Timer();
		
		Client client = new Client(breakpoint++, 
									 RandomStringUtils.randomAlphabetic(20), 
									 RandomStringUtils.randomAlphabetic(20), 
									 RandomStringUtils.randomAlphabetic(20), 
									 RandomStringUtils.randomAlphabetic(150), 
									 new Random().nextDouble());
		
		PostgresData.getInstance().addCliente(client);
		
		setBreakpoint(breakpoint);
		
		Utils.log(getClass(), "Client Added! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Timer timer = new Timer();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		PostgresData.removeClient(client.getCpf());
		
		Utils.log(getClass(), "Client Removed! Timer: " + timer.getTime() + " s.");
	}
}
