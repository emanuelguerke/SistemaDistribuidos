package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import service.GerenciadorConexao;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import util.Protocolo;
import javax.swing.JCheckBox;
public class PerfilUsuarioAdministradorWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtUsuario;
	private JTextField txtSenha;
	private String nome;
	private String usuario;
	private String senha;
	private String mensagem;
	private JTextField txtPerfil;
	private JCheckBox chBoxAdm;
	private JList lstUsuarios;
	
	private JScrollPane scrollPane;
	
	private void abrirOrdens() {
		
		OrdemDeServicoWindow ordemDeServicoWindow = new OrdemDeServicoWindow();
		ordemDeServicoWindow.setVisible(true);
		
		this.setVisible(false);
		
	}
	
	public PerfilUsuarioAdministradorWindow() {
        setTitle("Cliente - perfil usuario administrador");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 425, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(168, 57, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(168, 11, 46, 14);
		contentPane.add(lblUsuario);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(168, 102, 46, 14);
		contentPane.add(lblSenha);
		
		txtNome = new JTextField();
		txtNome.setBounds(98, 71, 192, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(98, 26, 192, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtSenha = new JTextField();
		txtSenha.setBounds(97, 127, 193, 20);
		contentPane.add(txtSenha);
		txtSenha.setColumns(10);
		
		JButton btnExcluirUsuario = new JButton("Excluir usuario");
		btnExcluirUsuario.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {

		            // mensagem para excluir usuário
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put("operacao", "excluir_usuario");
		            jsonObject.put("token", GerenciadorConexao.getToken()); // token do usuario atual
		            jsonObject.put("usuario_alvo", txtUsuario.getText());
		            

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

		
		btnExcluirUsuario.setBounds(8, 218, 192, 23);
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
		
		btnLogout.setBounds(98, 527, 192, 23);
		contentPane.add(btnLogout);
		
		JButton btnEditarUsuario = new JButton("Editar usuario");
		btnEditarUsuario.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {
		            //Criando o JSON com os dados do usuário
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put("operacao", "editar_usuario");
		            jsonObject.put("token", GerenciadorConexao.getToken()); // Nome do usuário atual
		            jsonObject.put("usuario_alvo", txtUsuario.getText());
		            jsonObject.put("novo_nome", txtNome.getText());
		            jsonObject.put("nova_senha", txtSenha.getText());
		            if (chBoxAdm.isSelected()) {
		            	jsonObject.put("novo_perfil", "adm");
		            }else {
		            	jsonObject.put("novo_perfil", "comum");
		            }
		            
		            
		            
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
				                	//GerenciadorConexao.setToken(txtUsuario.getText());
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
		
		
		
		btnEditarUsuario.setBounds(210, 184, 192, 23);
		contentPane.add(btnEditarUsuario);
		
		JButton btnListarUsuarios = new JButton("Listar Usuarios");
		btnListarUsuarios.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put(Protocolo.LISTARUSUARIOSOPERACAO, Protocolo.LISTARUSUARIOSOPERACAOCONTEUDO);
		            jsonObject.put(Protocolo.LISTARUSUARIOSTOKEN, GerenciadorConexao.getToken());

		            conexao.enviarJson(jsonObject);

		            String resposta = conexao.receberMensagem();
		            System.out.println("Cliente recebeu: " + resposta);

		            if (resposta == null || resposta.isEmpty()) {
		                JOptionPane.showMessageDialog(btnListarUsuarios, "Erro: Sem resposta do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            try {
		                JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                String status = (String) respostaJson.get("status");
		                String operacao = (String) respostaJson.get("operacao");
		                String mensagem = (String) respostaJson.get("mensagem");

		                if (Protocolo.LISTARUSUARIOSOPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                    if (Protocolo.LISTARUSUARIOSSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {

		                        // Criar um modelo de lista com espaçamento fixo
		                        JsonArray usuariosArray = (JsonArray) respostaJson.get("usuarios");
		                        DefaultListModel<String> listModel = new DefaultListModel<>();

		                        for (Object obj : usuariosArray) {
		                            JsonObject usuarioJson = (JsonObject) obj;
		                            String nome = (String) usuarioJson.get("nome");
		                            String usuario = (String) usuarioJson.get("usuario");
		                            String perfil = (String) usuarioJson.get("perfil");

		                            // Criar uma String formatada para exibir nome, usuário e perfil com espaçamento fixo
		                            String usuarioFormatado = String.format("%-20s %-20s %-10s", nome, usuario, perfil);
		                            listModel.addElement(usuarioFormatado);
		                        }

		                        // Definir um renderer personalizado para fonte monoespaçada
		                        lstUsuarios.setModel(listModel);
		                        lstUsuarios.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fonte monoespaçada para alinhamento

		                        JOptionPane.showMessageDialog(btnListarUsuarios, "Listado com sucesso", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		                    } else {
		                        JOptionPane.showMessageDialog(btnListarUsuarios, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
		                    }
		                }
		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(btnListarUsuarios, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            System.err.println("Erro: Conexão perdida!");
		        }
		    }
		});

		
		btnListarUsuarios.setBounds(210, 218, 192, 23);
		contentPane.add(btnListarUsuarios);
		
		JButton btnCadastrarUsuario = new JButton("Cadastrar novo usuario");
		btnCadastrarUsuario.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (true) { //chamar validar campo depois de testar as respostas do servidor para erros
		            
		            GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		            if (conexao.isConectado()) {
		                
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.put("operacao", "cadastro");
		                jsonObject.put("usuario", txtUsuario.getText());
		                jsonObject.put("nome", txtNome.getText());
		                jsonObject.put("senha", txtSenha.getText());
		                jsonObject.put("token", GerenciadorConexao.getToken());
		                
		                if (chBoxAdm.isSelected()) {
			            	jsonObject.put("perfil", "adm");
			            }else {
			            	jsonObject.put("perfil", "comum");
			            }

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
			                        	//voltarLogin();//voltar para o lugin
			                            //abrirAtualizarCadastro(txtNomeUsuario.getText()); // Vai para próxima tela
		                        		JOptionPane.showMessageDialog(btnCadastrarUsuario, "Cadastrado com sucesso", "Aviso", JOptionPane.INFORMATION_MESSAGE);
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
		btnCadastrarUsuario.setBounds(10, 184, 192, 23);
		contentPane.add(btnCadastrarUsuario);
		
		chBoxAdm = new JCheckBox("administrador");
		chBoxAdm.setBounds(98, 154, 192, 23);
		contentPane.add(chBoxAdm);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 261, 391, 258);
		contentPane.add(scrollPane);
		
		lstUsuarios = new JList();
		scrollPane.setViewportView(lstUsuarios);
		lstUsuarios.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JLabel lblNomeT = new JLabel("Nome");
		lblNomeT.setBounds(10, 247, 46, 14);
		contentPane.add(lblNomeT);
		
		JLabel lblNewLabel = new JLabel("Usuario");
		lblNewLabel.setBounds(155, 247, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Perfil");
		lblNewLabel_1.setBounds(308, 247, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		JButton btnOrdens = new JButton("Ordens");
		btnOrdens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				abrirOrdens();
			}
		});
		btnOrdens.setBounds(98, 561, 192, 23);
		contentPane.add(btnOrdens);
		lstUsuarios.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		            String selecionado = (String) lstUsuarios.getSelectedValue();

		            if (selecionado != null && !selecionado.isEmpty()) {
		                
		                String[] dados = selecionado.trim().split("\\s{2,}"); 
		                
		                if (dados.length == 3) { 
		                    txtNome.setText(dados[0].trim());      
		                    txtUsuario.setText(dados[1].trim());   
		                    if(dados[2].trim().equals("adm")) {
		                    	chBoxAdm.setSelected(true);
		                    }else {
		                    	chBoxAdm.setSelected(false);
		                    }
		                    //txtPerfil.setText(dados[2].trim());   
		                }
		            }
		        }
		    }
		});

	}
}
