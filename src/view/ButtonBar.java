package view;

import javax.swing.*;
import java.awt.*;

public class ButtonBar extends JPanel{

    private JButton join = new JButton("Beitreten");
    private JButton exit = new JButton("Beenden");
    private JLabel cS = new JLabel("Kartengröße:");
    private JComboBox<String> cardSize = new JComboBox<>();

    private GUINew mainGUI;

    public ButtonBar(GUINew parent) {
        mainGUI = parent;
        setLayout(new FlowLayout());

        cardSize.addItem("Normal");
        cardSize.addItem("Klein");
        cardSize.setSelectedIndex(0);

        join.addActionListener(e -> {
            String ipAddress = Utility.askInput("Server-IP-Adresse");
            if(ipAddress != null) {
                mainGUI.setCClient(ipAddress);
                int size = cardSize.getSelectedIndex();
                if (size == 0) {
                    mainGUI.setInitScale(1.0);
                } else {
                    mainGUI.setInitScale(0.9);
                    mainGUI.setMinimumSize(new Dimension(1280, 700));
                }
            }
        });
        exit.addActionListener(e -> {
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
        });

        add(join);
        add(exit);
        add(cS);
        add(cardSize);
    }
}
