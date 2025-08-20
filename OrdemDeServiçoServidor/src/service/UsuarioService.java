package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.BancoDados;
import dao.UsuarioDAO;
import entities.Usuario;

public class UsuarioService {
	
	public UsuarioService() {
		
	}
	public void editarUsuario(String token, String novoUsuario, String novoNome, String novaSenha) throws SQLException, IOException {
	    Connection conn = BancoDados.conectar();
	    new UsuarioDAO(conn).atualizarUsuario(token, novoUsuario, novoNome, novaSenha);
	}
	
	public void editarUsuarioAdmin(String usuarioAlvo, String novoNome, String novaSenha, String novoPerfil) throws SQLException, IOException {
	    Connection conn = BancoDados.conectar();
	    new UsuarioDAO(conn).atualizarUsuarioAdmin(usuarioAlvo, novoNome, novaSenha,novoPerfil);
	}

	public List<Usuario> buscarUsuarios() throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).buscarUsuarios();
	}
	
	public void cadastrar(Usuario usuario) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		new UsuarioDAO(conn).cadastrarUsuario(usuario);
	}
	public Usuario lerDadosUsuario(String token) throws SQLException, IOException {
	    Connection conn = BancoDados.conectar();
	    return new UsuarioDAO(conn).buscarUsuarioPorNome(token);
	}

	public boolean validarSenhaUsuario(String usuario, String senha) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return (new UsuarioDAO(conn).validarSenhaUsuario(usuario,senha));
	}
	public boolean validarAdmin(String usuario) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return (new UsuarioDAO(conn).validarAdmin(usuario));
	}
	public boolean validarNomeUsuario(String usuario) throws SQLException, IOException {
	
		Connection conn = BancoDados.conectar();
		return (new UsuarioDAO(conn).validarNomeUsuario(usuario));
	}

	public boolean excluirUsuario(String token) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		return (new UsuarioDAO(conn).excluirUsuario(token));
	}

}