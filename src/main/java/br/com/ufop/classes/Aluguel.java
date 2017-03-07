package br.com.ufop.classes;

public class Aluguel {
	private long codigo;
	private long cpfCliente;
	private String dataEntrega;
	private boolean pendente;
	
	public Aluguel(long codigo, long cpfCliente, String dataEntrega, boolean pendente) {
		this.codigo = codigo;
		this.cpfCliente = cpfCliente;
		this.dataEntrega = dataEntrega;
		this.pendente = pendente;
	}
	
	public Aluguel(long cpfCliente, String dataEntrega) {
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
}
