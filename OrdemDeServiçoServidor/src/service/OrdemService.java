package service;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.OrdemDAO;
import dao.BancoDados;

import dao.UsuarioDAO;
import entities.Ordem;
import entities.Usuario;


public class OrdemService {
	
	public void cadastrar(Ordem ordem, String token) throws SQLException, IOException {
			
			Connection conn = BancoDados.conectar();
			new OrdemDAO(conn).cadastrarOrdem(ordem, token);
	}
	
	public List<Ordem> buscarOrdens(String token, String filtro) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return new OrdemDAO(conn).buscarOrdens(token, filtro);
	}
	public List<Ordem> buscarOrdensAdm(String token, String filtro) throws SQLException, IOException {
			
			Connection conn = BancoDados.conectar();
			return new OrdemDAO(conn).buscarOrdensAdm(token, filtro);
	}
	public void editarOrdem(Ordem ordem, String token) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		new OrdemDAO(conn).editarOrdem(ordem, token);
	}
	public void alterarStatusOrdem(Ordem ordem, String token) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		new OrdemDAO(conn).alterarStatusOrdem(ordem, token);
	}
	
	public boolean validarAutor(String token, int id_ordem) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return (new OrdemDAO(conn).validarAutor(token,id_ordem));
	}
	
	public boolean validarOrdemPendente(String token, int id_ordem) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return (new OrdemDAO(conn).validarOrdemPendente(token,id_ordem));
	}
	public boolean ordemExiste(String token, int id_ordem) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return (new OrdemDAO(conn).ordemExiste(token,id_ordem));
	}
	
	
}
