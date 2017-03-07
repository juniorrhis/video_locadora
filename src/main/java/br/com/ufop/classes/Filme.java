package br.com.ufop.classes;

public class Filme {
	private long codigo;
	private String titulo;
	private int lancamento;
	private int duracao;
	private String genero;
	private String sinopse;
	private String diretor;
	private int quantidade;
	private String dataDisponivel;
	private String capa;
	private Double random;
	
	public Filme(long codigo, String titulo, int lancamento, int duracao, String genero, String sinopse, String diretor, int quantidade, String dataDisponivel, String capa, Double random) {
		this.codigo = codigo;
		this.titulo = titulo;
		this.lancamento = lancamento;
		this.duracao = duracao;
		this.genero = genero;
		this.sinopse = sinopse;
		this.diretor = diretor;
		this.quantidade = quantidade;
		this.dataDisponivel = dataDisponivel;
		this.capa = capa;
		this.random = random;
	}
	
	public long getCodigo() {
		return codigo;
	}
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getLancamento() {
		return lancamento;
	}
	public void setLancamento(int lancamento) {
		this.lancamento = lancamento;
	}
	public int getDuracao() {
		return duracao;
	}
	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getSinopse() {
		return sinopse;
	}
	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}
	public String getDiretor() {
		return diretor;
	}
	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}
	
	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getDataDisponivel() {
		return dataDisponivel;
	}
	
	public void setDataDisponivel(String dataDisponivel) {
		this.dataDisponivel = dataDisponivel;
	}
	
	public String getCapa() {
		return capa;
	}

	public void setCapa(String capa) {
		this.capa = capa;
	}
	
	public Double getRandom() {
		return random;
	}

	public void setRandom(Double random) {
		this.random = random;
	}

	@Override
	public String toString() {
		return "codigo = " + codigo + "\n" +
			   "titulo = " + titulo + "\n" +
			   "lancamento = " + lancamento + "\n" + 
			   "duracao = " + duracao + "\n" +
			   "genero = " + genero + "\n" +
			   "sinopse = " + sinopse + "\n" +
			   "capa = " + capa + "\n" +
			   "diretor = " + diretor  + "\n" +
			   "quantidade = " + quantidade + "\n" +
			   "data disponível = " + dataDisponivel; 
	}
}
