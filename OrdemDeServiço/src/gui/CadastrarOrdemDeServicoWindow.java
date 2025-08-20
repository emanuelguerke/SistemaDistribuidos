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


public class CadastrarOrdemDeServicoWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtDescricao;
	
	//private JPanel painelInfoPessoais;
	private JButton btnCadastrarOrdem;
	
	private LoginWindow loginWindow;


	public CadastrarOrdemDeServicoWindow() {
		setTitle("Cadastrar Ordem de Serviço");
		
		this.iniciarComponentes();
	//	this.usuarioService = new UsuarioService();
		
	//	this.cadastrarUsuario();
	}
	
	private void voltarOrdem() {
		
		OrdemDeServicoWindow voltar = new OrdemDeServicoWindow();
		voltar.setVisible(true);
		
		this.setVisible(false);
	}
	
	private void limparComponentes() {
		
		//this.txtNomeUsuario.setText("");
		//this.txtNome.setText("");
		//this.fieldSenha.setText("");
		
	}
	
	

	
	public boolean validarCampos() {
		return true;
	}
	

	
	private void iniciarComponentes() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 330);
		setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmVoltar = new JMenuItem("Voltar");
		mntmVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltarOrdem();
			}
		});
		menuBar.add(mntmVoltar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblOrdemDeServico = new JLabel("Cadastrar Ordem de serviço");
		lblOrdemDeServico.setBounds(133, 11, 377, 32);
		lblOrdemDeServico.setFont(new Font("Tahoma", Font.BOLD, 26));
		contentPane.add(lblOrdemDeServico);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(52, 54, 500, 2);
		contentPane.add(separator);
		
		
		JPanel painelOrdens = new JPanel();
		painelOrdens.setToolTipText("");
		painelOrdens.setBounds(82, 91, 470, 84);
		contentPane.add(painelOrdens);
		painelOrdens.setLayout(null);
		painelOrdens.setBorder(BorderFactory.createTitledBorder("Ordens"));
		
		JLabel lbldescricao = new JLabel("Descricao:");
		lbldescricao.setBounds(10, 32, 81, 14);
		painelOrdens.add(lbldescricao);
		
		txtDescricao = new JTextField();
		txtDescricao.setBounds(87, 25, 332, 37);
		painelOrdens.add(txtDescricao);
		txtDescricao.setColumns(10);
		
		
		btnCadastrarOrdem = new JButton("Cadastrar nova ordem");
		
		
		btnCadastrarOrdem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (true) { //chamar validar campo depois de testar as respostas do servidor para erros
		            
		            GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		            if (conexao.isConectado()) {
		                
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.put("operacao", "cadastrar_ordem");
		                jsonObject.put("descricao", txtDescricao.getText());
		                jsonObject.put("token", GerenciadorConexao.getToken());
		               

		                conexao.enviarJson(jsonObject); // Envia a solicitação de cadastro para o servidor

		                String resposta = conexao.receberMensagem();
		                if (resposta != null && !resposta.isEmpty()) {
		                    
		                    try {
		                        JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                        System.out.println("Cliente recebeu: "+resposta);
		                        
		                        String operacao = (String) respostaJson.get("operacao");
		                        String status = (String) respostaJson.get("status");
		                        String mensagem = (String) respostaJson.get("mensagem");
		                        
		                        if(Protocolo.CADASTRARORDEMOPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                        	if (Protocolo.CADASTRARORDEMSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
		                        		JOptionPane.showMessageDialog(btnCadastrarOrdem, mensagem, "Aviso", JOptionPane.INFORMATION_MESSAGE);
			                        } else if(Protocolo.CADASTRARORDEMSTATUSCONTEUDOERRO.equalsIgnoreCase(status)) {
			                            JOptionPane.showMessageDialog(btnCadastrarOrdem, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
			                        }
		                        }
		                        
		                    } catch (Exception ex) {
		                        JOptionPane.showMessageDialog(btnCadastrarOrdem, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
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
		
		
		
		btnCadastrarOrdem.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCadastrarOrdem.setBounds(216, 199, 199, 39);
		contentPane.add(btnCadastrarOrdem);
	}
}