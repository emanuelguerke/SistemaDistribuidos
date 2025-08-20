package gui;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import entities.Usuario;
import service.GerenciadorConexao;
import util.Protocolo;


public class CadastroWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtNomeUsuario;
	private JPasswordField fieldSenha;
	
	private JPanel painelInfoPessoais;
	private JButton btnCadastrarUsuario;
	
	private LoginWindow loginWindow;


	public CadastroWindow() {
		setTitle("Cadastro");
		
		this.iniciarComponentes();
	//	this.usuarioService = new UsuarioService();
		
	//	this.cadastrarUsuario();
	}
	
	private void voltarLogin() {
		
		LoginWindow voltar = new LoginWindow();
		voltar.setVisible(true);
		
		this.setVisible(false);
	}
	
	private void limparComponentes() {
		
		this.txtNomeUsuario.setText("");
		this.txtNome.setText("");
		this.fieldSenha.setText("");
		
	}
	
	

	
	public boolean validarCampos() {
	    String nomeUsuario = txtNomeUsuario.getText();
	    String nome =  txtNome.getText();
	    String senha = fieldSenha.getText();

	    // Regex para validar caracteres (apenas letras e números, sem espaços ou caracteres especiais)
	    String regex = "^[a-zA-Z0-9]+$";

	    // Verificando nome de usuário
	    if (nomeUsuario == null || nomeUsuario.isEmpty() || nomeUsuario.length() < 3 || nomeUsuario.length() > 30 || !nomeUsuario.matches(regex)) {
	        System.err.println("Erro: Nome de usuário inválido! Deve ter entre 3 e 30 caracteres, sem espaços e sem caracteres especiais.");
	        return false;
	    }
	    //verifica nome
	    if (nome == null || nome.isEmpty() || nome.length() < 3 || nome.length() > 30 || !nome.matches(regex)) {
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
	

	
	private void iniciarComponentes() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 445);
		setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmVoltar = new JMenuItem("Voltar");
		mntmVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltarLogin();
			}
		});
		menuBar.add(mntmVoltar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCadastroUsuario = new JLabel("Cadastro de usuário");
		lblCadastroUsuario.setBounds(97, 11, 280, 32);
		lblCadastroUsuario.setFont(new Font("Tahoma", Font.BOLD, 26));
		contentPane.add(lblCadastroUsuario);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 54, 445, 2);
		contentPane.add(separator);
		
		
		JPanel painelInfoUsuario = new JPanel();
		painelInfoUsuario.setBounds(68, 98, 324, 168);
		contentPane.add(painelInfoUsuario);
		painelInfoUsuario.setLayout(null);
		painelInfoUsuario.setBorder(BorderFactory.createTitledBorder("Informações de usuário"));
		
		JLabel lblNomeUsuario = new JLabel("Usuário:");
		lblNomeUsuario.setBounds(10, 32, 58, 14);
		painelInfoUsuario.add(lblNomeUsuario);
		
		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setBounds(78, 29, 203, 20);
		painelInfoUsuario.add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);
	
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(10, 108, 46, 14);
		painelInfoUsuario.add(lblSenha);
		
		fieldSenha = new JPasswordField();
		fieldSenha.setBounds(78, 105, 203, 20);
		painelInfoUsuario.add(fieldSenha);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 69, 46, 14);
		painelInfoUsuario.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.setBounds(78, 66, 203, 20);
		painelInfoUsuario.add(txtNome);
		txtNome.setColumns(10);
		
		
		btnCadastrarUsuario = new JButton("Cadastrar novo usuário");
		
		
		btnCadastrarUsuario.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (true) { //chamar validar campo depois de testar as respostas do servidor para erros
		            
		            GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		            if (conexao.isConectado()) {
		                
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.put("operacao", "cadastro");
		                jsonObject.put("usuario", txtNomeUsuario.getText());
		                jsonObject.put("nome", txtNome.getText());
		                jsonObject.put("senha", fieldSenha.getText());
		                jsonObject.put("perfil", "comum");

		                conexao.enviarJson(jsonObject); // Envia a solicitação de cadastro para o servidor

		                String resposta = conexao.receberMensagem();
		                if (resposta != null && !resposta.isEmpty()) {
		                    
		                    try {
		                        JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                        System.out.println("Cliente recebeu: "+resposta);
		                        
		                        String operacao = (String) respostaJson.get("operacao");
		                        String status = (String) respostaJson.get("status");
		                        String mensagem = (String) respostaJson.get("mensagem");
		                        
		                        if(Protocolo.CADASTROOPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                        	if (Protocolo.CADASTROSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
			                        	voltarLogin();//voltar para o lugin
			                            //abrirAtualizarCadastro(txtNomeUsuario.getText()); // Vai para próxima tela
			                        } else if(Protocolo.CADASTROSTATUSCONTEUDOERRO.equalsIgnoreCase(status)) {
			                            JOptionPane.showMessageDialog(btnCadastrarUsuario, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
			                        }
		                        }
		                        
		                    } catch (Exception ex) {
		                        JOptionPane.showMessageDialog(btnCadastrarUsuario, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
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
		
		
		
		btnCadastrarUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCadastrarUsuario.setBounds(123, 301, 199, 39);
		contentPane.add(btnCadastrarUsuario);
	}
}