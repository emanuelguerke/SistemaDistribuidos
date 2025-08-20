package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entities.Ordem;
import entities.Usuario;

public class OrdemDAO {
	private Connection conn;
	
	
	public OrdemDAO(Connection conn) {
		this.conn = conn;
	}
	
	public boolean validarAutor(String token, int id_ordem) throws SQLException{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT id FROM ordem WHERE autor COLLATE utf8_bin = ? and id = ?");
			st.setString(1, token);
			st.setInt(2, id_ordem);
			
			rs = st.executeQuery();
		
			if(rs.next()) {
				System.out.println("usuario é o autor");
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
	
	public boolean validarOrdemPendente(String token, int id_ordem) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	    	//tive que remover o token pra poder reutilizar a função no administrador
	    	//não deve causar problemas já que isso só vai executar na função do usuario comum se ele for o autor já na primeira verificação
	    	// st = conn.prepareStatement("SELECT status FROM ordem WHERE autor COLLATE utf8_bin = ? AND id = ?");
	    	st = conn.prepareStatement("SELECT status FROM ordem WHERE id = ?");
	        st.setInt(1, id_ordem);

	        rs = st.executeQuery();

	        if (rs.next()) {
	            String status = rs.getString("status");
	            return "pendente".equalsIgnoreCase(status);
	        } else {
	            return false;
	        }
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.desconectar();
	    }
	}
	public boolean ordemExiste(String token, int id_ordem) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	    	
	    	st = conn.prepareStatement("SELECT * FROM ordem WHERE id = ?");
	        st.setInt(1, id_ordem);

	        rs = st.executeQuery();

	        if (rs.next()) {
	            return true;
	        } else {
	            return false;
	        }
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.desconectar();
	    }
	}

	
	public void cadastrarOrdem(Ordem ordem, String token) throws SQLException {
			
			PreparedStatement st = null;
			
			try {
				st = conn.prepareStatement("INSERT IGNORE INTO ordem (descricao, status, autor) VALUES (?, ?, ?)");
				st.setString(1, ordem.getDescricao());
				st.setString(2, ordem.getStatus());
				st.setString(3, token);
				st.executeUpdate();
				
			} finally {
				BancoDados.finalizarStatement(st);
				BancoDados.desconectar();
			}
		}
	
	public void editarOrdem(Ordem ordem, String token) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("UPDATE ordem SET descricao = ? WHERE id = ? && autor = ?");
			st.setString(1, ordem.getDescricao());
			st.setInt(2, ordem.getId());
			st.setString(3, token);
			st.executeUpdate();
			
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public void alterarStatusOrdem(Ordem ordem, String token) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("UPDATE ordem SET status = ?,descricao = ? WHERE id = ?");
			st.setString(1, ordem.getStatus());
			st.setString(2, ordem.getDescricao());
			st.setInt(3, ordem.getId());
			st.executeUpdate();
			
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public List<Ordem> buscarOrdens(String token, String filtro) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			if(filtro.equals("todas")) {
				st = conn.prepareStatement("select * from ordem where ? = autor");
				st.setString(1, token);
				rs = st.executeQuery();
			}else{
				st = conn.prepareStatement("select * from ordem where ? = autor && status = ?");
				st.setString(1, token);
				st.setString(2, filtro);
				rs = st.executeQuery();
			}
			

			List<Ordem> listaOrdens = new ArrayList<>();

			while (rs.next()) {

				Ordem ordem = new Ordem();
				ordem.setId(rs.getInt("id"));
				ordem.setAutor(rs.getString("autor"));
				ordem.setDescricao(rs.getString("descricao"));
				ordem.setStatus(rs.getString("status"));

				listaOrdens.add(ordem);
			}

			return listaOrdens;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	public List<Ordem> buscarOrdensAdm(String token, String filtro) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			if(filtro.equals("todas")) {
				st = conn.prepareStatement("select * from ordem ");
				rs = st.executeQuery();
			}else {
				st = conn.prepareStatement("select * from ordem where status = ?");
				st.setString(1, filtro);
				rs = st.executeQuery();
			}
			

			List<Ordem> listaOrdens = new ArrayList<>();

			while (rs.next()) {

				Ordem ordem = new Ordem();
				ordem.setId(rs.getInt("id"));
				ordem.setAutor(rs.getString("autor"));
				ordem.setDescricao(rs.getString("descricao"));
				ordem.setStatus(rs.getString("status"));

				listaOrdens.add(ordem);
			}

			return listaOrdens;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
}
