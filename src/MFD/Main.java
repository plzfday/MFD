package MFD;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            GUI myGUI = new GUI();
            try {
                MusicList.LoadDataFromFile(myGUI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myGUI.setVisible(true);
        });


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                MusicList.SaveDataInFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }));
    }
}

