package service;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import dao.BancoDados;
import gui.ServidorGui;
import entities.Ordem;
import entities.Usuario;
import service.UsuarioService;
import util.Protocolo;
import service.OrdemService;

public class ClienteHandler implements Runnable {
    private Socket clientSocket;
    private String usuario;
    private String senha;
    private String perfil;
    private String nome;
    private String token;
    private String operacao;
    
    private String novo_nome;
    private String novo_usuario;
    private String nova_senha;
    private String novo_perfil;
   
    private String usuario_alvo;
    
    private String descricao;
    private String filtro;
    
    private int id_ordem;
    private String nova_descricao;
    private String novo_status;
    
    private boolean usuarioAutenticado = false;
    boolean sucesso = false;
    private ServidorGui servidorGui;
    private UsuarioService usuarioService;
	private OrdemService ordemService;

    

    public ClienteHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.usuarioService = new UsuarioService();
        this.ordemService = new OrdemService();
        this.servidorGui = ServidorGui.getInstance();
    }
    
    private void editarOrdem(String token, int idOrdem, String novaDescricao ) {
		
		try {
				Ordem ordem = new Ordem();
				this.ordemService = new OrdemService();
				
			
				ordem.setDescricao(novaDescricao);
				ordem.setId(idOrdem);
				
				this.ordemService.editarOrdem(ordem, token);
				
			
			}
			catch (SQLException | IOException | NumberFormatException | NullPointerException e) {
				
				
				System.out.println(e);
			}
		
			
			
	}
    
    private void alterarStatusOrdem(String token, int idOrdem, String novoStatus, String novaDescricao ) {
		
		try {
				Ordem ordem = new Ordem();
				this.ordemService = new OrdemService();
				
			
				ordem.setStatus(novoStatus);
				ordem.setDescricao(novaDescricao);
				ordem.setId(idOrdem);
				
				this.ordemService.alterarStatusOrdem(ordem, token);
				
			
			}
			catch (SQLException | IOException | NumberFormatException | NullPointerException e) {
				
				
				System.out.println(e);
			}
		
			
			
	}
	public void cadastrarOrdem(String token) {
			
			try {
					Ordem ordem = new Ordem();
					this.ordemService = new OrdemService();
					
					ordem.setAutor(token);
					ordem.setDescricao(descricao);
					ordem.setStatus("pendente");
					
					this.ordemService.cadastrar(ordem, token);
					
				
				}
				catch (SQLException | IOException | NumberFormatException | NullPointerException e) {
					
					//JOptionPane.showMessageDialog(null, "Errrrrrrou", "ERRO", JOptionPane.ERROR_MESSAGE);
					System.out.println(e);
				}
			
				
				
		}
    
    
	public boolean validarSenhaUsuario(String usuario, String senhaP) {
		try {
			if(this.usuarioService.validarSenhaUsuario(usuario, senhaP)) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException | IOException | NumberFormatException e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean validarAdmin(String usuario) {
		try {
			if(this.usuarioService.validarAdmin(usuario)) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException | IOException | NumberFormatException e) {
			System.out.println(e);
			return false;
		}
	}
	public boolean validarAutor(String token, int id_ordem) {
		try {
			if(this.ordemService.validarAutor(token, id_ordem)) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException | IOException | NumberFormatException e) {
			System.out.println(e);
			return false;
		}
	}
	public boolean validarOrdemPendente(String token, int id_ordem) {
		try {
			if(this.ordemService.validarOrdemPendente(token, id_ordem)) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException | IOException | NumberFormatException e) {
			System.out.println(e);
			return false;
		}
	}
	public boolean ordemExiste(String token, int id_ordem) {
		try {
			if(this.ordemService.ordemExiste(token, id_ordem)) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException | IOException | NumberFormatException e) {
			System.out.println(e);
			return false;
		}
	}
	public boolean validarNomeUsuario(String usuario) {
		
		try {
			
			if (this.usuarioService.validarNomeUsuario(usuario)) {
				return true;
				
			} else {
				return false;
			}
			
		} catch (SQLException | IOException | NumberFormatException e) {
			
			System.out.println(e);
			return false;
		}
	}
	
	public static boolean validarDescricao(String descricao) {
        if (descricao == null) {
            return false;
        }
        int tamanho = descricao.trim().length();
        return tamanho >= 3 && tamanho <= 150;
    }
    
	public boolean validarCampos(String usuario, String senhaP) {
	    String nomeUsuario = usuario;
	    String senha = senhaP;

	    // Regex para validar caracteres (apenas letras e números, sem espaços ou caracteres especiais)
	    String regex = "^[a-zA-Z0-9]+$";

	    // Verificando nome de usuário
	    if (nomeUsuario == null || nomeUsuario.isEmpty() || nomeUsuario.length() < 3 || nomeUsuario.length() > 30 || !nomeUsuario.matches(regex)) {
	       // System.err.println("Erro: Nome de usuário inválido! Deve ter entre 3 e 30 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }

	    // Verificando senha
	    if (senha == null || senha.isEmpty() || senha.length() < 4 || senha.length() > 10 || !senha.matches(regex)) {
	        //System.err.println("Erro: Senha inválida! Deve ter entre 4 e 10 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }

	    return true;
	}
	
	public boolean validarCamposCadastro(String usuario, String nomeP, String senhaP) {
	    String nomeUsuario = usuario;
	    String nome =  nomeP;
	    String senha = senhaP;

	    // Regex para validar caracteres (apenas letras e números, sem espaços ou caracteres especiais)
	    String regex = "^[a-zA-Z0-9]+$";

	    // Verificando nome de usuário
	    if (nomeUsuario == null || nomeUsuario.isEmpty() || nomeUsuario.length() < 3 || nomeUsuario.length() > 30 || !nomeUsuario.matches(regex)) {
	      //  System.err.println("Erro: Nome de usuário inválido! Deve ter entre 3 e 30 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }
	    //verifica nome
	    if (nome == null || nome.isEmpty() || nome.length() < 3 || nome.length() > 30 || !nome.matches(regex)) {
	    //    System.err.println("Erro: Nome de usuário inválido! Deve ter entre 3 e 30 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }
	    // Verificando senha
	    if (senha == null || senha.isEmpty() || senha.length() < 4 || senha.length() > 10 || !senha.matches(regex)) {
	    //    System.err.println("Erro: Senha inválida! Deve ter entre 4 e 10 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }

	    return true;
	}
	private void logoutUsuario(String token) {
	    if (servidorGui.usuarioJaConectado(token)) { // verifica corretamente se o usuário esta logado
	        servidorGui.removerUsuario(token); // remove o usuario da lista de logados
	        usuarioAutenticado = false; // marca como deslogado
	     //   System.out.println("usuario removido do sistema: " + token);
	    } else {
	    //    System.out.println("erro ao deslogar: token invalido - " + token);
	    }
	}
	public String buscarOrdens(String token, String filtro) {
        try {
            List<Ordem> ordens = this.ordemService.buscarOrdens(token, filtro);

            // Criando o objeto JSON com status e operação
            JsonObject respostaJson = new JsonObject();
            respostaJson.put("status", "sucesso");
            respostaJson.put("operacao", "listar_ordens");

            // Convertendo a lista de usuários para JSON
            JsonArray ordensArray = new JsonArray();
            for (Ordem ordem : ordens) {
                JsonObject ordemJson = new JsonObject();
                ordemJson.put("id", ordem.getId());
                ordemJson.put("descricao", ordem.getDescricao());
                ordemJson.put("status", ordem.getStatus());
                ordemJson.put("autor", ordem.getAutor());
                ordensArray.add(ordemJson);
            }

            respostaJson.put("ordens", ordensArray);
            //System.out.println(Jsoner.serialize(respostaJson));
            // Retorna a String JSON pronta
            return Jsoner.serialize(respostaJson);

        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar os dados das ordens.", "Listar Ordens", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);

            // Retorna um JSON padronizado para erro
            JsonObject erroJson = new JsonObject();
            erroJson.put("status", "erro");
            erroJson.put("operacao", "listar_ordens");
            erroJson.put("mensagem", "Erro ao carregar dados");
            
            return Jsoner.serialize(erroJson);
        }
    }
	public String buscarOrdensAdm(String token, String filtro) {
        try {
            List<Ordem> ordens = this.ordemService.buscarOrdensAdm(token, filtro);

            // Criando o objeto JSON com status e operação
            JsonObject respostaJson = new JsonObject();
            respostaJson.put("status", "sucesso");
            respostaJson.put("operacao", "listar_ordens");

            // Convertendo a lista de usuários para JSON
            JsonArray ordensArray = new JsonArray();
            for (Ordem ordem : ordens) {
                JsonObject ordemJson = new JsonObject();
                ordemJson.put("id", ordem.getId());
                ordemJson.put("descricao", ordem.getDescricao());
                ordemJson.put("status", ordem.getStatus());
                ordemJson.put("autor", ordem.getAutor());
                ordensArray.add(ordemJson);
            }

            respostaJson.put("ordens", ordensArray);
            //System.out.println(Jsoner.serialize(respostaJson));
            // Retorna a String JSON pronta
            return Jsoner.serialize(respostaJson);

        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar os dados das ordens.", "Listar Ordens", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);

            // Retorna um JSON padronizado para erro
            JsonObject erroJson = new JsonObject();
            erroJson.put("status", "erro");
            erroJson.put("operacao", "listar_ordens");
            erroJson.put("mensagem", "Erro ao carregar dados");
            
            return Jsoner.serialize(erroJson);
        }
    }
	
	 public String buscarUsuarios() {
	        try {
	            List<Usuario> usuarios = this.usuarioService.buscarUsuarios();

	            // Criando o objeto JSON com status e operação
	            JsonObject respostaJson = new JsonObject();
	            respostaJson.put("status", "sucesso");
	            respostaJson.put("operacao", "listar_usuarios");

	            // Convertendo a lista de usuários para JSON
	            JsonArray usuariosArray = new JsonArray();
	            for (Usuario usuario : usuarios) {
	                JsonObject usuarioJson = new JsonObject();
	                usuarioJson.put("nome", usuario.getNome());
	                usuarioJson.put("usuario", usuario.getNomeUsuario());
	                usuarioJson.put("perfil", usuario.getPerfil());
	                usuariosArray.add(usuarioJson);
	            }

	            respostaJson.put("usuarios", usuariosArray);
	            //System.out.println(Jsoner.serialize(respostaJson));
	            // Retorna a String JSON pronta
	            return Jsoner.serialize(respostaJson);

	        } catch (SQLException | IOException e) {
	            JOptionPane.showMessageDialog(null, "Erro ao carregar os dados dos usuários.", "Buscar Usuários", JOptionPane.ERROR_MESSAGE);
	            System.out.println(e);

	            // Retorna um JSON padronizado para erro
	            JsonObject erroJson = new JsonObject();
	            erroJson.put("status", "erro");
	            erroJson.put("operacao", "buscar_usuarios");
	            erroJson.put("mensagem", "Erro ao carregar dados");
	            
	            return Jsoner.serialize(erroJson);
	        }
	    }

	 
	private void cadastrarUsuario(String usuarioP, String nomeP, String senhaP, String perfilP) {
		
		try {
			
				
				Usuario usuarioC = new Usuario();
				this.usuarioService = new UsuarioService();
				
				usuarioC.setNomeUsuario(usuarioP);
				usuarioC.setNome(nomeP);
				usuarioC.setSenha(senhaP);
				
				if(validarAdmin(token)) {
					usuarioC.setPerfil(perfilP);
				}else {
					usuarioC.setPerfil("comum");
				}
				
				
				this.usuarioService.cadastrar(usuarioC);
			
			}
			catch (SQLIntegrityConstraintViolationException e) {
				System.out.println(e);
				JOptionPane.showMessageDialog(null, "Nome de usuario já cadastrado.", "ERRO", JOptionPane.ERROR_MESSAGE);
				
			}
			catch (SQLException | IOException e) {
				JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar um novo usuário.", "ERRO", JOptionPane.ERROR_MESSAGE);
				System.out.println(e);
			}
				
	}
	
	private void editarUsuario(String token, String novoUsuario, String novoNome, String novaSenha) {
	    try {
	        this.usuarioService = new UsuarioService();
	        this.usuarioService.editarUsuario(token, novoUsuario, novoNome, novaSenha);

	        // Atualiza corretamente a lista de usuários logados
	        if (servidorGui.usuarioJaConectado(token)) {
	            servidorGui.removerUsuario(token); // Remove o antigo usuário
	        }
	        servidorGui.adicionarUsuario(novoUsuario); // Adiciona o novo usuário

	        System.out.println("usuario atualizado no sistema: " + token + " → " + novoUsuario);

	    } catch (SQLException | IOException e) {
	        System.out.println("erro ao atualizar usuario: " + e.getMessage());
	    }
	}
	
	private boolean editarUsuarioAdmin(String usuarioAlvo, String novoNome, String novaSenha, String novoPerfil) {
	    try {
	    	if(usuario_alvo.equals(token) && novoPerfil.equals("comum")) {
        		System.out.println("administrador não pode remover seus previlégios");
        		return false;
        	}else {
        		this.usuarioService = new UsuarioService();
     	        this.usuarioService.editarUsuarioAdmin(usuarioAlvo, novoNome, novaSenha,novoPerfil);
     	        System.out.println("usuario atualizado no sistema: ");
     	       return true;
        	}
	       
	    	
	    } catch (SQLException | IOException e) {
	        System.out.println("erro ao atualizar usuario: " + e.getMessage());
	        return false;
	    }
	    
	    
	}
	

	private void excluirUsuarioAdmin(String token) throws IOException {
	    if (servidorGui.usuarioJaConectado(token)) { // Verifica se o usuário está logado
	        try {
	        	
	        	this.usuarioService = new UsuarioService(); // Instancia o serviço corretamente
	        	
	        	
	        	if(validarAdmin(token)) {
	        		if(usuario_alvo.equals(token)) {
		        		System.out.println("o adm está tentando se deletar e não pode");
		        	}else {
		        		sucesso = usuarioService.excluirUsuario(usuario_alvo);
		        		servidorGui.removerUsuario(usuario_alvo);
		        	}
	        	}
	            
	        } catch (SQLException e) {
	            System.out.println("Erro ao excluir conta: problema com o banco de dados.");
	        }
	    } else {
	        System.out.println("Erro ao excluir conta: token invalido - " + token);
	    }
	}

	private void excluirUsuario(String token) throws IOException {
	    if (servidorGui.usuarioJaConectado(token)) { // Verifica se o usuário está logado
	        try {
	            this.usuarioService = new UsuarioService(); // Instancia o serviço corretamente
	            boolean sucesso = usuarioService.excluirUsuario(token); // 
	            
	            if (sucesso) {
	                servidorGui.removerUsuario(token); // Remove da lista de logados
	                System.out.println("Conta excluída com sucesso para: " + token);
	            } else {
	                System.out.println("Erro ao excluir conta");
	            }

	        } catch (SQLException e) {
	            System.out.println("Erro ao excluir conta: problema com o banco de dados.");
	        }
	    } else {
	        System.out.println("Erro ao excluir conta: token invalido - " + token);
	    }
	}





	private JsonObject lerDadosUsuario(String token) {
	    JsonObject dadosJson = new JsonObject(); // dados

	    try {
	        this.usuarioService = new UsuarioService();
	        Usuario usuario = this.usuarioService.lerDadosUsuario(token); // Consulta o banco

	        if (usuario != null) { // Verifica se o usuário foi encontrado
	            dadosJson.put("nome", usuario.getNome());
	            dadosJson.put("usuario", usuario.getNomeUsuario());
	            dadosJson.put("senha", usuario.getSenha());
	            dadosJson.put("perfil", usuario.getPerfil());
	        } else {
	            System.out.println("Usuário não encontrado no banco de dados: " + token);
	        }

	    } catch (SQLException | IOException e) {
	        System.out.println("Erro ao buscar dados do usuário: " + e.getMessage());
	    }

	    return !dadosJson.isEmpty() ? dadosJson : null; // Retorna null apenas se não houver dados
	}





	@Override
	public void run() {
	    boolean usuarioAutenticado = false; // Variavel  global no escopo do metodo
	    //usuario = null;
	    
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	         PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

	        while (true) {
	            String mensagemRecebida = reader.readLine();
	            if (mensagemRecebida == null) break;
	          
	            JsonObject jsonObject = Jsoner.deserialize(mensagemRecebida, new JsonObject());
	            
	            System.out.println("Servidor Recebeu: "+mensagemRecebida);
	            
		            if (jsonObject.containsKey("operacao")) {
		            	operacao = (String) jsonObject.get("operacao");
		            	perfil = (String) jsonObject.get("perfil");
		            }
	            	
	            	if (jsonObject.containsKey("usuario")) {
		                usuario = (String) jsonObject.get("usuario");
		                senha = (String) jsonObject.get("senha");
			            nome = (String) jsonObject.get("nome");
	            	}
	            	if (jsonObject.containsKey("token")) {
	            		
	            		token = (String) jsonObject.get("token");
	  		            novo_usuario = (String) jsonObject.get("novo_usuario");
	  		            novo_nome = (String) jsonObject.get("novo_nome");
	  		            nova_senha = (String) jsonObject.get("nova_senha");
	  		            usuario_alvo = (String) jsonObject.get("usuario_alvo");
	  		            novo_perfil = (String) jsonObject.get("novo_perfil");
	  		            
	  		            descricao = (String) jsonObject.get("descricao");
	  		            filtro = (String) jsonObject.get("filtro");
	  		            nova_descricao = (String) jsonObject.get("nova_descricao");
	  		       
	  		       if (jsonObject.containsKey("id_ordem")) {
	  		    	 Object idObj = jsonObject.get("id_ordem");
	  		    	if (idObj instanceof Number) {
	  		    	    id_ordem = ((Number) idObj).intValue();
	  		    	} else {
	  		    	    id_ordem = -1;
	  		    	    System.out.println("ID da ordem está vazio, nulo ou não é um número.");
	  		    	}


	  		    	}

	  		         if (jsonObject.containsKey("novo_status")) {
	  		        	novo_status = (String) jsonObject.get("novo_status");
	  		         }
	  		            
	            	}
		          
	          
	           
	           

	            JsonObject respostaJson = new JsonObject();

	            if (Protocolo.LOGINOPERACAOCONTEUDO.equals(operacao)) {
	               // System.out.println("Login solicitado para o usuário: " + usuario);
	                //System.out.println("Senha: " + senha);

	                if (servidorGui.usuarioJaConectado(usuario)) {
	                    respostaJson.put(Protocolo.LOGINSTATUS, Protocolo.LOGINSTATUSCONTEUDOERRO);
	                    respostaJson.put(Protocolo.LOGINOPERACAO, Protocolo.LOGINOPERACAOCONTEUDO);
	                    respostaJson.put(Protocolo.LOGINMENSAGEM, "Usuário já logado");
	                   // System.out.println("Tentativa de login recusada para " + usuario);
	                    writer.println(Jsoner.serialize(respostaJson));
	                    System.out.println("servidor enviou: "+ Jsoner.serialize(respostaJson));
	                    continue;
	                }

	                if (validarCampos(usuario, senha) && validarSenhaUsuario(usuario, senha)) {
	                	
	                    JsonObject dadosUsuario = lerDadosUsuario(usuario);
	                    perfil = (String) dadosUsuario.get("perfil");
	                    if(perfil.equals(Protocolo.LOGINPERFILCONTEUDOCOMUM) || perfil.equals(Protocolo.LOGINPERFILCONTEUDOADM)) {
	                    	token = (String) usuario; // Atualiza o token global corretamente
		                    servidorGui.adicionarUsuario(token); // Adiciona usuário à lista de logados
	                    	usuarioAutenticado = true; // usuário é marcado como autenticado
		                    System.out.println("Login realizado com sucesso");
		                    respostaJson.clear();
		                    respostaJson.put(Protocolo.LOGINSTATUS, Protocolo.CADASTROSTATUSCONTEUDOSUCESSO);
		                    respostaJson.put(Protocolo.LOGINOPERACAO, Protocolo.LOGINOPERACAOCONTEUDO);
		                    respostaJson.put(Protocolo.LOGINTOKEN, usuario);
		                    //System.out.println(perfil);
		                   
		                    respostaJson.put(Protocolo.LOGINPERFIL, perfil);
		                    //buscarUsuarios();

	                    }else {
	                    	respostaJson.clear();
		                    respostaJson.put(Protocolo.LOGINSTATUS, Protocolo.LOGINSTATUSCONTEUDOERRO);
		                    respostaJson.put(Protocolo.LOGINOPERACAO, Protocolo.LOGINOPERACAOCONTEUDO);
		                    respostaJson.put(Protocolo.LOGINMENSAGEM, "perfil não existe");
	                    }
	                    
	                    
	                } else {
	                	respostaJson.clear();
	                    respostaJson.put(Protocolo.LOGINSTATUS, Protocolo.LOGINSTATUSCONTEUDOERRO);
	                    respostaJson.put(Protocolo.LOGINOPERACAO, Protocolo.LOGINOPERACAOCONTEUDO);
	                    respostaJson.put(Protocolo.LOGINMENSAGEM, "Informacoes incorretas");
	                    //System.out.println("servidor enviou: "+ Jsoner.serialize(respostaJson));

	                    //System.out.println("Falha no login para " + usuario);
	                }
	            }
	            
	            else if (Protocolo.LEROPERACAOCONTEUDO.equals(operacao)) {
                	if(usuarioAutenticado && servidorGui.usuarioJaConectado(token)) {
                		token = (String) jsonObject.get("token");
                		

                		JsonObject dadosUsuario = lerDadosUsuario(token);
                		//lerDadosUsuario(token,writer);
                		respostaJson.put(Protocolo.LERSTATUS, Protocolo.LERSTATUSCONTEUDOSUCESSO);
	                	respostaJson.put(Protocolo.LEROPERACAO, Protocolo.LEROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.LERDADOS, dadosUsuario);
	                	//respostaJson.put("mensagem", "testando ler dados");
	                   // System.out.println("Dados encontrados e enviados: " + Jsoner.serialize(dadosUsuario)); // Log de verificação
	  
                	}else {
                		respostaJson.put(Protocolo.LERSTATUS, Protocolo.LERSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.LEROPERACAO, Protocolo.LEROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.LERMENSAGEM, "token invalido");
                	}
                	
                	
                }
	            else if (Protocolo.CADASTROOPERACAOCONTEUDO.equals(operacao)) {
	                if(validarCamposCadastro(usuario, nome,senha) && validarNomeUsuario(usuario)) {
	                	 cadastrarUsuario(usuario, nome, senha, perfil);
	                	 respostaJson.put(Protocolo.CADASTROSTATUS, Protocolo.CADASTROSTATUSCONTEUDOSUCESSO);
	                	 respostaJson.put(Protocolo.CADASTROOPERACAO, Protocolo.CADASTROOPERACAOCONTEUDO);
	                	 respostaJson.put(Protocolo.CADASTROMENSAGEM, "Cadastro realizado com sucesso");
	                }else if(!servidorGui.usuarioJaConectado(token)) {
	                	respostaJson.put(Protocolo.CADASTROSTATUS, Protocolo.CADASTROSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTROOPERACAO, Protocolo.CADASTROOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTROMENSAGEM, "Token inválido");
	                }
	                else if(!validarCamposCadastro(usuario, nome,senha)) {
	                	respostaJson.put(Protocolo.CADASTROSTATUS, Protocolo.CADASTROSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTROOPERACAO, Protocolo.CADASTROOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTROMENSAGEM, "Os campos recebidos não são validos");
	                }else if(!validarNomeUsuario(usuario)){
	                	respostaJson.put(Protocolo.CADASTROSTATUS, Protocolo.CADASTROSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTROOPERACAO, Protocolo.CADASTROOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTROMENSAGEM, "usuario já cadastrado");
	                }
	                else {
	                	respostaJson.put(Protocolo.CADASTROSTATUS, Protocolo.CADASTROSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTROOPERACAO, Protocolo.CADASTROOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTROMENSAGEM, "Token inválido");
	                }
	      

	            }
	            else if (Protocolo.CADASTRARORDEMOPERACAOCONTEUDO.equals(operacao)) {
	                if(validarDescricao(descricao) && usuarioAutenticado && servidorGui.usuarioJaConectado(token)) {
	                	 cadastrarOrdem(token);
	                	 respostaJson.put(Protocolo.CADASTRARORDEMSTATUS, Protocolo.CADASTRARORDEMSTATUSCONTEUDOSUCESSO);
	                	 respostaJson.put(Protocolo.CADASTRARORDEMOPERACAO, Protocolo.CADASTRARORDEMOPERACAOCONTEUDO);
	                	 respostaJson.put(Protocolo.CADASTROMENSAGEM, "Ordem cadastrada com sucesso");
	                }else if(!servidorGui.usuarioJaConectado(token)) {
	                	respostaJson.put(Protocolo.CADASTRARORDEMSTATUS, Protocolo.CADASTRARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTRARORDEMOPERACAO, Protocolo.CADASTRARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTRARORDEMMENSAGEM, "Token inválido");
	                }
	                else if(!validarDescricao(descricao)) {
	                	respostaJson.put(Protocolo.CADASTRARORDEMSTATUS, Protocolo.CADASTRARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTRARORDEMOPERACAO, Protocolo.CADASTRARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTRARORDEMMENSAGEM, "Descrição inválida");
	                }
	                else {
	                	respostaJson.put(Protocolo.CADASTRARORDEMSTATUS, Protocolo.CADASTRARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.CADASTRARORDEMOPERACAO, Protocolo.CADASTRARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.CADASTRARORDEMMENSAGEM, "Token inválido");
	                }
	      

	            }
	            
	            else if (Protocolo.ALTERARORDEMOPERACAOCONTEUDO.equals(operacao)) {
		            
	            	if(!novo_status.equals("cancelada") && !novo_status.equals("finalizada") && !novo_status.equals("pendente")) {
	            		respostaJson.put(Protocolo.ALTERARORDEMSTATUS, Protocolo.ALTERARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.ALTERARORDEMOPERACAO, Protocolo.ALTERARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.ALTERARORDEMMENSAGEM, "novo status invalido");
	            	}
	            	else if(!ordemExiste(token,id_ordem)) {
	            		respostaJson.put(Protocolo.ALTERARORDEMSTATUS, Protocolo.ALTERARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.ALTERARORDEMOPERACAO, Protocolo.ALTERARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.ALTERARORDEMMENSAGEM, "ordem não existe");
	                
	            	}
	            	
	            	else if(!servidorGui.usuarioJaConectado(token) || !validarAdmin(token)) {
	            		respostaJson.put(Protocolo.ALTERARORDEMSTATUS, Protocolo.ALTERARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.ALTERARORDEMOPERACAO, Protocolo.ALTERARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.ALTERARORDEMMENSAGEM, "Token invalido");
	                
	            	}else if(validarDescricao(nova_descricao) && validarAdmin(token)) {
	                     
	            		alterarStatusOrdem(token, id_ordem, novo_status, nova_descricao);
	                	 respostaJson.put(Protocolo.ALTERARORDEMSTATUS, Protocolo.ALTERARORDEMSTATUSCONTEUDOSUCESSO);
	                	 respostaJson.put(Protocolo.ALTERARORDEMOPERACAO, Protocolo.ALTERARORDEMOPERACAOCONTEUDO);
	                	 respostaJson.put(Protocolo.ALTERARORDEMMENSAGEM, "Ordem editada com sucesso");
	                	
	                }else if(!validarDescricao(nova_descricao)) {
	                	respostaJson.put(Protocolo.ALTERARORDEMSTATUS, Protocolo.ALTERARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.ALTERARORDEMOPERACAO, Protocolo.ALTERARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.ALTERARORDEMMENSAGEM, "Descricao vazia ou fora dos limites");
	                }
	                else {
	                	respostaJson.put(Protocolo.EDITARORDEMSTATUS, Protocolo.EDITARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITARORDEMOPERACAO, Protocolo.EDITARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARORDEMMENSAGEM, "Ordem não existe");
	                }
	            }
	            
	            else if (Protocolo.EDITARORDEMOPERACAOCONTEUDO.equals(operacao)) {
	            
	            	if(!servidorGui.usuarioJaConectado(token) || !validarAutor(token,id_ordem)) {
	            		respostaJson.put(Protocolo.EDITARORDEMSTATUS, Protocolo.EDITARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITARORDEMOPERACAO, Protocolo.EDITARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARORDEMMENSAGEM, "Permissão negada");
	                
	            	}else if(validarDescricao(nova_descricao) && validarOrdemPendente(token,id_ordem)) {
	                     
	            		editarOrdem(token, id_ordem, nova_descricao);
	                	 respostaJson.put(Protocolo.EDITARORDEMSTATUS, Protocolo.EDITARORDEMSTATUSCONTEUDOSUCESSO);
	                	 respostaJson.put(Protocolo.EDITARORDEMOPERACAO, Protocolo.EDITARORDEMOPERACAOCONTEUDO);
	                	 respostaJson.put(Protocolo.EDITARORDEMMENSAGEM, "Ordem editada com sucesso");
	                	
	                }else if(!validarDescricao(nova_descricao)) {
	                	respostaJson.put(Protocolo.EDITARORDEMSTATUS, Protocolo.EDITARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITARORDEMOPERACAO, Protocolo.EDITARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARORDEMMENSAGEM, "Descricao invalida");
	                }else if(!validarOrdemPendente(token,id_ordem)) {
	                	respostaJson.put(Protocolo.EDITARORDEMSTATUS, Protocolo.EDITARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITARORDEMOPERACAO, Protocolo.EDITARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARORDEMMENSAGEM, "Ordem já finalizada");
	                }
	                else {
	                	respostaJson.put(Protocolo.EDITARORDEMSTATUS, Protocolo.EDITARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITARORDEMOPERACAO, Protocolo.EDITARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARORDEMMENSAGEM, "Permissão negada");
	                }
	            }
	            
	            else if (Protocolo.EDITAROPERACAOCONTEUDO.equals(operacao) && validarAdmin(token)) {
		            
	            	if(!servidorGui.usuarioJaConectado(token)) {
	                	respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "Token inválido");
	                }
	            	else if(validarNomeUsuario(usuario_alvo)) {
	            		respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "Usuario não existe");
	            	}
	            	else if(validarCamposCadastro(usuario_alvo, novo_nome,nova_senha) && editarUsuarioAdmin(usuario_alvo, novo_nome, nova_senha, novo_perfil)) {
	                	 respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOSUCESSO);
	                	 respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	 respostaJson.put(Protocolo.EDITARMENSAGEM, "Usuário editado com sucesso");
	                	 //respostaJson.put(Protocolo.EDITARTOKEN, novo_usuario);
	                	 //token = (String) novo_usuario;
	                }else if(!validarCamposCadastro(usuario_alvo, novo_nome,nova_senha)) {
	                	respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "Os campos recebidos não são validos");
	                }else if(!validarNomeUsuario(usuario_alvo)){
	                	respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "o adm não pode remover seus previlegios");
	                }
	                else{
	                	respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "Token inválido");
	                }
	      
	                //writer.println(Jsoner.serialize(respostaJson));
	            }
	            
	            else if (Protocolo.EDITAROPERACAOCONTEUDO.equals(operacao)) {
	            	String novoNomeUsuario = novo_usuario;
	            	if(!servidorGui.usuarioJaConectado(token)) {
	            		respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "Token inválido");
	                
	            	}else if(validarCamposCadastro(novo_usuario, novo_nome,nova_senha) && (novoNomeUsuario.equals(token) || validarNomeUsuario(novo_usuario))) {
	                    editarUsuario(token, novo_usuario, novo_nome, nova_senha);
	                	 respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOSUCESSO);
	                	 respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	 respostaJson.put(Protocolo.EDITARMENSAGEM, "Dados atualizados com sucesso");
	                	 respostaJson.put(Protocolo.EDITARTOKEN, novo_usuario);
	                	 token = (String) novo_usuario;
	                }else if(!validarCamposCadastro(novo_usuario, novo_nome,nova_senha)) {
	                	respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "Os campos recebidos não são validos");
	                }else if(!validarNomeUsuario(novo_usuario)){
	                	respostaJson.put(Protocolo.EDITARSTATUS, Protocolo.EDITARSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EDITAROPERACAO, Protocolo.EDITAROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EDITARMENSAGEM, "usuario já cadastrado");
	                }
	            }
	            
	            else if (Protocolo.LOGOUTOPERACAOCONTEUDO.equals(operacao)) {
	                //System.out.println("Tentando deslogar usuário: " + token);

	                if (servidorGui.usuarioJaConectado(token)) { // verifica se o usuário esta logado
	                    servidorGui.removerUsuario(token); // remove o usuario da lista de logados
	                    usuarioAutenticado = false; // marca como deslogado

	                    //System.out.println("Logout realizado com sucesso para: " + token);
	                    respostaJson.put("status", "sucesso");
	                    respostaJson.put("operacao", "logout");
	                    respostaJson.put("mensagem", "Logout realizado com sucesso");
	                } else {
	                    System.out.println("Erro ao deslogar: Token inválido - " + token);
	                    respostaJson.put("status", "erro");
	                    respostaJson.put("operacao", "logout");
	                    respostaJson.put("mensagem", "Token inválido");
	                }

	                writer.println(Jsoner.serialize(respostaJson)); //Envia a resposta ao cliente
	                System.out.println("Servidor enviou: "+Jsoner.serialize(respostaJson));
	                continue;
		            

	            }



	            
	            else if (Protocolo.EXCLUIROPERACAOCONTEUDO.equals(operacao) && validarAdmin(token)) {
                	if(usuarioAutenticado) {
                		token = (String) jsonObject.get("token");

                		excluirUsuarioAdmin(token);
                		
                		if(sucesso) {
                			respostaJson.put(Protocolo.EXCLUIRSTATUS, Protocolo.EXCLUIRSTATUSCONTEUDOSUCESSO);
    	                	respostaJson.put(Protocolo.EXCLUIROPERACAO, Protocolo.EXCLUIROPERACAOCONTEUDO);
    	                	respostaJson.put(Protocolo.EXCLUIRMENSAGEM, "Conta excluída com sucesso");
    	                	sucesso = false;
                		}else if(!servidorGui.usuarioJaConectado(token)) {
                			respostaJson.put(Protocolo.EXCLUIRSTATUS, Protocolo.EXCLUIRSTATUSCONTEUDOERRO);
    	                	respostaJson.put(Protocolo.EXCLUIROPERACAO, Protocolo.EXCLUIROPERACAOCONTEUDO);
    	                	respostaJson.put(Protocolo.EXCLUIRMENSAGEM, "token invalido");
    	                	sucesso = false;
                		}else if(validarNomeUsuario(usuario_alvo)) {
                			respostaJson.put(Protocolo.EXCLUIRSTATUS, Protocolo.EXCLUIRSTATUSCONTEUDOERRO);
    	                	respostaJson.put(Protocolo.EXCLUIROPERACAO, Protocolo.EXCLUIROPERACAOCONTEUDO);
    	                	respostaJson.put(Protocolo.EXCLUIRMENSAGEM, "Usuario não existe");
    	                	sucesso = false;
                		}
                		else {
                			respostaJson.put(Protocolo.EXCLUIRSTATUS, Protocolo.EXCLUIRSTATUSCONTEUDOERRO);
    	                	respostaJson.put(Protocolo.EXCLUIROPERACAO, Protocolo.EXCLUIROPERACAOCONTEUDO);
    	                	respostaJson.put(Protocolo.EXCLUIRMENSAGEM, "adm não pode se excluir");
    	                	sucesso = false;
                		}
	                	
                	}
                	
                	
                }
	            
	            else if (Protocolo.EXCLUIROPERACAOCONTEUDO.equals(operacao)) {
                	if(usuarioAutenticado && servidorGui.usuarioJaConectado(token)) {
                		token = (String) jsonObject.get("token");
          

                		excluirUsuario(token);
                		respostaJson.put(Protocolo.EXCLUIRSTATUS, Protocolo.EXCLUIRSTATUSCONTEUDOSUCESSO);
	                	respostaJson.put(Protocolo.EXCLUIROPERACAO, Protocolo.EXCLUIROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EXCLUIRMENSAGEM, "Conta excluída com sucesso");
	                	sucesso = false;
	                	
                	}else {
                		respostaJson.put(Protocolo.EXCLUIRSTATUS, Protocolo.EXCLUIRSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.EXCLUIROPERACAO, Protocolo.EXCLUIROPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.EXCLUIRMENSAGEM, "token invalido");
	                	sucesso = false;
                	}
                	
                	
                }
	            else if (Protocolo.LISTARORDEMOPERACAOCONTEUDO.equals(operacao) && validarAdmin(token)) {
                	try {
                		
                	
	            	if(usuarioAutenticado && servidorGui.usuarioJaConectado(token)) {
                		String respostaDaBusca = buscarOrdensAdm(token, filtro);
                		
                      
                        JsonObject resultadoJson = (JsonObject) Jsoner.deserialize(respostaDaBusca);
                        JsonArray ordens = (JsonArray) resultadoJson.get("ordens");
                        
                        if(ordens.isEmpty()) {
                        	 respostaJson.put(Protocolo.LISTARORDEMSTATUS, Protocolo.LISTARORDEMSTATUSCONTEUDOERRO);
                             respostaJson.put(Protocolo.LISTARORDEMOPERACAO, Protocolo.LISTARORDEMOPERACAOCONTEUDO);
                             respostaJson.put(Protocolo.LISTARORDEMMENSAGEM, "Nenhuma ordem disponível");
                        }else {
                        	respostaJson = resultadoJson;
                        }
                        
	                	
                	}else {
                		respostaJson.put(Protocolo.LISTARORDEMSTATUS, Protocolo.LISTARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.LISTARORDEMOPERACAO, Protocolo.LISTARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.LISTARORDEMMENSAGEM, "token invalido");
	                	
                	}
                	}catch(JsonException e) {
                		
                	}
                	
                }
	            else if (Protocolo.LISTARORDEMOPERACAOCONTEUDO.equals(operacao)) {
                	try {
                		
                	
	            	if(usuarioAutenticado && servidorGui.usuarioJaConectado(token)) {
                		String respostaDaBusca = buscarOrdens(token, filtro);
                		
                      
                        JsonObject resultadoJson = (JsonObject) Jsoner.deserialize(respostaDaBusca);
                        JsonArray ordens = (JsonArray) resultadoJson.get("ordens");
                        
                        if(ordens.isEmpty()) {
                        	 respostaJson.put(Protocolo.LISTARORDEMSTATUS, Protocolo.LISTARORDEMSTATUSCONTEUDOERRO);
                             respostaJson.put(Protocolo.LISTARORDEMOPERACAO, Protocolo.LISTARORDEMOPERACAOCONTEUDO);
                             respostaJson.put(Protocolo.LISTARORDEMMENSAGEM, "Nenhuma ordem disponível");
                        }else {
                        	respostaJson = resultadoJson;
                        }
                        
	                	
                	}else {
                		respostaJson.put(Protocolo.LISTARORDEMSTATUS, Protocolo.LISTARORDEMSTATUSCONTEUDOERRO);
	                	respostaJson.put(Protocolo.LISTARORDEMOPERACAO, Protocolo.LISTARORDEMOPERACAOCONTEUDO);
	                	respostaJson.put(Protocolo.LISTARORDEMMENSAGEM, "token invalido");
	                	
                	}
                	}catch(JsonException e) {
                		
                	}
                	
                }
	            
	            else if (Protocolo.LISTARUSUARIOSOPERACAOCONTEUDO.equals(operacao) && validarAdmin(token)) {
	            	 try {
	     	            List<Usuario> usuarios = this.usuarioService.buscarUsuarios();

	     	            // Criando o objeto JSON com status e operação
	     	           
	     	            respostaJson.put("status", "sucesso");
	     	            respostaJson.put("operacao", "listar_usuarios");

	     	            // Convertendo a lista de usuários para JSON
	     	            JsonArray usuariosArray = new JsonArray();
	     	            for (Usuario usuario : usuarios) {
	     	                JsonObject usuarioJson = new JsonObject();
	     	                usuarioJson.put("nome", usuario.getNome());
	     	                usuarioJson.put("usuario", usuario.getNomeUsuario());
	     	                usuarioJson.put("perfil", usuario.getPerfil());
	     	                usuariosArray.add(usuarioJson);
	     	            }

	     	            respostaJson.put("usuarios", usuariosArray);
	     	            System.out.println(Jsoner.serialize(respostaJson));
	     	            // Retorna a String JSON pronta
	     	           

	     	        } catch (SQLException | IOException e) {
	     	            JOptionPane.showMessageDialog(null, "Erro ao carregar os dados dos usuários.", "Buscar Usuários", JOptionPane.ERROR_MESSAGE);
	     	            System.out.println(e);

	     	            
	     	        
	     	        }
	            }
	            else if(Protocolo.LISTARUSUARIOSOPERACAOCONTEUDO.equals(operacao) && !servidorGui.usuarioJaConectado(token)) {
	            	respostaJson.put("status", "erro");
	            	respostaJson.put("operacao", "listar_usuarios");
	            	respostaJson.put("mensagem", "Token inválido");
	            }
	            else {
	            	respostaJson.put("status", "erro");
	            	respostaJson.put("operacao", "listar_usuarios");
	            	respostaJson.put("mensagem", "Token inválido");
	                
	                //System.out.println("Operação inválida recebida: " + operacao);
	            }

	            writer.println(Jsoner.serialize(respostaJson));
	            System.out.println("Servidor enviou: "+Jsoner.serialize(respostaJson));
	        }

	    } catch (IOException e) {
	        System.out.println("Usuário " + (usuarioAutenticado ? token : "desconhecido") + " desconectado.");
	       
	    } finally {
	        if (usuarioAutenticado) { // so remove se o login foi bem sucedido
	            servidorGui.removerUsuario(token);
	            
	        }
	        try {
	            clientSocket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


}