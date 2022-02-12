package view;

import model.ColorW;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;

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

    public static void tone(int hz, int msecs, double vol)
            throws LineUnavailableException
    {
        byte[] buf = new byte[1];
        AudioFormat af =
                new AudioFormat(
                        SAMPLE_RATE, // sampleRate
                        8,           // sampleSizeInBits
                        1,           // channels
                        true,        // signed
                        false);      // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs*8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
    public static void main(String[] args) throws Exception {
        Utility.tone(1000,100,0.3);
        Thread.sleep(1000);
        Utility.tone(100,1000,0.3);
        Thread.sleep(1000);
        Utility.tone(5000,100,0.3);
        Thread.sleep(1000);
        Utility.tone(400,500,0.3);
        Thread.sleep(1000);
        Utility.tone(400,500, 0.2);

    }
}
