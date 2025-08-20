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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import entities.Usuario;
import service.GerenciadorConexao;
import util.Protocolo;


public class OrdemDeServicoWindow extends JFrame {

	private JPanel contentPane;
	
	private JPanel painelInfoPessoais;
	private JButton btnListarOrdens;
	
	private LoginWindow loginWindow;


	public OrdemDeServicoWindow() {
		setTitle("Ordem de Serviço");
		
		this.iniciarComponentes();
	//	this.usuarioService = new UsuarioService();
		
	//	this.cadastrarUsuario();
	}
	
	private void voltarPerfil() {
		
		PerfilUsuarioWindow perfil = new PerfilUsuarioWindow();
		perfil.setVisible(true);
		
		this.setVisible(false);
	}
	
	private void abrirAlterarOrdens() {
		
		AlterarOrdemDeServicoWindow alterar = new AlterarOrdemDeServicoWindow();
		alterar.setVisible(true);
		
		this.setVisible(false);
	}
	
	private void abrirCadastrarOrdem() {
		
		CadastrarOrdemDeServicoWindow cadastrar = new CadastrarOrdemDeServicoWindow();
		cadastrar.setVisible(true);
		
		this.setVisible(false);
	}
	private void abrirEditarOrdem() {
		
		EditarOrdemDeServicoWindow editar = new EditarOrdemDeServicoWindow();
		editar.setVisible(true);
		
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
		setBounds(100, 100, 625, 760);
		setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmVoltar = new JMenuItem("Voltar");
		mntmVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltarPerfil();
			}
		});
		menuBar.add(mntmVoltar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblOrdemDeServico = new JLabel("Ordem de serviço");
		lblOrdemDeServico.setBounds(180, 11, 261, 32);
		lblOrdemDeServico.setFont(new Font("Tahoma", Font.BOLD, 26));
		contentPane.add(lblOrdemDeServico);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(52, 54, 500, 2);
		contentPane.add(separator);
		
		
		JPanel painelOrdens = new JPanel();
		painelOrdens.setToolTipText("");
		painelOrdens.setBounds(52, 91, 500, 432);
		contentPane.add(painelOrdens);
		painelOrdens.setLayout(null);
		painelOrdens.setBorder(BorderFactory.createTitledBorder("Ordens"));
		// Aqui entra a tabela
		String[] colunas = { "ID", "Autor", "Descrição", "Status" };
		DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
		JTable tabelaOrdens = new JTable(modeloTabela);
		JScrollPane scrollPane = new JScrollPane(tabelaOrdens);
		scrollPane.setBounds(10, 20, 480, 400);
		painelOrdens.add(scrollPane);
		
		btnListarOrdens = new JButton("Listar Ordens");
		
		
		JRadioButton rdbtnPendente = new JRadioButton("Pendente");
		rdbtnPendente.setBounds(52, 530, 100, 20);
		rdbtnPendente.setSelected(true); // selecionado por padrão
		contentPane.add(rdbtnPendente);

		JRadioButton rdbtnConcluida = new JRadioButton("Finalizada");
		rdbtnConcluida.setBounds(160, 530, 100, 20);
		contentPane.add(rdbtnConcluida);

		JRadioButton rdbtnCancelada = new JRadioButton("Cancelada");
		rdbtnCancelada.setBounds(270, 530, 100, 20);
		contentPane.add(rdbtnCancelada);

		JRadioButton rdbtnTodas = new JRadioButton("Todas");
		rdbtnTodas.setBounds(380, 530, 100, 20);
		contentPane.add(rdbtnTodas);

		ButtonGroup grupoStatus = new ButtonGroup();
		grupoStatus.add(rdbtnPendente);
		grupoStatus.add(rdbtnConcluida);
		grupoStatus.add(rdbtnCancelada);
		grupoStatus.add(rdbtnTodas);

		
		btnListarOrdens.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		    	 if (conexao.isConectado()) {
			            JsonObject jsonObject = new JsonObject();
			            jsonObject.put(Protocolo.LISTARORDEMOPERACAO, Protocolo.LISTARORDEMOPERACAOCONTEUDO);
			            jsonObject.put(Protocolo.LISTARORDEMTOKEN, GerenciadorConexao.getToken());
			            
			            String filtroSelecionado = "pendente"; // valorzin padrão

			            if (rdbtnConcluida.isSelected()) {
			                filtroSelecionado = "finalizada";
			            } else if (rdbtnCancelada.isSelected()) {
			                filtroSelecionado = "cancelada";
			            } else if (rdbtnTodas.isSelected()) {
			                filtroSelecionado = "todas";
			            }

			            jsonObject.put("filtro", filtroSelecionado);


			            conexao.enviarJson(jsonObject);
		    	
		    	
		    	String resposta = conexao.receberMensagem();
	            System.out.println("Cliente recebeu: " + resposta);
		    	
	            try {
	            	
	            JsonObject respostaJson = Jsoner.deserialize(resposta, new JsonObject());
		        JsonArray ordensArray = (JsonArray) respostaJson.get("ordens");
		        
		        modeloTabela.setRowCount(0); // limpa a tabela antes

		        if (ordensArray != null && !ordensArray.isEmpty()) {
		            for (Object obj : ordensArray) {
		                JsonObject ordemJson = (JsonObject) obj;

		                Object idObj = ordemJson.get("id");
		                int idOrdem = 0;

		                if (idObj != null && idObj instanceof Number) {
		                    idOrdem = ((Number) idObj).intValue();
		                } else {
		                    System.out.println("ID inválido ou ausente: " + idObj);
		                    continue;
		                }

		                String autor = (String) ordemJson.get("autor");
		                String descricao = (String) ordemJson.get("descricao");
		                String status = (String) ordemJson.get("status");

		                modeloTabela.addRow(new Object[] { idOrdem, autor, descricao, status });
		            }
		        } else {
		            JOptionPane.showMessageDialog(btnListarOrdens, "Nenhuma ordem encontrada com esse filtro.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        }

	            }catch(Exception ex) {
	            	JOptionPane.showMessageDialog(btnListarOrdens, ex, "Erro", JOptionPane.ERROR_MESSAGE);
	            }
		        
		    }else {
		    	 JOptionPane.showMessageDialog(btnListarOrdens, "token não encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		    }
		    }
		});


		
		
		
		btnListarOrdens.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnListarOrdens.setBounds(62, 552, 199, 39);
		contentPane.add(btnListarOrdens);
		
		JButton btnCadastrarOrdem = new JButton("Cadastrar nova ordem");
		btnCadastrarOrdem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				abrirCadastrarOrdem();
				
			}
		});
		btnCadastrarOrdem.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCadastrarOrdem.setBounds(304, 551, 199, 40);
		contentPane.add(btnCadastrarOrdem);
		
		JButton btnEditarOrdens = new JButton("Editar Ordens");
		btnEditarOrdens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirEditarOrdem();
			}
		});
		btnEditarOrdens.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEditarOrdens.setBounds(61, 603, 199, 39);
		contentPane.add(btnEditarOrdens);
		
		JButton btnAlterarOrdens = new JButton("Alterar Ordens");
		btnAlterarOrdens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirAlterarOrdens();
			}
		});
		btnAlterarOrdens.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAlterarOrdens.setBounds(304, 602, 199, 40);
		contentPane.add(btnAlterarOrdens);
	}
}