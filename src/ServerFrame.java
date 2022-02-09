import javax.swing.*;
import java.awt.*;

public class ServerFrame extends JFrame {

    private ServerGUI sg = new ServerGUI();

    public ServerFrame() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(sg);
    }
}
