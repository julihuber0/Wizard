package view;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The Utility class provides some general functionality that is used all over
 * the project.
 */
public class Utility {

    /**
     * This resized an ImageIcon to a new size.
     *
     * @param icon      The icon to be resized.
     * @param newWidth  The icon's width after resizing.
     * @param newHeight The icon's height after resizing.
     * @return The resized ImageIcon.
     */
    public static ImageIcon resizeIcon(ImageIcon icon, int newWidth, int newHeight) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    /**
     * Shows a new popup window with a text field to enter some text.
     *
     * @param msg The message to be shown in the popup window.
     * @return The text that was written into the text field.
     */
    public static String askInput(String msg) {
        String input = JOptionPane.showInputDialog(msg);
        if (input != null && input.equals("")) {
            return askInput(msg);
        }
        return input;
    }

    /**
     * Shows a new popup window with some information text and an "OK" button.
     *
     * @param msg The message to be shown in the popup window.
     */
    public static void showInfoDialog(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * Shows a new popup window with some information and a "YES" and "NO"
     * button to confirm or deny.
     *
     * @param msg    The message to be shown in the popup window.
     * @param header The popup window header text.
     * @return The option that was chosen with one of the two buttons.
     */
    public static int showConfirmDialog(String msg, String header) {
        return JOptionPane.showConfirmDialog(null, msg, header, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Plays a ping notification sound, used here for new unseen chat messages.
     */
    public static void playPingSound() {
        File f = new File("./Resources/notification.wav");
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(f);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            clip.open(audioIn);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        clip.start();
    }

    /**
     * Plays the given sound from the given folder. This is used mainly for
     * the quick reactions in the chat, as we pass also the folder name of
     * one of the different sound packages available.
     *
     * @param filename     The audio file's name.
     * @param soundPackage The number of the sound package according to the
     *                     array in the GUINew class.
     */
    public static void playSound(String filename, int soundPackage) {
        File f = new File("./Resources/" + GUINew.SOUNDPACKS[soundPackage] + "/" + filename);
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(f);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            clip.open(audioIn);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10.0f);
        clip.start();
    }

}
