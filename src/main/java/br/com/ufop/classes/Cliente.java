package br.com.ufop.classes;

public class Cliente {
	private long cpf;
	private String pNome;
	private String mNome;
	private String uNome;
	private String endereco;
	private Double random;
	
	public Cliente(long cpf, String pNome, String mNome, String uNome, String endereco, Double random) {
		this.cpf = cpf;
		this.pNome = pNome;
		this.mNome = mNome;
		this.uNome = uNome;
		this.endereco = endereco;
		this.random = random;
	}

	public long getCpf() {
		return cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public String getpNome() {
		return pNome;
	}

	public void setpNome(String pNome) {
		this.pNome = pNome;
	}

	public String getmNome() {
		return mNome;
	}

	public void setmNome(String mNome) {
		this.mNome = mNome;
	}

	public String getuNome() {
		return uNome;
	}

	public void setuNome(String uNome) {
		this.uNome = uNome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public Double getRandom() {
		return random;
	}

	public void setRandom(Double random) {
		this.random = random;
	}

	@Override
	public String toString() {
		return "cpf = " + cpf + "\n" + 
			   "pNome = " + pNome + "\n" + 
			   "mNome = " + mNome + "\n" +
			   "uNome = " + uNome + "\n" +
			   "endereco = " + endereco + "\n";
	}
}
