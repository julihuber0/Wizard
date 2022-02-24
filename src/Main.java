import view.GUINew;
import view.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        GUINew gui = new GUINew();
        gui.setSize(new Dimension(1280, 750));
        gui.setMinimumSize(new Dimension(1280, 750));
        gui.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        gui.setIconImage(icon.getImage());
        gui.setTitle("Wizard");
        gui.setVisible(true);

        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = Utility.showConfirmDialog("Wirklich beenden?", "Beenden");
                if(result == JOptionPane.YES_OPTION) {
                    gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    gui.shutdown();
                } else if (result == JOptionPane.NO_OPTION) {
                    gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        String v;
        try {
            URL myIP = new URL("https://raw.githubusercontent.com/julihuber0/Wizard/master/version.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream()));
            v = in.readLine();
            if(!v.equals(GUINew.VERSION)) {
                int result = Utility.showConfirmDialog("Es ist eine neue Version verfügbar. Jetzt herunterladen?", "Update");
                if(result == JOptionPane.YES_OPTION) {
                    Desktop d = Desktop.getDesktop();
                    d.browse(new URI(GUINew.DOWNLOAD_URL));
                }
            }
        } catch (IOException e) {
            Utility.showInfoDialog("Version kann zurzeit nicht geprüft werden.");
        } catch (URISyntaxException e) {
            Utility.showInfoDialog("Download zurzeit nicht möglich, später erneut versuchen.");
        }
    }
}
