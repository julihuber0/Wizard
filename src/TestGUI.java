import javax.swing.*;
import java.awt.*;

public class TestGUI extends JFrame {

    public TestGUI() {
        setLayout(new BorderLayout());

        CardView cv = new CardView(new Card(12, ColorW.YELLOW));
        add(cv, BorderLayout.CENTER);
    }
}
