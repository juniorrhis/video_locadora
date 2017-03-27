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
		
		Methods.log(getClass(), "Client worker initialized!");
	}
	
	public static void main(String[] args) throws Exception {
		new ClientWorker().add();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		System.out.println(client.getCpf());
		
		PostgresData.getInstance().removeClient(client.getCpf());
		PostgresData.getInstance().updateClient(client);
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

				Thread.sleep(150);
				setBreakpoint(breakpoint.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		
		Methods.log(getClass(), "Client ( " + client.getCpf() + " ) Added. Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void remove() throws Exception {
		Timer timer = new Timer();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		if(client == null) {
			return;
		}
		
		PostgresData.getInstance().removeClient(client.getCpf());
		
		Methods.log(getClass(), "Client ( " + client.getCpf() + " ) Removed. Timer: " + timer.getTime() + " s.");
	}

	@Override
	protected void update() throws Exception {
		Timer timer = new Timer();
		
		Client client = PostgresData.getInstance().getRandomClient();
		
		if(client == null) {
			return;
		}
		
		int value = new Random().nextInt(7);
		
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
				client.setEndereco(RandomStringUtils.randomAlphabetic(150));
				client.setuNome(RandomStringUtils.randomAlphabetic(20));
				break;
			case 4:
				client.setmNome(RandomStringUtils.randomAlphabetic(20));
				client.setuNome(RandomStringUtils.randomAlphabetic(20));
				break;
			case 5:
				client.setpNome(RandomStringUtils.randomAlphabetic(20));
				client.setuNome(RandomStringUtils.randomAlphabetic(20));
				break;
			case 6:
				client.setpNome(RandomStringUtils.randomAlphabetic(20));
				client.setEndereco(RandomStringUtils.randomAlphabetic(150));
				break;
			case 7:
				client.setpNome(RandomStringUtils.randomAlphabetic(20));
				client.setmNome(RandomStringUtils.randomAlphabetic(20));
				client.setuNome(RandomStringUtils.randomAlphabetic(20));
				client.setEndereco(RandomStringUtils.randomAlphabetic(150));
				break;
		}
		PostgresData.getInstance().updateClient(client);
		
		Methods.log(getClass(), "Client ( " + client.getCpf() + " ) Updated! Timer: " + timer.getTime() + " s.");
	}
}
