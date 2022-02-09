import model.GameW;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ServerGUI extends JFrame {

    private JButton start = new JButton("Starte Spiel");
    private JLabel ip = new JLabel();
    private JLabel ip2;
    private JCheckBox check = new JCheckBox("Stiche dürfen sich ausgehen");

    private GameW g;
    private String ipadress;
    private String ipadress2;

    public ServerGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(start);
        add(check);
        add(ip);
        add(ip2);

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
        if(!ipadress2.isEmpty()) {
            ip2.setText(ipadress2);
        }

        g.setForbidden(true);
        check.setFocusable(false);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.setForbidden(!check.isSelected());
                g.getServer().sendString("SG/");
                g.start();
            }
        });

    }
}