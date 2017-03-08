package br.com.ufop.classes;

public class ReserveContent {
	private long codigoReserva;
	private long codigoFilme;
	
	public ReserveContent(long codigoReserva, long codigoFilme) {
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
