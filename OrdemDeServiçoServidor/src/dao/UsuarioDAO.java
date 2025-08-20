package dao;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entities.Usuario;

public class UsuarioDAO {
	
	private Connection conn;
	
	public UsuarioDAO(Connection conn) {
		this.conn = conn;
	}
	public void atualizarUsuario(String token, String novoUsuario, String novoNome, String novaSenha) throws SQLException {
	    PreparedStatement st = null;

	    try {
	        //System.out.println("Atualizando usuário no banco: " + token); // Log para verificar o usuario original
	        st = conn.prepareStatement("UPDATE usuario SET nome_usuario = ?, nome = ?, senha = ? WHERE nome_usuario = ?");
	        
	        st.setString(1, novoUsuario);
	        st.setString(2, novoNome);
	        st.setString(3, novaSenha);
	        st.setString(4, token); // 🔹 O usuário é identificado pelo token

	        int linhasAfetadas = st.executeUpdate();
	        if (linhasAfetadas > 0) {
	            //System.out.println("usuario atualizado no banco com sucesso: " + novoUsuario);
	        } else {
	            //System.out.println("usuario não encontrado.");
	        }

	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.desconectar();
	    }
	}
	
	public void atualizarUsuarioAdmin(String usuarioAlvo, String novoNome, String novaSenha, String novoPerfil) throws SQLException {
	    PreparedStatement st = null;

	    try {
	        //System.out.println("Atualizando usuário no banco: " + token); // Log para verificar o usuario original
	        st = conn.prepareStatement("UPDATE usuario SET nome = ?, senha = ?, perfil = ? WHERE nome_usuario = ?");
	        
	     
	        st.setString(1, novoNome);
	        st.setString(2, novaSenha);
	        st.setString(3, novoPerfil); 
	        st.setString(4, usuarioAlvo);
	        int linhasAfetadas = st.executeUpdate();
	        if (linhasAfetadas > 0) {
	            //System.out.println("usuario atualizado no banco com sucesso: " + novoUsuario);
	        } else {
	            //System.out.println("usuario não encontrado.");
	        }

	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.desconectar();
	    }
	}
	



	
	public List<Usuario> buscarUsuarios() throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT a.*" + "FROM usuario a");
			rs = st.executeQuery();
			
			List<Usuario> listaUsuarios = new ArrayList<>();
			
			while(rs.next()) {
				
				Usuario usuario = new Usuario();
				
				usuario.setNome(rs.getString("nome"));
				usuario.setNomeUsuario(rs.getString("nome_usuario"));
				usuario.setPerfil(rs.getString("perfil"));
				
				listaUsuarios.add(usuario);
			}
			
			return listaUsuarios;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
			
		}
		
	}
	
	public void cadastrarUsuario(Usuario usuario) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("INSERT IGNORE INTO usuario (nome_usuario, senha, nome, perfil) VALUES ( ?, ?, ?, ?)");
			
			st.setString(1, usuario.getNomeUsuario());
			st.setString(2, usuario.getSenha());
			st.setString(3, usuario.getNome());
			st.setString(4, usuario.getPerfil());
			st.executeUpdate();
			
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	public Usuario buscarUsuarioPorNome(String nomeUsuario) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    Usuario usuario = null;

	    try {
	        st = conn.prepareStatement("SELECT nome_usuario, senha, nome, perfil FROM usuario WHERE nome_usuario = ?");
	        st.setString(1, nomeUsuario);
	        rs = st.executeQuery();

	        if (rs.next()) { // Se encontrar um usuário, cria o objeto
	            usuario = new Usuario();
	            usuario.setNomeUsuario(rs.getString("nome_usuario"));
	            usuario.setSenha(rs.getString("senha"));
	            usuario.setNome(rs.getString("nome"));
	            usuario.setPerfil(rs.getString("perfil"));
	        }

	    } finally {
	        BancoDados.finalizarResultSet(rs);
	        BancoDados.finalizarStatement(st);
	        BancoDados.desconectar();
	    }

	    return usuario; // Retorna os dados do usuário (ou null se não encontrado)
	}

	public boolean validarNomeUsuario(String usuario) throws SQLException{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT nome_usuario FROM usuario WHERE ? = nome_usuario");
			st.setString(1, usuario);
			rs = st.executeQuery();
		//	if(rs.getString("nome_usuario") == usuario && rs.getString("senha") == senha ) {
			if(!rs.next()) {
				//System.out.println("Usuario valido");
				return true;
			}else {
				//System.out.println("Nome de usuario já cadastrado");
				return false;
			}
		}
		finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public boolean validarSenhaUsuario(String usuario, String senha) throws SQLException{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT nome_usuario, senha FROM usuario WHERE nome_usuario COLLATE utf8_bin = ? and senha = ?");
			st.setString(1, usuario);
			st.setString(2, senha);
			rs = st.executeQuery();
		//	if(rs.getString("nome_usuario") == usuario && rs.getString("senha") == senha ) {
			if(rs.next()) {
				//System.out.println("senha correta");
				return true;
			}else {
				//System.out.println("senha ou usuario incorreto");
				return false;
			}
		}
		finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	public boolean validarAdmin(String usuario) throws SQLException{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT nome_usuario FROM usuario WHERE nome_usuario COLLATE utf8_bin = ? and perfil = 'adm'");
			st.setString(1, usuario);
			rs = st.executeQuery();
		
			if(rs.next()) {
				//System.out.println("usuario é adm mesmo pode confiar");
				return true;
			}else {
				return false;
			}
		}
		finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	public boolean excluirUsuario(String token) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("DELETE FROM usuario WHERE nome_usuario = ?");
			
			st.setString(1, token);
			
			int linhasAfetadas = st.executeUpdate();
	        if (linhasAfetadas > 0) {
	            //System.out.println("usuario deletado no banco com sucesso: ");
	            return true;
	        } else {
	            //System.out.println("usuário não encontrado.");
	            return false;
	        }
		
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
}