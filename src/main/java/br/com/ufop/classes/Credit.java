package br.com.ufop.classes;

public class Credit {
	private long codigoPagamento;
	private long numeroCartao;
	
	public Credit(long codigoPagamento, long numeroCartao) {
		this.codigoPagamento = codigoPagamento;
		this.numeroCartao = numeroCartao;
	}
	public long getCodigoPagamento() {
		return codigoPagamento;
	}
	public void setCodigoPagamento(long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}
	public long getNumeroCartao() {
		return numeroCartao;
	}
	public void setNumeroCartao(long numeroCartao) {
		this.numeroCartao = numeroCartao;
	}
	
}
