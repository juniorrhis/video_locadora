package br.com.ufop.classes;

public class ConteudoReserva {
	private long codigoReserva;
	private long codigoFilme;
	
	public ConteudoReserva(long codigoReserva, long codigoFilme) {
		this.codigoReserva = codigoReserva;
		this.codigoFilme = codigoFilme;
	}

	public long getCodigoReserva() {
		return codigoReserva;
	}
	
	public long getCodigoFilme() {
		return codigoFilme;
	}
}
