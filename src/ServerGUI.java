import model.GameW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ServerGUI extends JPanel {

    private JButton start = new JButton("Starte Spiel");
    private JLabel ip = new JLabel();
    private JLabel ip2 = new JLabel();
    private JCheckBox check = new JCheckBox("Stiche dürfen sich ausgehen");
    private JButton quit = new JButton("Beenden");

    private GameW g;
    private String ipadress = "";
    private String ipadress2 = "";
    Thread game;
    boolean isStarted = false;

    public ServerGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        g = new GameW();
        Font standard = new Font("Segoe UI", Font.PLAIN, 20);
        add(start);
        add(check);
        add(ip);
        add(ip2);
        check.setFont(standard);
        ip.setFont(standard);
        ip2.setFont(standard);

        try {
            URL myIP = new URL("http://myip.dnsomatic.com/");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(myIP.openStream())
            );
            ipadress = in.readLine();
        } catch (Exception e) {
            ipadress = "Fehler beim Anzeigen der eigenen IP-Adresse.";
            ipadress2 = "Der Server startet ganz normal.";
        }
        ip.setText(ipadress);
        if (!ipadress2.isEmpty()) {
            ip2.setText(ipadress2);
        }
        check.setFocusable(false);

        start.addActionListener(e -> {
            start.setVisible(false);
            game = new Thread(() -> {
                g.setForbidden(!check.isSelected());
                g.getServer().sendString("SG/");
                g.start();
            });
            game.start();
        });
    }
}
