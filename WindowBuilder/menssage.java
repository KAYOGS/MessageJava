import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;  // Usando a classe Color do pacote java.awt

import helper_classes.CustomFontLoader;
import helper_classes.OnClickEventHelper;
import helper_classes.OnFocusEventHelper;
import helper_classes.RoundedBorder;

public class menssage {
    private static JTextArea messageArea;
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final int PORT = 12345; // Porta fixa para comunicação
    private static String localIP;

    public static void main(String[] args) {
        // Obter IP local
        try {
            localIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            localIP = "127.0.0.1"; // Fallback para localhost
        }

        JFrame frame = new JFrame("Mensagem App - Meu IP: " + localIP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Resolução 800x600

        // Carregar e definir o ícone da janela
        try {
            java.awt.Image icon = ImageIO.read(new File("./resources/icon.png")); // Caminho para o ícone
            frame.setIconImage(icon);
        } catch (IOException e) {
            System.out.println("Erro ao carregar o ícone: " + e.getMessage());
        }

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.decode("#1e1e1e"));  // Usando java.awt.Color

        // Label "Mensagem"
        JLabel label1 = new JLabel("Mensagem");
        label1.setBounds(40, 30, 106, 22);
        label1.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 18));
        label1.setForeground(Color.decode("#D9D9D9"));  // Usando java.awt.Color
        panel.add(label1);

        // Área de exibição de mensagens
        messageArea = new JTextArea();
        messageArea.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
        messageArea.setBackground(Color.decode("#2e2e2e"));  // Usando java.awt.Color
        messageArea.setForeground(Color.decode("#D9D9D9"));  // Usando java.awt.Color
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBounds(40, 60, 720, 400);
        scrollPane.setBorder(new RoundedBorder(2, Color.decode("#979797"), 0));  // Usando java.awt.Color
        panel.add(scrollPane);

        // Campo Destinatário (IP do destinatário)
        JTextField destinatario = new JTextField("");
        destinatario.setBounds(40, 480, 300, 21);
        destinatario.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
        destinatario.setBackground(Color.decode("#B2B2B2"));  // Usando java.awt.Color
        destinatario.setForeground(Color.decode("#656565"));  // Usando java.awt.Color
        destinatario.setBorder(new RoundedBorder(2, Color.decode("#979797"), 0));  // Usando java.awt.Color
        OnFocusEventHelper.setOnFocusText(destinatario, "IP do Destinatário", Color.decode("#353535"),
                Color.decode("#656565"));
        panel.add(destinatario);

        // Campo Mensagem
        JTextField mensagem = new JTextField("");
        mensagem.setBounds(40, 510, 300, 21);
        mensagem.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
        mensagem.setBackground(Color.decode("#B2B2B2"));  // Usando java.awt.Color
        mensagem.setForeground(Color.decode("#656565"));  // Usando java.awt.Color
        mensagem.setBorder(new RoundedBorder(2, Color.decode("#979797"), 0));  // Usando java.awt.Color
        OnFocusEventHelper.setOnFocusText(mensagem, "Mensagem", Color.decode("#353535"), Color.decode("#656565"));
        panel.add(mensagem);

        // Botão Enviar
        JButton enviar = new JButton("Enviar");
        enviar.setBounds(650, 500, 106, 28);
        enviar.setBackground(Color.decode("#2e2e2e"));  // Usando java.awt.Color
        enviar.setForeground(Color.decode("#D9D9D9"));  // Usando java.awt.Color
        enviar.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
        enviar.setBorder(new RoundedBorder(4, Color.decode("#979797"), 1));  // Usando java.awt.Color
        enviar.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(enviar, Color.decode("#232323"), Color.decode("#2e2e2e"));  // Usando java.awt.Color
        panel.add(enviar);

        frame.add(panel);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela
        frame.setVisible(true);

        // Iniciar thread para escutar mensagens
        startMessageListener();

        // Ação do botão Enviar
        enviar.addActionListener(e -> {
            String destIP = destinatario.getText().trim();
            String msg = mensagem.getText().trim();
            if (!destIP.isEmpty() && !msg.isEmpty() && !destIP.equals("IP do Destinatário")
                    && !msg.equals("Mensagem")) {
                sendMessage(destIP, msg);
                messageArea.append("Eu para " + destIP + ": " + msg + "\n");
                mensagem.setText(""); // Limpa o campo após envio
            }
        });
    }

    // Método para escutar mensagens recebidas
    private static void startMessageListener() {
        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()))) {
                        String receivedMessage = in.readLine();
                        if (receivedMessage != null) {
                            String senderIP = clientSocket.getInetAddress().getHostAddress();
                            SwingUtilities.invokeLater(
                                    () -> messageArea.append(senderIP + " diz: " + receivedMessage + "\n"));
                        }
                    } catch (IOException e) {
                        SwingUtilities.invokeLater(
                                () -> messageArea.append("Erro ao receber mensagem: " + e.getMessage() + "\n"));
                    }
                }
            } catch (IOException e) {
                SwingUtilities
                        .invokeLater(() -> messageArea.append("Erro ao iniciar o ouvinte: " + e.getMessage() + "\n"));
            }
        });
    }

    // Método para enviar mensagens
    private static void sendMessage(String destIP, String message) {
        executor.submit(() -> {
            try (Socket socket = new Socket(destIP, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(message);
            } catch (IOException e) {
                SwingUtilities.invokeLater(
                        () -> messageArea.append("Erro ao enviar para " + destIP + ": " + e.getMessage() + "\n"));
            }
        });
    }
}
