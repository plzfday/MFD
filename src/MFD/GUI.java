package MFD;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class GUI extends JFrame {
    private JPanel rootPanel;
    private JButton play;
    private JButton next;
    private JButton prev;
    private JButton stop;
    private JSlider volumeBar;
    private JButton addFilesButton;
    private JButton addFoldersButton;
    private JButton settingsButton;
    private JList<Object> playlist;
    private JSlider loadBar;
    private JButton pause;
    private DefaultListModel<Object> listModel;

    GUI() {
        UCustom();
        add(rootPanel);
        setTitle("MFD");
        setSize(450, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFilesButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "mp3 files", "mp3");
            chooser.setFileFilter(filter);
            int retVal = chooser.showOpenDialog(rootPanel);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                String listName = file.getName();
                System.out.println("곡 이름: " + listName);

                listName = listName.replace(".mp3", "");

                listModel.addElement(listName);
                playlist.setModel(listModel);
            }
        });
        addFoldersButton.addActionListener(e -> {

        });
        playlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    System.out.println("Value : " + playlist.getSelectedValue());


                }
            }
        });
    }

    private void UCustom() {
        UsetbuttonBorder(new JButton[]{play, next, prev, stop, pause, addFilesButton, addFoldersButton, settingsButton});
        listModel = new DefaultListModel<>();
        if (playlist.getModel().getSize() > 0) {
            listModel.addElement(playlist.getModel());
        }
    }

    private void UsetbuttonBorder(JButton[] button) {
        for (JButton aButton : button) {
            aButton.setMargin(new Insets(0, 0, 0, 0));
            aButton.setContentAreaFilled(false);
            aButton.setBorder(null);
        }
    }
}
