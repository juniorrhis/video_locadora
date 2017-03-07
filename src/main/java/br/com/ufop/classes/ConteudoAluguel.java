package br.com.ufop.classes;

public class ConteudoAluguel {
	private long codigoAluguel;
	private long codigoFilme;
	
	public ConteudoAluguel(long codigoAluguel, long codigoFilme) {
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
