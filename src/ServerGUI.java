import model.GameW;
import view.VerticalFlowLayout;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ServerGUI extends JPanel {

    private JButton start = new JButton("Starte Spiel");
    private JPanel ipPanel = new JPanel();
    private JLabel ipLabel = new JLabel("Server-IP:");
    private JTextField ip = new JTextField();
    private JLabel ip2 = new JLabel();
    private JCheckBox check = new JCheckBox("Stiche dürfen sich ausgehen");

    private GameW g;
    private String ipadress;
    Thread game;

    public ServerGUI() {
        setLayout(new VerticalFlowLayout(FlowLayout.CENTER, FlowLayout.CENTER));
        g = new GameW();
        Font standard = new Font("Segoe UI", Font.PLAIN, 20);

        ipPanel.setLayout(new FlowLayout());
        ip.setBackground(Color.WHITE);
        ip.setPreferredSize(new Dimension(160,25));
        ip.setHorizontalAlignment(JTextField.CENTER);
        ip.setEditable(false);
        ipLabel.setFont(standard);
        ipPanel.add(ipLabel);
        ipPanel.add(ip);


        add(start);
        add(check);
        add(ipPanel);
        check.setFont(standard);
        ip.setFont(standard);
        ip2.setFont(standard);

        try {
            URL myIP = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream()));
            ipadress = in.readLine();
        } catch (Exception e) {
            ipadress = "IP-Lesefehler!";
        }
        ip.setText(ipadress);
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
