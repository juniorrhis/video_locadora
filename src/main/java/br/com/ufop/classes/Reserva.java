package br.com.ufop.classes;

public class Reserva {
	private long codigo;
	private long cpfCliente;
	private String data;
	private boolean pendente;
	
	public Reserva(long codigo, long cpfCliente, String data, boolean pendente) {
		this.codigo = codigo;
		this.cpfCliente = cpfCliente;
		this.data = data;
		this.pendente = pendente;
	}
	
	public Reserva(long cpfCliente, String data) {
		this.cpfCliente = cpfCliente;
		this.data = data;
	}

	public long getCodigo() {
		return codigo;
	}
	
	public long getCpfCliente() {
		return cpfCliente;
	}
	
	public String getData() {
		return data;
	}

	public boolean getPendente() {
		return pendente;
	}

	public void setPendente(boolean pendente) {
		this.pendente = pendente;
	}
}
