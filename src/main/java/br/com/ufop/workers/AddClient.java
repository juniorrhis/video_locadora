package br.com.ufop.workers;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.ufop.classes.Cliente;
import br.com.ufop.database.PostgresData;
import br.com.ufop.utils.Utils;

public class AddClient implements Runnable {
	
	private static int BREAKPOINT = 1;
	private static final String BREAKPOINT_FILE = "clients.breakpoint";
	
	public AddClient() {
		getBreakpoint();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Cliente client = new Cliente(BREAKPOINT++, RandomStringUtils.randomAlphabetic(20), RandomStringUtils.randomAlphabetic(20), RandomStringUtils.randomAlphabetic(20), RandomStringUtils.randomAlphabetic(150), new Random().nextDouble());
			
				PostgresData.getInstance().inserirCliente(client);
				
				setBreakpoint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			Cliente client = new Cliente(BREAKPOINT++, RandomStringUtils.randomAlphabetic(20), RandomStringUtils.randomAlphabetic(20), RandomStringUtils.randomAlphabetic(20), RandomStringUtils.randomAlphabetic(150), new Random().nextDouble());
		
			PostgresData.getInstance().inserirCliente(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getBreakpoint() {
		try {
			BREAKPOINT = Integer.parseInt(Utils.readFile(BREAKPOINT_FILE));
		} catch (Exception e) {
			BREAKPOINT = 1;
		}
	}
	
	private void setBreakpoint() {
		try {
			Utils.writeFile(BREAKPOINT_FILE, String.valueOf(BREAKPOINT), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
