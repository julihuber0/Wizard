package view;

import javax.swing.*;
import java.awt.*;

public class ButtonBar extends JPanel {

    private JLabel directJoin = new JLabel("Eigene IP-Adresse:");
    private JButton join = new JButton("Beitreten");
    private JButton exit = new JButton("Beenden");
    private JLabel serverJoin = new JLabel("Serverauswahl:");
    private JComboBox<String> serverlist = new JComboBox<>();
    private String[] ipAddresses = {"wizardjw.ddns.net", "wizardjh.ddns.net", "wizardgame.ddns.net"};
    private JButton joinServerList = new JButton("Beitreten");
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
        add(first);

        JPanel second = new JPanel();
        second.setLayout(new FlowLayout());
        add(second);

        JPanel third = new JPanel();
        third.setLayout(new FlowLayout());
        add(third);

        JPanel fourth = new JPanel();
        fourth.setLayout(new FlowLayout());
        add(fourth);

        serverlist.addItem("Julian Wohnung");
        serverlist.addItem("Julian Home");
        serverlist.addItem("Tobias");
        serverlist.setSelectedIndex(0);

        cardSize.addItem("Normal");
        cardSize.addItem("Klein");
        cardSize.setSelectedIndex(0);

        soundSelector.setSelectedIndex(0);

        join.addActionListener(e -> {
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
        });
        exit.addActionListener(e -> {
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
        });
        joinServerList.addActionListener(e -> {
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
        });

        first.add(directJoin);
        first.add(join);
        second.add(serverJoin);
        second.add(serverlist);
        second.add(joinServerList);
        third.add(cS);
        third.add(cardSize);
        third.add(soundPackage);
        third.add(soundSelector);
        fourth.add(exit);
    }
}
