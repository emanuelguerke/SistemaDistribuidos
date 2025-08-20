package gui;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.*;
import java.net.*;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import service.GerenciadorConexao;

public class ConectarClienteWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIp;
	private JTextField txtPorta;
	private LoginWindow loginWindow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConectarClienteWindow frame = new ConectarClienteWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void abirLogin() {
		
		LoginWindow abrirlogin = new LoginWindow();
		abrirlogin.setVisible(true);
		
		this.setVisible(false);
	}
	/**
	 * Create the frame.
	 */
	public ConectarClienteWindow() {
        setTitle("Cliente - Conectar usuario");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblConectarCliente = new JLabel("Conectar");
		lblConectarCliente.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblConectarCliente.setBounds(178, 10, 78, 25);
		contentPane.add(lblConectarCliente);
		
		JLabel lblIP = new JLabel("ip:");
		lblIP.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblIP.setBounds(38, 70, 34, 30);
		contentPane.add(lblIP);
		
		txtIp = new JTextField();
		txtIp.setBounds(117, 79, 208, 20);
		contentPane.add(txtIp);
		txtIp.setColumns(10);
		
		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPorta.setBounds(34, 127, 67, 25);
		contentPane.add(lblPorta);
		
		txtPorta = new JTextField();
		txtPorta.setBounds(117, 133, 208, 20);
		contentPane.add(txtPorta);
		txtPorta.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String ipServidor = txtIp.getText();
		        int portaServidor = Integer.parseInt(txtPorta.getText());

		        GerenciadorConexao conexao = GerenciadorConexao.getInstance();
		        if (conexao.conectar(ipServidor, portaServidor)) {
		            abirLogin(); // Abre a tela de login
		        } else {
		            System.err.println("Falha ao conectar ao servidor.");
		        }
		    }
		});

		btnConectar.setBounds(167, 185, 89, 23);
		contentPane.add(btnConectar);
	}
}
