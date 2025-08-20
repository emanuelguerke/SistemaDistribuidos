package gui;

import java.awt.EventQueue;
import util.Protocolo;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import entities.Usuario;
import service.GerenciadorConexao;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPasswordField;


public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNomeUsuario;
	private JPasswordField fieldSenha;
	//private UsuarioService usuarioService;
	private CadastroWindow usuarioWindow;


	public LoginWindow() {
		setTitle("Login");
		
		this.iniciarComponentes();
		//this.usuarioService = new UsuarioService();
		
	}
	
	private void abrirCadastroUsuario() {
		
		CadastroWindow janelaUsuario = new CadastroWindow();
		janelaUsuario.setVisible(true);
		
		this.setVisible(false);
	}
	
	
	
	public boolean validarCampos() {
	    String nomeUsuario = txtNomeUsuario.getText();
	    String senha = fieldSenha.getText();

	    // Regex para validar caracteres (apenas letras e números, sem espaços ou caracteres especiais)
	    String regex = "^[a-zA-Z0-9]+$";

	    // Verificando nome de usuário
	    if (nomeUsuario == null || nomeUsuario.isEmpty() || nomeUsuario.length() < 3 || nomeUsuario.length() > 30 || !nomeUsuario.matches(regex)) {
	        System.err.println("Erro: Nome de usuário inválido! Deve ter entre 3 e 30 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }

	    // Verificando senha
	    if (senha == null || senha.isEmpty() || senha.length() < 4 || senha.length() > 10 || !senha.matches(regex)) {
	        System.err.println("Erro: Senha inválida! Deve ter entre 4 e 10 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }

	    return true;
	}


	private void abrirPerfilUsuario() {
		
		PerfilUsuarioWindow perfilUsuarioWindow = new PerfilUsuarioWindow();
		perfilUsuarioWindow.setVisible(true);
		
		this.setVisible(false);
		
	}
	private void abrirPerfilUsuarioAdministrador() {
		
		PerfilUsuarioAdministradorWindow perfilUsuarioAdminWindow = new PerfilUsuarioAdministradorWindow();
		perfilUsuarioAdminWindow.setVisible(true);
		
		this.setVisible(false);
		
	}
	
	private void iniciarComponentes() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 408);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel painelBV = new JPanel();
		painelBV.setBackground(new Color(255, 255, 255));
		painelBV.setBounds(78, 27, 266, 202);
		contentPane.add(painelBV);
		painelBV.setLayout(null);
		painelBV.setBorder(BorderFactory.createTitledBorder(""));
		
		JLabel lblBemVindo = new JLabel("Bem-Vindo");
		lblBemVindo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBemVindo.setBounds(93, 11, 86, 14);
		painelBV.add(lblBemVindo);
		
		JLabel lblNomeUsuario = new JLabel("Nome de usuario:");
		lblNomeUsuario.setBounds(55, 40, 161, 14);
		painelBV.add(lblNomeUsuario);
		
		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setBounds(55, 54, 161, 20);
		painelBV.add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(55, 85, 46, 14);
		painelBV.add(lblSenha);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (true) { //chamar validação dos campos no cliente depois de testar as respostas do servidor para erros
		            
		            GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		            if (conexao.isConectado()) {
		                
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.put(Protocolo.LOGINOPERACAO, Protocolo.LOGINOPERACAOCONTEUDO);
		                jsonObject.put(Protocolo.LOGINUSUARIO, txtNomeUsuario.getText());
		                jsonObject.put(Protocolo.LOGINSENHA, fieldSenha.getText());

		                conexao.enviarJson(jsonObject); // Envia a solicitação de login ao servidor

		                String resposta = conexao.receberMensagem();
		                if (resposta != null && !resposta.isEmpty()) {
		                    
		                    try {
		                        JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                        System.out.println("Cliente recebeu: "+resposta);
		                        
		                        String perfil = (String) respostaJson.get(Protocolo.LOGINPERFIL);
		                        
		                        String status = (String) respostaJson.get(Protocolo.LOGINSTATUS);
		                        String token = (String) respostaJson.get(Protocolo.LOGINTOKEN);
		                        String operacao = (String) respostaJson.get(Protocolo.LOGINOPERACAO);
		                        String mensagem = (String) respostaJson.get(Protocolo.LOGINMENSAGEM);
		                        
		                        if(Protocolo.LOGINOPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                        	if (Protocolo.LOGINSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
			                            // Atualiza o token global corretamente
			                            GerenciadorConexao.setToken(token);
			                            System.out.println("atualizando token no cliente apos login: " + GerenciadorConexao.getToken());

			                            // Vai para a próxima tela de perfil **apenas se o login for bem-sucedido**
			                            if(Protocolo.LOGINPERFILCONTEUDOCOMUM.equals(perfil)) {
			                            	 abrirPerfilUsuario();
					                         dispose();
			                            }else if(Protocolo.LOGINPERFILCONTEUDOADM.equals(perfil)){
			                            	abrirPerfilUsuarioAdministrador();
					                         dispose();
			                            }else {
			                            	JOptionPane.showMessageDialog(btnEntrar, "Perfil não existente recebido", "Erro", JOptionPane.ERROR_MESSAGE);
			                            }
			                           
			                        } else if(Protocolo.LOGINSTATUSCONTEUDOERRO.equalsIgnoreCase(status)) {
			                            JOptionPane.showMessageDialog(btnEntrar, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
			                            //return; // Evita que `abrirPerfilUsuario()` seja chamado indevidamente
			                        }else {
			                        	System.out.println("ta caindo aqui????");
			                        }
		                        }
		                         

		                    } catch (Exception ex) {
		                        JOptionPane.showMessageDialog(btnEntrar, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		                    }

		                } else {
		                    System.err.println("Erro: Sem resposta do servidor!");
		                }
		            } else {
		                System.err.println("Erro: Conexão perdida!");
		            }
		        }
		    }
		});

		btnEntrar.setBounds(90, 146, 89, 30);
		painelBV.add(btnEntrar);
		
		fieldSenha = new JPasswordField();
		fieldSenha.setBounds(55, 100, 161, 23);
		painelBV.add(fieldSenha);
		
		JPanel painelCD = new JPanel();
		painelCD.setBackground(new Color(255, 255, 255));
		painelCD.setBounds(78, 263, 266, 78);
		contentPane.add(painelCD);
		painelCD.setLayout(null);
		painelCD.setBorder(BorderFactory.createTitledBorder(""));
		
		JLabel lblCadastro = new JLabel("Ainda não é cadastrado?");
		lblCadastro.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCadastro.setBounds(66, 11, 145, 14);
		painelCD.add(lblCadastro);
		
		JButton btnCliqueAqui = new JButton("Clique Aqui");
		btnCliqueAqui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				abrirCadastroUsuario();
			}
		});
		btnCliqueAqui.setBounds(76, 36, 110, 31);
		painelCD.add(btnCliqueAqui);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}