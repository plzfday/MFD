package MFD;

import javax.swing.*;

public class GUI extends JFrame {
    private JPanel rootPanel;
    private JButton play;
    private JButton next;
    private JButton prev;
    private JButton stop;
    private JSlider slider1;
    private JButton addFilesButton;
    private JButton addFoldersButton;
    private JButton settinsButton;
    private JList list1;
    private JSlider slider2;

    GUI() {
        add(rootPanel);

        setTitle("MFD");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
