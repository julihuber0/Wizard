package view;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Utility {
    public static float SAMPLE_RATE = 8000f;

    public static ImageIcon resizeIcon(ImageIcon icon, int newWidth, int newHeight) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(newWidth, newHeight, Image.SCALE_REPLICATE);
        return new ImageIcon(resized);
    }

    public static String askInput(String msg) {
        String input = JOptionPane.showInputDialog(msg);
        if(input == null || input.equals("")){
            return askInput(msg);
        }
        return input;
    }

    public static void showInfoDialog(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void playPingSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File f = new File("./Resources/notification.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }

    public static void main(String[] args){
        try {
            playPingSound();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
}
