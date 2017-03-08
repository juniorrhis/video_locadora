package br.com.ufop.classes;

public class RentContent {
	private long codigoAluguel;
	private long codigoFilme;
	
	public RentContent(long codigoAluguel, long codigoFilme) {
		this.codigoAluguel = codigoAluguel;
		this.codigoFilme = codigoFilme;
	}

	public long getCodigoAluguel() {
		return codigoAluguel;
	}
	
	public long getCodigoFilme() {
		return codigoFilme;
	}
}
