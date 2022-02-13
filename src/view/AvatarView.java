package view;

import javax.swing.*;
import java.awt.*;

public class AvatarView extends JPanel {

    private MarkerColor mc;
    private boolean scalable = true;

    public AvatarView(MarkerColor mc) {
        setMarkerColor(mc);
    }

    public void setMarkerColor(MarkerColor mc) {
        this.mc = mc;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(mc != null) {
            g2.drawImage(getAvatarImage(), 0, 0, (int) (100*getScaleFactor()), (int) (100*getScaleFactor()), null);
        }
    }

    public Image getAvatarImage() {
        ImageIcon ic;
        if(mc == MarkerColor.NONE) {
            ic = new ImageIcon("./Resources/avatar.png");
        } else if(mc == MarkerColor.GREEN) {
            ic = new ImageIcon("./Resources/avatarRight.png");
        } else if(mc == MarkerColor.RED) {
            ic = new ImageIcon("./Resources/avatarWrong.png");
        } else {
            ic = new ImageIcon("./Resources/avatarMarked.png");
        }
        return ic.getImage();
    }

    private double getScaleFactor() {
        if(scalable) {
            Frame jf = Frame.getFrames()[0];
            double dim = Math.min(jf.getHeight(), jf.getWidth());
            double scale = dim / 690;
            if(scale < 1) {
                return scale;
            }
        }
        return 1;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (100*getScaleFactor()), (int) (100*getScaleFactor()));
        //return new Dimension(200 , 200);
    }
}
