package MFD;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            GUI myGUI = new GUI();
            myGUI.setVisible(true);
        });

        Runnable r = new PlayMusic("C:\\Users\\qkreh\\Music\\Coldplay - Adventure Of A Lifetime HQ.mp3");
        Thread test = new Thread(r);
        test.start();

        try {
            test.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PlayMusic implements Runnable {
    private MyPlayer mp;

    PlayMusic(String path) {
        mp = new MyPlayer(path);
    }

    @Override
    public void run() {
        mp.Play();
    }
}