import helper_classes.*;
import java.awt.Color;
import javax.swing.*;

public class WindowBuilder {
  public static void main(String[] args) {

     JFrame frame = new JFrame("Mensagem App");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(938, 507);
     JPanel panel = new JPanel();
     panel.setLayout(null);
     panel.setBackground(Color.decode("#1e1e1e"));

     JLabel label1 = new JLabel("Mensagem");
     label1.setBounds(54, 35, 106, 22);
     label1.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 18));
     label1.setForeground(Color.decode("#D9D9D9"));
     panel.add(label1);

     JTextField destinatario = new JTextField("");
     destinatario.setBounds(55, 380, 350, 21);
     destinatario.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
     destinatario.setBackground(Color.decode("#B2B2B2"));
     destinatario.setForeground(Color.decode("#656565"));
     destinatario.setBorder(new RoundedBorder(2, Color.decode("#979797"), 0));
     OnFocusEventHelper.setOnFocusText(destinatario, "Destinatario", Color.decode("#353535"),   Color.decode("#656565"));
     panel.add(destinatario);

     JTextField mensagem = new JTextField("");
     mensagem.setBounds(58, 415, 350, 21);
     mensagem.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
     mensagem.setBackground(Color.decode("#B2B2B2"));
     mensagem.setForeground(Color.decode("#656565"));
     mensagem.setBorder(new RoundedBorder(2, Color.decode("#979797"), 0));
     OnFocusEventHelper.setOnFocusText(mensagem, "Mensagem", Color.decode("#353535"),   Color.decode("#656565"));
     panel.add(mensagem);

     JButton enviar = new JButton("Enviar");
     enviar.setBounds(774, 414, 106, 28);
     enviar.setBackground(Color.decode("#2e2e2e"));
     enviar.setForeground(Color.decode("#D9D9D9"));
     enviar.setFont(CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 14));
     enviar.setBorder(new RoundedBorder(4, Color.decode("#979797"), 1));
     enviar.setFocusPainted(false);
     OnClickEventHelper.setOnClickColor(enviar, Color.decode("#232323"), Color.decode("#2e2e2e"));
     panel.add(enviar);

     frame.add(panel);
     frame.setVisible(true);

  }
}