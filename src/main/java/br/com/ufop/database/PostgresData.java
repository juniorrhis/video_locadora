package br.com.ufop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

import br.com.ufop.classes.Aluguel;
import br.com.ufop.classes.Cliente;
import br.com.ufop.classes.ConteudoAluguel;
import br.com.ufop.classes.ConteudoReserva;
import br.com.ufop.classes.Credito;
import br.com.ufop.classes.Debito;
import br.com.ufop.classes.Filme;
import br.com.ufop.classes.Pagamento;
import br.com.ufop.classes.Reserva;

/**
 * @author Júnior Rhis
 * Métodos para manipulação do Banco de Dados
 */
public class PostgresData {
	public static final String TABLE_FILME = "filme";
	public static final String TABLE_ADMINISTRADOR = "administrador";
	public static final String TABLE_CLIENTE = "cliente";
	public static final String TABLE_RESERVA = "reserva";
	public static final String TABLE_ALUGUEL = "aluguel";
	private static final String TABLE_CONTEUDO_ALUGUEL = "conteudo_aluguel";
	private static final String TABLE_CONTEUDO_RESERVA = "conteudo_reserva";
	public static final String TABLE_PAGAMENTO = "pagamento";
	private static final String TABLE_CREDITO = "credito";
	private static final String TABLE_DEBITO = "debito";
	public static final long DIA_EM_MILISEGUNDOS = 86400000;
	private static Connection connection;
	
	private static PostgresData instance;
	
	public PostgresData() throws Exception {
		openConnection();
	}
	
	public static synchronized PostgresData getInstance() throws Exception {
		if(instance == null) {
			instance = new PostgresData();
		}
		return instance;
	}
	
	/**
     * Abre a conexão com o banco de dados.
     * @throws Exception se a operação falhar.
     */
	public static void openConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager
				.getConnection("jdbc:postgresql://localhost:5432/video_locadora",
						"admin", "password");
	}
	
	/**
     * Fecha a conexão com o banco de dados.
     * @throws Exception se a operação falhar.
     */
	public static void closeConnection() throws Exception {
		connection.close();
	}
	
	/**
     * Insere um cliente no Banco.
     * @param cliente - cliente a ser adicionado.
     * @return true - se a operação foi bem sucedida; false - caso contrário.
     */
	public boolean inserirCliente(Cliente cliente) {
		String query = "INSERT INTO " + TABLE_CLIENTE + "(cpf, pnome, mnome, unome, endereco, random) VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, cliente.getCpf());
			ps.setString(2, cliente.getpNome());
			ps.setString(3, cliente.getmNome());
			ps.setString(4, cliente.getuNome());
			ps.setString(5, cliente.getEndereco());
			ps.setDouble(6, cliente.getRandom());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
     * Remove um cliente do Banco.
     * @param cpf - cpf do cliente a ser removido.
     * @return true - se a operação foi bem sucedida; false - caso contrário.
     */
	public static boolean removerCliente(long cpf) {
		String query = "DELETE FROM " + TABLE_CLIENTE + " WHERE cpf = " + cpf;
		
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
     * Atualiza um cliente do Banco.
     * @param cliente - cliente a ser atualizado.
     * @return true - se a operação foi bem sucedida; false - caso contrário.
     */
	public static boolean atualizarCliente(Cliente cliente) {
		String query = "UPDATE " + TABLE_CLIENTE + " SET pnome = '"+ cliente.getpNome() +"',mnome = '" + cliente.getmNome() +"',unome = '" + cliente.getuNome() + "',endereco = '" + cliente.getEndereco() + "' WHERE cpf = " + cliente.getCpf();
		
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
     * Pesquisa todos clientes.
     * @return lista de clientes.
     */
	public static List<Cliente> pesquisarClientes() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String query = "SELECT * FROM " + TABLE_CLIENTE;
		
		try {
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Cliente cliente = new Cliente(rs.getLong("cpf"), rs.getString("pnome"), rs.getString("mnome"), rs.getString("unome"), rs.getString("endereco"));
				clientes.add(cliente);
			}
			
			return clientes;
		} catch (Exception e) {}
		
		return null;
	}
	
	/**
     * Pesquisa clientes que possuem um determinado primeiro nome.
     * @param pNome - primeiro nome a ser pesquisado.
     * @return lista de clientes; null - caso contrário.
     */
	public static List<Cliente> pesquisarClientesPeloPrimeiroNome(String pNome) {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String query = "SELECT * FROM " + TABLE_CLIENTE + " WHERE lower(pnome) = " + "'" + pNome.toLowerCase() + "'";	
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Cliente cliente = new Cliente(rs.getLong("cpf"), rs.getString("pnome"), rs.getString("mnome"), rs.getString("unome"), rs.getString("endereco"));
				clientes.add(cliente);
			}
			
			return clientes;
		} catch (Exception e) {}
		
		return null;
	}
	
	/**
	 * Pesquisa um cliente com um determinado cpf.
	 * @param cpf - cpf a ser pesquisado.
	 * @return cliente com o cpf pesquisado; null - caso contrário.
	 */
	public static Cliente pesquisarClientePeloCpf(long cpf) {
		String query = "SELECT * FROM " + TABLE_CLIENTE + " WHERE cpf = " + cpf;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				return new Cliente(rs.getLong("cpf"), rs.getString("pnome"), rs.getString("mnome"), rs.getString("unome"), rs.getString("endereco"));
			}
		} catch(Exception e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Verifica o login do cliente.
	 * @param cpf - cpf do cliente.
	 * @return retorna o cliente se o login for bem sucedido; null - caso contrário.
	 */
	public static Cliente loginCliente(long cpf) {
		String query = "SELECT * FROM " + TABLE_CLIENTE + " WHERE cpf = " + cpf;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				return new Cliente(rs.getLong("cpf"), rs.getString("pnome"), rs.getString("mnome"), rs.getString("unome"), rs.getString("endereco"));
			}
			
		} catch(Exception e) {e.printStackTrace();}
		
		return null;
	}

	/**
	 * Insere um filme no Banco.
	 * @param filme - filme a ser adicionado.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public boolean inserirFilme(Filme filme) {
		String query = "INSERT INTO " + TABLE_FILME + "(titulo, lancamento, duracao, genero, sinopse, diretor, quantidade, data_disponivel, capa, random) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, filme.getTitulo());
			ps.setInt(2, filme.getLancamento());
			ps.setInt(3, filme.getDuracao());
			ps.setString(4, filme.getGenero());
			ps.setString(5,  filme.getSinopse());
			ps.setString(6, filme.getDiretor());
			ps.setInt(7, filme.getQuantidade());
			ps.setString(8, filme.getDataDisponivel());
			ps.setString(9, filme.getCapa());
			ps.setDouble(10, filme.getRandom());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {e.printStackTrace();}
		
		return false;
	}
	
	/**
	 * Remove um filme do Banco.
	 * @param codigo - código do filme a ser removido.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public static boolean removerFilme(long codigo) {
		String query = "DELETE FROM " + TABLE_FILME + " WHERE codigo = " + codigo;
		
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			return true;
		} catch (Exception e) {}
		
		return false;
	}
	
	
	/**
	 * Atualiza um filme do Banco.
	 * @param filme - filme a ser atualizado.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public static boolean atualizarFilme(Filme filme) {
		String query = "UPDATE " + TABLE_FILME + " SET titulo = '"+ filme.getTitulo() +"',lancamento = " + filme.getLancamento() +",duracao = " + filme.getDuracao() + ",genero = '" + filme.getGenero() + "',sinopse = '" + filme.getSinopse() + "',diretor = '" + filme.getDiretor() + "',quantidade = '" + filme.getQuantidade() + "',data_disponivel = '" + filme.getDataDisponivel() + "',capa = ? WHERE codigo = " + filme.getCodigo();
		try {
			PreparedStatement stat2 = connection.prepareStatement(query);

			stat2.setString(1, filme.getCapa());
			stat2.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Pesquisa todos os filmes do Banco.
	 * @return lista de todos os filmes.
	 */
	public static List<Filme> pesquisarFilmes() {
		List<Filme> filmes = new ArrayList<Filme>();
		String query = "SELECT * FROM " + TABLE_FILME;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Filme filme = new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa"));
				filmes.add(filme);
			}
			
			return filmes;
		} catch (Exception e) {}
		
		return null;
	}
	
	/**
	 * Pesquisa todos os filmes com um determinado valor em um determidado campo.
	 * @param campo - String a ser pesquisado.
	 * @param valor - String valor a ser verificado.
	 * @return lista de filmes que satisfazem a combinação de campo e valor; null - caso contrário.
	 */
	public static List<Filme> pesquisarFilmesPorCampo(String campo, String valor) {
		List<Filme> filmes = new ArrayList<Filme>();
		
		String query = "SELECT * FROM " + TABLE_FILME + " WHERE lower(" + campo + ") = '" + valor.toLowerCase() + "'";	
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Filme filme = new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa"));
				filmes.add(filme);
			}
			
			return filmes;
		} catch (Exception e) {}
		
		return null;
	}
	
	/**
	 * Pesquisa todos os filmes com um determinado valor em um determidado campo.
	 * @param campo - String a ser pesquisado.
	 * @param valor - Integer a ser verificado.
	 * @return lista de filmes que satisfazem a combinação de campo e valor; null - caso contrário.
	 */
	public static List<Filme> pesquisarFilmesPorCampo(String campo, Integer valor) {
		List<Filme> filmes = new ArrayList<Filme>();
		
		String query = "SELECT * FROM " + TABLE_FILME + " WHERE " + campo + " = " + valor;	
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Filme filme = new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa"));
				filmes.add(filme);
			}
			
			return filmes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Pesquisa todos os filmes que possuam uma duração superior à uma determinada duração.
	 * @param duracao - Integer que representa a duração mínima em segundos.
	 * @return lista de filmes que com duração superior a informada; null - caso contrário.
	 */
	public static List<Filme> pesquisarFilmesPorDuracao(Integer duracao) {
		List<Filme> filmes = new ArrayList<Filme>();
		String query = "SELECT * FROM " + TABLE_FILME + " WHERE duracao <= " + duracao + " ORDER BY duracao";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Filme filme = new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa"));
				filmes.add(filme);
			}
			
			return filmes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Pesquisa um filme pelo código.
	 * @param codigo - código a ser pesquisado.
	 * @return filme com o código informado; null - caso contrário.
	 */
	public static Filme pesquisarFilmePeloCodigo(long codigo) {
		String query = "SELECT * FROM " + TABLE_FILME + " WHERE codigo = " + codigo;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				return new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Realiza o login do administrador.
	 * @param usuario - String representando o usuario do administrador.
	 * @param senha - String representando a senha do administrador.
	 * @return true se o login for bem sucedido; false - caso contrário.
	 */
	public static boolean loginAdministrador(String usuario, String senha) {
		String query = "SELECT * FROM " + TABLE_ADMINISTRADOR + " WHERE usuario = " + "'" + usuario + "' AND senha = " + "'" + senha + "'";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				return true;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Insere um reserva no Banco.
	 * @param reserva - reserva a ser inserida.
	 * @return true se a operação foi bem sucedida; false - caso contrário;
	 */
	public static boolean inserirReserva(Reserva reserva) {
		String query = "INSERT INTO " + TABLE_RESERVA + "(codigo, cpf_cliente, data) VALUES (?, ?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, reserva .getCodigo());
			ps.setLong(2, reserva .getCpfCliente());
			ps.setString(3, reserva.getData());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Insere o conteúdo de uma reserva no Banco.
	 * @param conteudoReserva - conteúdo da reserva; null - caso contrário.
	 */
	public static void inserirConteudoReserva(ConteudoReserva conteudoReserva) {
		String query = "INSERT INTO " + TABLE_CONTEUDO_RESERVA + "(codigo_reserva, codigo_filme) VALUES (?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, conteudoReserva .getCodigoReserva());
			ps.setLong(2, conteudoReserva.getCodigoFilme());
			
			ps.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insere um aluguel no Banco.
	 * @param aluguel - Aluguel a ser inserido.
	 * @return true se a operação foi bem sucedida; false - caso contrário;
	 */
	public static boolean inserirAluguel(Aluguel aluguel) {
		String query = "INSERT INTO " + TABLE_ALUGUEL + "(codigo, cpf_cliente, data_entrega) VALUES (?, ?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, aluguel.getCodigo());
			ps.setLong(2, aluguel.getCpfCliente());
			ps.setString(3, aluguel.getDataEntrega());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Pesquisa um aluguel com um determinado código.
	 * @param codigo - long representando o código do aluguel a ser pesquisado.
	 * @return aluguel com o código informado; null - caso contrário.
	 */
	public static Aluguel pesquisarAluguelPeloCodigo(long codigo) {
		String query = "SELECT * FROM " + TABLE_ALUGUEL + " WHERE codigo = " + codigo + " AND pendente = TRUE";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				return new Aluguel(rs.getLong("codigo"), rs.getLong("cpf_cliente"), rs.getString("data_entrega"), rs.getBoolean("pendente"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Insere o contéudo de um aluguel no Banco.
	 * @param conteudoAluguel - conteúdo do aluguel a ser inserido.
	 */
	public static void inserirConteudoAluguel(ConteudoAluguel conteudoAluguel) {
		String query = "INSERT INTO " + TABLE_CONTEUDO_ALUGUEL + "(codigo_aluguel, codigo_filme) VALUES (?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, conteudoAluguel.getCodigoAluguel());
			ps.setLong(2, conteudoAluguel.getCodigoFilme());
			
			ps.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insere um pagamento no Banco.
	 * @param pagamento - pagamento a ser inserido.
	 * @return true - se a operação for bem sucedida; false - caso contrário.
	 */
	public static boolean inserirPagamento(Pagamento pagamento) {
		String query = "INSERT INTO " + TABLE_PAGAMENTO + "(codigo, codigo_aluguel, cpf_cliente, valor, vencimento, tipo) VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, pagamento.getCodigo());
			ps.setLong(2, pagamento.getCodigoAluguel());
			ps.setLong(3, pagamento.getCpfCliente());
			ps.setDouble(4, pagamento.getValor());
			ps.setString(5, pagamento.getVencimento());
			ps.setString(6,  pagamento.getTipo());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Pesquisa os aluguéis pendentes de um determinado cliente.
	 * @param cliente - cliente o qual queira pesquisar os aluguéis.
	 * @return lista de aluguéis pendentes do cliente; null - caso contrário.
	 */
	public static List<Aluguel> pesquisarAlugueisCliente(Cliente cliente) {
		List<Aluguel> alugueis = new ArrayList<Aluguel>();
		String query = "SELECT * FROM " + TABLE_ALUGUEL + " WHERE cpf_cliente = " + cliente.getCpf() + " AND " + "pendente = TRUE";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				alugueis.add(new Aluguel(rs.getLong("codigo"), rs.getLong("cpf_cliente"), rs.getString("data_entrega"), rs.getBoolean("pendente")));
			}
			
			return alugueis;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Atualiza um aluguel do Banco.
	 * @param aluguel - aluguel a ser atualizado.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public static boolean atualizarAluguel(Aluguel aluguel) {
		String query = "UPDATE " + TABLE_ALUGUEL + " SET cpf_cliente = '" + aluguel.getCpfCliente() +"',data_entrega = '" + aluguel.getDataEntrega() + "',pendente = '" + aluguel.getPendente() + "' WHERE codigo = " + aluguel.getCodigo();
		
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Atualiza uma reserva do Banco.
	 * @param reserva - Reserva a ser atualizada.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public static boolean atualizarReserva(Reserva reserva) {
		String query = "UPDATE " + TABLE_RESERVA + " SET cpf_cliente = '" + reserva.getCpfCliente() +"',data = '" + reserva.getData() + "',pendente = '" + reserva.getPendente() + "' WHERE codigo = " + reserva.getCodigo();
		
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Pesquisa os filmes que compõem um aluguel feito por um cliente.
	 * @param codigoAluguel - código do aluguel.
	 * @return lista de filmes que compõem o aluguel.
	 */
	public static List<Filme> pesquisarFilmesAlugadosPeloCliente(long codigoAluguel) {
		List<Filme> filmes = new ArrayList<Filme>();
		String query = "SELECT * FROM " + TABLE_FILME + " AS F " + "," + TABLE_CONTEUDO_ALUGUEL + " AS CA " + "," + TABLE_ALUGUEL + " AS A " + " WHERE F.codigo = CA.codigo_filme AND CA.codigo_aluguel = A.codigo AND A.codigo = " + codigoAluguel;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()) {
				filmes.add(new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa")));
			}
			
			return filmes;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Insere um pagamento do tipo crédito no Banco.
	 * @param credito - pagamento do tipo crédito a ser adicionado.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public static boolean inserirPagamentoCredito(Credito credito) {
		String query = "INSERT INTO " + TABLE_CREDITO + "(codigo_pagamento, numero_cartao) VALUES (?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, credito.getCodigoPagamento());
			ps.setLong(2, credito.getNumeroCartao());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Insere um pagamento do tipo debito(à vista) no Banco.
	 * @param debito - pagamento do tipo debito a ser adicionado.
	 * @return true - se a operação foi bem sucedida; false - caso contrário.
	 */
	public static boolean inserirPagamentoDebito(Debito debito) {
		String query = "INSERT INTO " + TABLE_DEBITO + "(codigo_pagamento, valor_com_desconto) VALUES (?, ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, debito.getCodigoPagamento());
			ps.setDouble(2, debito.getValorComDesconto());
			
			ps.executeUpdate();
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Pesquisa as reservas feitas por um determinado cliente.
	 * @param cliente - cliente o qual queira se saber as reservas.
	 * @return lista de reservas feitas pelo cliente; null - caso contrário.
	 */
	public static List<Reserva> pesquisarReservasDoCliente(Cliente cliente) {
		List<Reserva> reservas = new ArrayList<Reserva>();
		
		String query = "SELECT * FROM " + TABLE_RESERVA + " WHERE cpf_cliente = " + cliente.getCpf() + " AND pendente = TRUE";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Reserva reserva = new Reserva(rs.getLong("codigo"), rs.getLong("cpf_cliente"), rs.getString("data"), rs.getBoolean("pendente"));
				reservas.add(reserva);
			}
			
			return reservas;
		} catch (Exception e) {}
		
		return null;
	}
	
	/**
	 * Pesquisa os filmes que compõem uma reserva feita por um cliente.
	 * @param codigoReserva - código da reserva.
	 * @return lista de filmes que compõem a reserva; null - caso contrário.
	 */
	public static List<Filme> pesquisarFilmesReservadosPeloCliente(long codigoReserva) {
		List<Filme> filmes = new ArrayList<Filme>();
		String query = "SELECT * FROM " + TABLE_FILME + " AS F " + "," + TABLE_CONTEUDO_RESERVA + " AS CR " + "," + TABLE_RESERVA + " AS R " + " WHERE F.codigo = CR.codigo_filme AND CR.codigo_reserva = R.codigo AND R.pendente = TRUE AND R.codigo = " + codigoReserva;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()) {
				filmes.add(new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa")));
			}
			
			return filmes;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Pesquisa todos os aluguéis do Banco.
	 * @return lista de todos os aluguéis;
	 */
	public static List<Aluguel> pesquisarTodosAlugueis() {
		List<Aluguel> alugueis = new ArrayList<Aluguel>();
		String query = "SELECT * FROM " + TABLE_ALUGUEL;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()) {
				alugueis.add(new Aluguel(rs.getLong("codigo"), rs.getLong("cpf_cliente"), rs.getString("data_entrega"), rs.getBoolean("pendente")));
			}
			
			return alugueis;

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Pesquisa um pagamento referente a um determinado aluguel.
	 * @param codigo_aluguel - código do aluguel o qual queira obter o pagamento.
	 * @return pagamento referente ao aluguel; null - caso contrário.
	 */
	public static Pagamento pesquisarPagamento(long codigo_aluguel) {
		String query = "SELECT * FROM " + TABLE_PAGAMENTO + " WHERE codigo_aluguel = " + codigo_aluguel;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				return new Pagamento(rs.getLong("codigo"), rs.getLong("codigo_aluguel"), rs.getLong("cpf_cliente"), rs.getDouble("valor"), rs.getString("vencimento"), rs.getString("tipo"));
			}

		} catch(Exception e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Pesquisa um pagamento do tipo crédito referente a um determinado pagamento.
	 * @param codigo_pagamento - código do pagamento o qual queira se obter o tipo.
	 * @return tipo credito referente ao pagameto.
	 */
	public static Credito pesquisarPagamentoCredito(long codigo_pagamento) {
		String query = "SELECT * FROM " + TABLE_CREDITO + " WHERE codigo_pagamento = " + codigo_pagamento;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				return new Credito(rs.getLong("codigo_pagamento"), rs.getLong("numero_cartao"));
			}

		} catch(Exception e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Pesquisa um pagamento do tipo debito(à vista) referente a um determinado pagamento.
	 * @param codigo_pagamento - código do pagamento o qual queira se obter o tipo.
	 * @return tipo debito referente ao pagameto.
	 */
	public static Debito pesquisarPagamentoDebito(long codigo_pagamento) {
		String query = "SELECT * FROM " + TABLE_DEBITO + " WHERE codigo_pagamento = " + codigo_pagamento;
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				return new Debito(rs.getLong("codigo_pagamento"), rs.getDouble("valor_com_desconto"));
			}

		} catch(Exception e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Pesquisa todas as reservas pendentes do Banco.
	 * @return lista de todas as reservas pendentes.
	 */
	public static List<Reserva> pesquisarTodasReservas() {
		List<Reserva> reservas = new ArrayList<Reserva>();
		String query = "SELECT * FROM " + TABLE_RESERVA + " WHERE pendente = true";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()) {
				reservas.add(new Reserva(rs.getLong("codigo"), rs.getLong("cpf_cliente"), rs.getString("data"), rs.getBoolean("pendente")));
			}
			
			return reservas;

		} catch(Exception e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Realiza a conversão de uma data do tipo long para um padrão.
	 * @param time - tempo em milisegundos.
	 * @return String contendo a data no formato dd/MM/yyyy.
	 */
	public static String converterLongParaData(long time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(new Date(time));
	}
	
	/**
	 * Realiza conversão de uma data em um padrão para uma data em milisegundos.
	 * @param data - String representando o padrão a ser convertido.
	 * @return long representando a data em milisegundos.
	 * @throws ParseException - se houver falha durante a conversão.
	 */
	public static long converterDataParaLong(String data) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date novaData = format.parse(data);
		return novaData.getTime();
	}
	
	public List<Filme> getRandomMovies(int amount) {
		List<Filme> movies = new ArrayList<Filme>();
		String query = "SELECT * FROM " + TABLE_FILME + " WHERE random < " + (new Random().nextDouble() + 0.5) + " LIMIT " + amount + " ORDER desc";
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				
				Filme filme = new Filme(rs.getLong("codigo"), rs.getString("titulo"), rs.getInt("lancamento"), rs.getInt("duracao"), rs.getString("genero"), rs.getString("sinopse"), rs.getString("diretor"), rs.getInt("quantidade"), rs.getString("data_disponivel"), rs.getString("capa"), rs.getDouble("random"));
				movies.add(filme);
			}
			
			return movies;
		} catch (Exception e) {}
		
		return null;
	}

	public Cliente getRandomClient() {
		String query = "SELECT * FROM " + TABLE_CLIENTE + " WHERE random < " + (new Random().nextDouble() + 0.5) + " ORDER desc";
		
		try {
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Cliente cliente = new Cliente(rs.getLong("cpf"), rs.getString("pnome"), rs.getString("mnome"), rs.getString("unome"), rs.getString("endereco"), rs.getDouble("random"));
				
				return cliente;
			}
			
		} catch (Exception e) {}
		
		return null;
	}
}