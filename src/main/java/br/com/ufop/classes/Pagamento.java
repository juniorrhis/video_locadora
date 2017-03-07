package br.com.ufop.classes;

public class Pagamento {
	private long codigo;
	private long codigoAluguel;
	private long cpfCliente;
	private double valor;
	private String vencimento;
	private String tipo;
	
	
	public Pagamento(long codigo, long codigoAluguel, long cpfCliente, double valor, String vencimento, String tipo) {
		this.codigo = codigo;
		this.codigoAluguel = codigoAluguel;
		this.cpfCliente = cpfCliente;
		this.valor = valor;
		this.vencimento = vencimento;
		this.tipo = tipo;
	}
	
	public Pagamento(long codigoAluguel, long cpfCliente, double valor, String vencimento, String tipo) {
		this.codigoAluguel = codigoAluguel;
		this.cpfCliente = cpfCliente;
		this.valor = valor;
		this.vencimento = vencimento;
		this.tipo = tipo;
	}

	public long getCodigo() {
		return codigo;
	}
	
	public long getCodigoAluguel() {
		return codigoAluguel;
	}
	
	public long getCpfCliente() {
		return cpfCliente;
	}
	
	public double getValor() {
		return valor;
	}
	
	public String getVencimento() {
		return vencimento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
