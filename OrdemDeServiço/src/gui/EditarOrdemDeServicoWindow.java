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

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import entities.Usuario;
import service.GerenciadorConexao;
import util.Protocolo;
import javax.swing.JComboBox;


public class EditarOrdemDeServicoWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtNovaDescricao;
	
	//private JPanel painelInfoPessoais;
	private JButton btnEditarOrdem;
	
	private LoginWindow loginWindow;
	private JTextField txtIdOrdem;


	public EditarOrdemDeServicoWindow() {
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
		setBounds(100, 100, 650, 465);
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
		
		JLabel lblEditarOrdemDeServico = new JLabel("Editar Ordem de serviço");
		lblEditarOrdemDeServico.setBounds(133, 11, 377, 32);
		lblEditarOrdemDeServico.setFont(new Font("Tahoma", Font.BOLD, 26));
		contentPane.add(lblEditarOrdemDeServico);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(52, 54, 500, 2);
		contentPane.add(separator);
		
		
		JPanel painelOrdens = new JPanel();
		painelOrdens.setToolTipText("");
		painelOrdens.setBounds(82, 91, 470, 204);
		contentPane.add(painelOrdens);
		painelOrdens.setLayout(null);
		painelOrdens.setBorder(BorderFactory.createTitledBorder("Ordens"));
		
		JLabel lblNovaDescricao = new JLabel("Nova Descricao:");
		lblNovaDescricao.setBounds(10, 32, 108, 14);
		painelOrdens.add(lblNovaDescricao);
		
		txtNovaDescricao = new JTextField();
		txtNovaDescricao.setBounds(126, 25, 314, 30);
		painelOrdens.add(txtNovaDescricao);
		txtNovaDescricao.setColumns(10);
		
		JLabel lblIdOrdem = new JLabel("Id Ordem:");
		lblIdOrdem.setBounds(10, 78, 81, 14);
		painelOrdens.add(lblIdOrdem);
		
		txtIdOrdem = new JTextField();
		txtIdOrdem.setBounds(126, 107, 314, 26);
		painelOrdens.add(txtIdOrdem);
		txtIdOrdem.setColumns(10);
		txtIdOrdem.setVisible(false);
		
		JComboBox cbOrdens = new JComboBox();
		cbOrdens.setBounds(126, 74, 314, 22);
		painelOrdens.add(cbOrdens);
		
		
		btnEditarOrdem = new JButton("Editar ordem");
		
		
		btnEditarOrdem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (true) { //chamar validar campo depois de testar as respostas do servidor para erros
		            
		            GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		            if (conexao.isConectado()) {
		                
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.put("operacao", "editar_ordem");
		                //jsonObject.put("id_ordem", txtIdOrdem.getText()); 
		                jsonObject.put("nova_descricao", txtNovaDescricao.getText());
		                jsonObject.put("token", GerenciadorConexao.getToken());
		                
		                try {
		                    int id = Integer.parseInt(txtIdOrdem.getText().trim());
		                    jsonObject.put("id_ordem", id);
		                } catch (NumberFormatException ex) {
		                    System.out.println("O ID informado não é um número válido.");
		                    jsonObject.put("id_ordem", -1);
		                }

		               

		                conexao.enviarJson(jsonObject); // Envia a solicitação de editar para o servidor

		                String resposta = conexao.receberMensagem();
		                if (resposta != null && !resposta.isEmpty()) {
		                    
		                    try {
		                        JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                        System.out.println("Cliente recebeu: "+resposta);
		                        
		                        String operacao = (String) respostaJson.get("operacao");
		                        String status = (String) respostaJson.get("status");
		                        String mensagem = (String) respostaJson.get("mensagem");
		                        
		                        if(Protocolo.EDITARORDEMOPERACAOCONTEUDO.equalsIgnoreCase(operacao)) {
		                        	if (Protocolo.EDITARORDEMSTATUSCONTEUDOSUCESSO.equalsIgnoreCase(status)) {
		                        		JOptionPane.showMessageDialog(btnEditarOrdem, mensagem, "Aviso", JOptionPane.INFORMATION_MESSAGE);
			                        } else if(Protocolo.EDITARORDEMSTATUSCONTEUDOERRO.equalsIgnoreCase(status)) {
			                            JOptionPane.showMessageDialog(btnEditarOrdem, mensagem, "Aviso", JOptionPane.ERROR_MESSAGE);
			                        }
		                        }
		                        
		                    } catch (Exception ex) {
		                        JOptionPane.showMessageDialog(btnEditarOrdem, "Resposta inválida do servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
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
		
		
		
		btnEditarOrdem.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEditarOrdem.setBounds(207, 306, 199, 39);
		contentPane.add(btnEditarOrdem);
		
		cbOrdens.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Object selected = cbOrdens.getSelectedItem();
		        if (selected != null) {
		            txtIdOrdem.setText(String.valueOf(selected));
		        }
		    }
		});

		JButton btnListarOrdens = new JButton("Mostrar id OS pendentes");
		btnListarOrdens.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.isConectado()) {
		            JsonObject jsonObject = new JsonObject();
		            jsonObject.put(Protocolo.LISTARORDEMOPERACAO, Protocolo.LISTARORDEMOPERACAOCONTEUDO);
		            jsonObject.put(Protocolo.LISTARORDEMTOKEN, GerenciadorConexao.getToken());
		            jsonObject.put("filtro", "pendente");

		            conexao.enviarJson(jsonObject);
		            String usuarioSolicitando = GerenciadorConexao.getToken();
		            String resposta = conexao.receberMensagem();
		            System.out.println("Cliente recebeu: " + resposta);

		            try {
		                JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		                JsonArray ordensArray = (JsonArray) respostaJson.get("ordens");

		                cbOrdens.removeAllItems(); // limpa o JComboBox antes de adicionar os novos itens

		                if (ordensArray != null && !ordensArray.isEmpty()) {
		                    for (Object obj : ordensArray) {
		                        JsonObject ordemJson = (JsonObject) obj;

		                        Object idObj = ordemJson.get("id");
		                        Object autorObj = ordemJson.get("autor");
		                        
		                        int idOrdem = 0;

		                        if (idObj != null && idObj instanceof Number && autorObj.equals(usuarioSolicitando)) {
		                            idOrdem = ((Number) idObj).intValue();
		                        } else {
		                            System.out.println("ID inválido ou ausente: " + idObj);
		                            continue;
		                        }

		                        // Adiciona o ID da ordem no JComboBox
		                        cbOrdens.addItem(idOrdem);
		                    }
		                } else {
		                    JOptionPane.showMessageDialog(btnListarOrdens, "Nenhuma ordem encontrada com esse filtro.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		                }

		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(btnListarOrdens, ex, "Erro", JOptionPane.ERROR_MESSAGE);
		            }

		        } else {
		            JOptionPane.showMessageDialog(btnListarOrdens, "token não encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        }
		    }
		});

		btnListarOrdens.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnListarOrdens.setBounds(207, 356, 199, 31);
		contentPane.add(btnListarOrdens);
	}
}