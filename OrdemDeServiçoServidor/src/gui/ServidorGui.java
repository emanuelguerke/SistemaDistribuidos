package gui;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import service.ClienteHandler;
import java.awt.Font;

public class ServidorGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private static ServidorGui instancia; // Instância única (Singleton)
    
    private JTextArea txtLog;
    private DefaultListModel<String> listaUsuariosModel;
    private Set<String> usuariosConectados;
    private int porta; // Porta do servidor
    private boolean servidorLigado = false; // Estado do servidor
    private ServerSocket serverSocket;
    private Thread servidorThread;
    private JButton btnIniciar;

    private ServidorGui() {
        setTitle("Servidor - Usuarios Conectados");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblPorta = new JLabel("Digite a porta:");
        lblPorta.setBounds(10, 10, 100, 20);
        getContentPane().add(lblPorta);

        JTextField txtPorta = new JTextField();
        txtPorta.setBounds(110, 10, 100, 20);
        getContentPane().add(txtPorta);

        btnIniciar = new JButton("Iniciar Servidor");
        btnIniciar.setBounds(220, 10, 150, 20);
        btnIniciar.setBackground(Color.RED);
        getContentPane().add(btnIniciar);

        // Área de log com barra de rolagem
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(10, 63, 410, 100);
        getContentPane().add(scrollLog);

        // Lista de usuários com barra de rolagem
        listaUsuariosModel = new DefaultListModel<>();
        JList<String> listaUsuarios = new JList<>(listaUsuariosModel);
        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        scrollUsuarios.setBounds(10, 199, 410, 140);
        getContentPane().add(scrollUsuarios);

        usuariosConectados = new HashSet<>();

        btnIniciar.addActionListener(e -> alternarServidor(txtPorta));
        
        JLabel lblUsuariosConectados = new JLabel("Usuarios conectados");
        lblUsuariosConectados.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUsuariosConectados.setBounds(120, 174, 180, 14);
        getContentPane().add(lblUsuariosConectados);
        
        JLabel lblLog = new JLabel("Log");
        lblLog.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblLog.setBounds(180, 38, 54, 25);
        getContentPane().add(lblLog);
    }

    public static synchronized ServidorGui getInstance() {
        if (instancia == null) {
            instancia = new ServidorGui();
        }
        return instancia;
    }
    
    public boolean usuarioJaConectado(String usuario) {
        return usuariosConectados.contains(usuario);
    }

    private void alternarServidor(JTextField txtPorta) {
        if (!servidorLigado) {
            try {
                porta = Integer.parseInt(txtPorta.getText());
                servidorLigado = true;
                servidorThread = new Thread(this::iniciarServidor);
                servidorThread.start();
                btnIniciar.setText("Desligar Servidor");
                btnIniciar.setBackground(Color.GREEN);
                log("Servidor iniciado na porta " + porta);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Digite um número válido para a porta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            desligarServidor();
        }
    }

    private void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(porta);
            log("Servidor aguardando conexões...");

            while (servidorLigado) {
                try {
                    serverSocket.setSoTimeout(500); 
                    Socket clientSocket = serverSocket.accept();
                    
                    if (!servidorLigado) {
                        clientSocket.close();
                        break;
                    }

                    new Thread(new ClienteHandler(clientSocket)).start();

                } catch (SocketTimeoutException e) {
                    
                } catch (IOException e) {
                    if (!servidorLigado) {
                        break;
                    }
                    log("Erro ao aceitar conexão: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            log("Erro ao iniciar servidor: " + e.getMessage());
        }
    }

    private void desligarServidor() {
        servidorLigado = false; // flag para fechar conexão

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Fecha o socket para impedir novas conexões
            }

            if (servidorThread != null && servidorThread.isAlive()) {
                servidorThread.interrupt(); // Interrompe a Thread corretamente
            }
            
            Thread.sleep(500); 

            SwingUtilities.invokeLater(() -> {
                btnIniciar.setText("Iniciar Servidor");
                btnIniciar.setBackground(Color.RED);
                log("Servidor desligado.");
            });

        } catch (IOException | InterruptedException e) {
            log("Erro ao desligar o servidor: " + e.getMessage());
        }
    }




    private void log(String mensagem) {
        txtLog.append(mensagem + "\n");
    }

    public void adicionarUsuario(String usuario) {
        if (usuariosConectados.add(usuario)) {
            listaUsuariosModel.addElement(usuario);
            log("Usuário conectado: " + usuario);
        }
    }

    public void removerUsuario(String usuario) {
        if (usuariosConectados.remove(usuario)) {
            listaUsuariosModel.removeElement(usuario);
            log("Usuário desconectado: " + usuario);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> ServidorGui.getInstance().setVisible(true));
    }
}
