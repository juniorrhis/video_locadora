package br.com.ufop.workers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.ufop.Worker;
import br.com.ufop.classes.Client;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.Methods;
import br.com.ufop.utils.Timer;

public class ClientWorker extends Worker implements Runnable {
	
	private static final String BREAKPOINT_FILE = "clients.breakpoint";
	private static AtomicInteger breakpoint;
	
	public ClientWorker() {
		breakpoint = getBreakpoint();
	}
	
	public void run() {
		while(true) {
			try {
				int random = new Random().nextInt(11);
				
				if(random < 8) {
					add();
				} else {
					if(random < 9) {
						remove();
					} else {
						update();
					}
				}
				
				Thread.sleep(2500);
				setBreakpoint(breakpoint.get());
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
		
		Client client = new Client(breakpoint.getAndIncrement(), 
									 RandomStringUtils.randomAlphabetic(20), 
									 RandomStringUtils.randomAlphabetic(20), 
									 RandomStringUtils.randomAlphabetic(20), 
									 RandomStringUtils.randomAlphabetic(150), 
									 new Random().nextDouble());
		
		PostgresData.getInstance().addCliente(client);
		
		setBreakpoint(breakpoint.get());
		
		Methods.log(getClass(), "Client Added! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Timer timer = new Timer();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		PostgresData.removeClient(client.getCpf());
		
		Methods.log(getClass(), "Client Removed! Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void update() throws Exception {
		Timer timer = new Timer();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		int value = new Random().nextInt(4);
		
		switch (value) {
			case 0:
				client.setEndereco(RandomStringUtils.randomAlphabetic(150));
				break;
			case 1:
				client.setmNome(RandomStringUtils.randomAlphabetic(20));
				break;
			case 2:
				client.setpNome(RandomStringUtils.randomAlphabetic(20));
				break;
			case 3:
				client.setuNome(RandomStringUtils.randomAlphabetic(20));
				break;
		}
		PostgresData.updateClient(client);
		
		Methods.log(getClass(), "Client Updated! Timer: " + timer.getTime() + " s.");
	}
}
