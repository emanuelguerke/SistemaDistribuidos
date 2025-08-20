package service;

import java.io.*;
import java.net.*;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class GerenciadorConexao {
    private static GerenciadorConexao instancia;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private static String token;

    private GerenciadorConexao() {}

    public static synchronized GerenciadorConexao getInstance() {
        if (instancia == null) {
            instancia = new GerenciadorConexao();
        }
        return instancia;
    }
    // Método para definir o token
    public static void setToken(String novoToken) {
        token = novoToken;
    }

    // Método para recuperar o token
    public static String getToken() {
        return token;
    }
    
    public boolean conectar(String host, int porta) {
        try {
            socket = new Socket();
            
            // Define um tempo limite para a conexão (exemplo: 5 segundos)
            int timeout = 5000;
            socket.connect(new InetSocketAddress(host, porta), timeout);
            
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Conectado ao servidor!");

            return true;
        } catch (SocketTimeoutException e) {
            System.err.println("Erro: O tempo para conectar ao servidor excedeu o limite.");
        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível estabelecer conexão com o servidor.");
        } catch (IOException e) {
            System.err.println("Erro de entrada/saída: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro: A porta deve ser um número inteiro válido.");
        }

        return false;
    }

    public void enviarMensagem(String mensagem) {
        if (writer != null) {
            writer.println(mensagem);
            //System.out.println("Cliente enviou: "+mensagem);
        }
    }

    public String receberMensagem() {
        try {
            return (reader != null) ? reader.readLine() : null;
        } catch (IOException e) {
            System.err.println("Erro ao receber mensagem: " + e.getMessage());
            return null;
        }
    }
    public void enviarJson(JsonObject jsonObject) {
        if (writer != null) {
            writer.println(Jsoner.serialize(jsonObject));
            System.out.println("Cliente enviou : "+Jsoner.serialize(jsonObject));
        }
    }

    public boolean isConectado() {
        return (socket != null && socket.isConnected());
    }

    public void fecharConexao() {
        try {
            if (socket != null) socket.close();
            System.out.println("Conexão encerrada.");
        } catch (IOException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}
