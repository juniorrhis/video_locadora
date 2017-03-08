package br.com.ufop.classes;

public class Rent {
	private long codigo;
	private long cpfCliente;
	private String dataEntrega;
	private boolean pendente;
	private Double random;
	
	public Rent(long codigo, long cpfCliente, String dataEntrega, boolean pendente, Double random) {
		this.codigo = codigo;
		this.cpfCliente = cpfCliente;
		this.dataEntrega = dataEntrega;
		this.pendente = pendente;
		this.random = random;
	}
	
	public Rent(long cpfCliente, String dataEntrega) {
		this.cpfCliente = cpfCliente;
		this.dataEntrega = dataEntrega;
	}

	public long getCodigo() {
		return codigo;
	}
	
	public long getCpfCliente() {
		return cpfCliente;
	}
	
	public String getDataEntrega() {
		return dataEntrega;
	}

	public boolean getPendente() {
		return pendente;
	}

	public void setPendente(boolean pendente) {
		this.pendente = pendente;
	}

	public Double getRandom() {
		return random;
	}

	public void setRandom(Double random) {
		this.random = random;
	}
}
