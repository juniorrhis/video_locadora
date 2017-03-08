package br.com.ufop.classes;

public class Debit {
	private long codigoPagamento;
	private double valorComDesconto;
	
	public Debit(long codigoPagamento, double valorComDesconto) {
		this.codigoPagamento = codigoPagamento;
		this.valorComDesconto = valorComDesconto;
	}
	public long getCodigoPagamento() {
		return codigoPagamento;
	}
	public void setCodigoPagamento(long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}
	public double getValorComDesconto() {
		return valorComDesconto;
	}
	public void setValorComDesconto(double valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}
}
