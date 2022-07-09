package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonBar extends JPanel {

    private JLabel directJoin = new JLabel("IP-Adresse eingeben");
    private JLabel exit = new JLabel("Beenden");
    private JLabel serverJoin = new JLabel("Ausgewähltem Server beitreten");
    private JComboBox<String> serverlist = new JComboBox<>();
    private String[] ipAddresses = {"wizardjw.ddns.net", "wizardjh.ddns.net", "wizardgame.ddns.net"};
    private JLabel cS = new JLabel("Kartengröße:");
    private JComboBox<String> cardSize = new JComboBox<>();
    private JLabel soundPackage = new JLabel("Soundpaket:");
    private JComboBox<String> soundSelector = new JComboBox<>(GUINew.SOUNDPACKS);

    private GUINew mainGUI;

    public ButtonBar(GUINew parent) {
        mainGUI = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel first = new JPanel();
        first.setLayout(new FlowLayout());
        first.setOpaque(false);
        add(first);

        JPanel second = new JPanel();
        second.setLayout(new FlowLayout());
        second.setOpaque(false);
        add(second);

        JPanel third = new JPanel();
        third.setLayout(new FlowLayout());
        third.setOpaque(false);
        add(third);

        JPanel fourth = new JPanel();
        fourth.setLayout(new FlowLayout());
        fourth.setOpaque(false);
        add(fourth);

        serverlist.addItem("Julian Wohnung");
        serverlist.addItem("Julian Home");
        serverlist.addItem("Tobias");
        serverlist.setSelectedIndex(0);

        cardSize.addItem("Normal");
        cardSize.addItem("Klein");
        cardSize.setSelectedIndex(0);

        soundSelector.setSelectedIndex(0);

        directJoin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String ipAddress = Utility.askInput("Server-IP-Adresse");
                if (ipAddress != null) {
                    mainGUI.setCClient(ipAddress);
                    int size = cardSize.getSelectedIndex();
                    if (size == 0) {
                        mainGUI.setInitScale(1.0);
                    } else {
                        mainGUI.setInitScale(0.9);
                        mainGUI.setMinimumSize(new Dimension(1280, 700));
                    }
                    mainGUI.setSoundPackage(soundSelector.getSelectedIndex());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                directJoin.setFont(new Font("Candara", Font.BOLD, 35));
                directJoin.setForeground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                directJoin.setFont(new Font("Candara", Font.PLAIN, 30));
                directJoin.setForeground(Color.WHITE);
            }
        });

        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Frame frame : Frame.getFrames()) {
                    frame.dispose();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                exit.setFont(new Font("Candara", Font.BOLD, 35));
                exit.setForeground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                exit.setFont(new Font("Candara", Font.PLAIN, 30));
                exit.setForeground(Color.WHITE);
            }
        });

        serverJoin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                String ipAddress = ipAddresses[serverlist.getSelectedIndex()];
                mainGUI.setCClient(ipAddress);
                int size = cardSize.getSelectedIndex();
                if (size == 0) {
                    mainGUI.setInitScale(1.0);
                } else {
                    mainGUI.setInitScale(0.9);
                    mainGUI.setMinimumSize(new Dimension(1280, 700));
                }
                mainGUI.setSoundPackage(soundSelector.getSelectedIndex());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                serverJoin.setFont(new Font("Candara", Font.BOLD, 35));
                serverJoin.setForeground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                serverJoin.setFont(new Font("Candara", Font.PLAIN, 30));
                serverJoin.setForeground(Color.WHITE);
            }
        });

        directJoin.setForeground(Color.WHITE);
        directJoin.setFont(new Font("Candara", Font.PLAIN, 30));
        serverJoin.setForeground(Color.WHITE);
        serverJoin.setFont(new Font("Candara", Font.PLAIN, 30));
        cS.setForeground(Color.WHITE);
        cS.setFont(new Font("Candara", Font.PLAIN, 30));
        soundPackage.setForeground(Color.WHITE);
        soundPackage.setFont(new Font("Candara", Font.PLAIN, 30));
        exit.setForeground(Color.WHITE);
        exit.setFont(new Font("Candara", Font.PLAIN, 30));

        first.add(directJoin);
        second.add(serverJoin);
        second.add(serverlist);
        third.add(cS);
        third.add(cardSize);
        third.add(soundPackage);
        third.add(soundSelector);
        fourth.add(exit);
    }
}
