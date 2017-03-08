package br.com.ufop.classes;

public class Reserve {
	private long codigo;
	private long cpfCliente;
	private String data;
	private boolean pendente;
	private Double random;
	
	public Reserve(long codigo, long cpfCliente, String data, boolean pendente, Double random) {
		this.codigo = codigo;
		this.cpfCliente = cpfCliente;
		this.data = data;
		this.pendente = pendente;
		this.random = random;
	}
	
	public Reserve(long cpfCliente, String data) {
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

	public Double getRandom() {
		return random;
	}

	public void setRandom(Double random) {
		this.random = random;
	}
}
