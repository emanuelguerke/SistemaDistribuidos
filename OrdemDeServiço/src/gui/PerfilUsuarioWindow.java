package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import service.GerenciadorConexao;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import util.Protocolo;
public class PerfilUsuarioWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtUsuario;
	private JTextField txtSenha;
	private String nome;
	private String usuario;
	private String senha;

	private void abrirOrdens() {
		
		OrdemDeServicoWindow ordemDeServicoWindow = new OrdemDeServicoWindow();
		ordemDeServicoWindow.setVisible(true);
		
		this.setVisible(false);
		
	}
	
	public PerfilUsuarioWindow() {
        setTitle("Cliente - perfil usuario");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 418, 444);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(168, 16, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(168, 72, 46, 14);
		contentPane.add(lblUsuario);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(168, 126, 46, 14);
		contentPane.add(lblSenha);
		
		txtNome = new JTextField();
		txtNome.setBounds(98, 41, 192, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(98, 95, 192, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtSenha = new JTextField();
		txtSenha.setBounds(97, 150, 193, 20);
		contentPane.add(txtSenha);
		txtSenha.setColumns(10);
		
		JButton btnLerDados = new JButton("Mostrar dados");
		btnLerDados.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put(Protocolo.LEROPERACAO, Protocolo.LEROPERACAOCONTEUDO);
		            jsonObject.put(Protocolo.LERTOKEN, GerenciadorConexao.getToken());

		            conexao.enviarJson(jsonObject);

		            String resposta = conexao.receberMensagem();
                    System.out.println("Cliente recebeu: "+resposta);

		            if (resposta == null || resposta.isEmpty()) {
		                JOptionPane.showMessageDialog(btnLerDados, "Erro: Sem resposta do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            try {
		                JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                String status = (String) respostaJson.get("status");
		                String mensagem = (String) respostaJson.get("mensagem");
		                String operacao = (String) respostaJson.get("operacao");
		                
		                if(Protocolo.LEROPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                	if (Protocolo.LERSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
			                    // extrai os dados do objeto dados
			                    JsonObject dados = (JsonObject) respostaJson.get("dados");

			                    if (dados != null) {
			                        nome = (String) dados.get("nome");
			                        usuario = (String) dados.get("usuario");
			                        senha = (String) dados.get("senha");

			                        // atualiza os campos de texto
			                        txtNome.setText(nome);
			                        txtUsuario.setText(usuario);
			                        txtSenha.setText(senha);
			                    }

			                    JOptionPane.showMessageDialog(btnLerDados, "lido com sucesso", "Aviso", JOptionPane.INFORMATION_MESSAGE);
			                } else {
			                    JOptionPane.showMessageDialog(btnLerDados, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
			                }
		                }
		                

		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(btnLerDados, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            System.err.println("Erro: Conexão perdida!");
		        }
		    }
		});


		btnLerDados.setBounds(126, 199, 129, 23);
		contentPane.add(btnLerDados);
		
		JButton btnExcluirUsuario = new JButton("Excluir Usuario");
		btnExcluirUsuario.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {

		            // mensagem para excluir usuário
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put("operacao", "excluir_usuario");
		            jsonObject.put("token", GerenciadorConexao.getToken()); // token do usuario atual

		            // envia  solicitacao ao servidor
		            conexao.enviarJson(jsonObject);
		            String resposta = conexao.receberMensagem();

		            if (resposta != null && !resposta.isEmpty()) {
		                try {
		                    JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                    System.out.println("Cliente recebeu: " + resposta);

		                    String status = (String) respostaJson.get("status");
		                    String mensagem = (String) respostaJson.get("mensagem");
		                    String operacao = (String) respostaJson.get("operacao");
		                    
		                    if(Protocolo.EXCLUIROPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                    	if (Protocolo.EXCLUIRSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
			                        JOptionPane.showMessageDialog(btnExcluirUsuario, mensagem, "Aviso", JOptionPane.INFORMATION_MESSAGE);

			                        // apos excluir fecha a sessao e volta ao login
			                        GerenciadorConexao.setToken(null);
			                        LoginWindow login = new LoginWindow();
			                        login.setVisible(true);

			                        // fecha a janela atual
			                        JFrame janelaAtual = (JFrame) SwingUtilities.getWindowAncestor(btnExcluirUsuario);
			                        if (janelaAtual != null) {
			                            janelaAtual.dispose();
			                        }

			                    } else {
			                        JOptionPane.showMessageDialog(btnExcluirUsuario, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
			                    }
		                    }
		                    

		                } catch (Exception ex) {
		                    JOptionPane.showMessageDialog(btnExcluirUsuario, "Resposta invalida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		                }
		            } else {
		                System.err.println("Erro: Sem resposta do servidor!");
		            }
		        } else {
		            System.err.println("Erro: Conexão perdida!");
		        }
		    }
		});

		
		btnExcluirUsuario.setBounds(126, 236, 129, 23);
		contentPane.add(btnExcluirUsuario);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {
		            // json de logout
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put("operacao", "logout");
		            jsonObject.put("token", GerenciadorConexao.getToken()); //token do usuario atual

		            // enviando a solicitacao ao servidor
		            conexao.enviarJson(jsonObject);
		            
		            // recebendo resposta do servidor
		            String resposta = conexao.receberMensagem();
		            if (resposta == null || resposta.isEmpty()) {
		                JOptionPane.showMessageDialog(btnLogout, "Erro: Sem resposta do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            try {
		                JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                System.out.println("Cliente recebeu: "+resposta);
		                String status = (String) respostaJson.get("status");
		                String operacao = (String) respostaJson.get("operacao");
		                String mensagem = (String) respostaJson.get("mensagem");

		                if(Protocolo.LOGOUTOPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                	if (Protocolo.LOGOUTSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
			                    //JOptionPane.showMessageDialog(btnLogout, "Logout realizado com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);

			                    GerenciadorConexao.setToken(null); // Apaga o token do cliente
			                    //System.out.println("Token resetado no cliente depois logout.");

			                    // Abre a tela de login novamente???bugado voltar aqui depois pra arrumar
			                    LoginWindow login = new LoginWindow();
			                    login.setVisible(true);
			                    //ConectarClienteWindow conectarCliente = new ConectarClienteWindow();
			                    //conectarCliente.setVisible(true);
			                    
			                    dispose();
			                }
			                else if(Protocolo.LOGOUTSTATUSCONTEUDOERRO.equalsIgnoreCase(status)){
			                    JOptionPane.showMessageDialog(btnLogout, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
			                    GerenciadorConexao.setToken(null); 
				                   
			                    LoginWindow login = new LoginWindow();
			                    login.setVisible(true);
			                   
			                    
			                    dispose();
			                    
			                }
		                }
		                

		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(btnLogout, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            System.err.println("Erro: Conexão perdida!");
		        }
		    }
		});

		btnLogout.setBounds(126, 270, 129, 23);
		contentPane.add(btnLogout);
		
		JButton btnEditarUsuario = new JButton("Editar Usuario");
		
		btnEditarUsuario.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {
		            //Criando o JSON com os dados do usuário
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put("operacao", "editar_usuario");
		            jsonObject.put("token", GerenciadorConexao.getToken()); // Nome do usuário atual
		            jsonObject.put("novo_usuario", txtUsuario.getText());
		            jsonObject.put("novo_nome", txtNome.getText());
		            jsonObject.put("nova_senha", txtSenha.getText());

		            // enviando a solicitacao ao servidor
		            conexao.enviarJson(jsonObject);

		           

		            

		            try {
		            	 // recebendo resposta do servidor
			            String resposta = conexao.receberMensagem();
			            
			            if (resposta == null || resposta.isEmpty()) {
			                JOptionPane.showMessageDialog(btnEditarUsuario, "Erro: Sem resposta do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
			                return;
			            }
			            
	                    System.out.println("Cliente recebeu: "+resposta);
		                JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                
		                String status = (String) respostaJson.get("status");
		                String operacao = (String) respostaJson.get("operacao");
		                String mensagem = (String) respostaJson.get("mensagem");
		                
		                if (Protocolo.EDITAROPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                	 if (Protocolo.EDITARSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
				                	GerenciadorConexao.setToken(txtUsuario.getText());
				                    JOptionPane.showMessageDialog(btnEditarUsuario, "Usuário atualizado com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
				                } else if(Protocolo.EDITARSTATUSCONTEUDOERRO.equalsIgnoreCase(status)) {
				                    JOptionPane.showMessageDialog(btnEditarUsuario, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
				                }
		                }
		                
		               

		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(btnEditarUsuario, "Resposta invalida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            System.err.println("Erro: Conexão perdida!");
		        }
		    }
		});

		
		
		btnEditarUsuario.setBounds(126, 310, 129, 23);
		contentPane.add(btnEditarUsuario);
		
		JButton btnOrdens = new JButton("Ordens");
		btnOrdens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirOrdens();
			}
		});
		btnOrdens.setBounds(125, 371, 130, 23);
		contentPane.add(btnOrdens);
	}
}
