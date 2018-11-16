package MFD;

import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

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
    private JLabel Status;
    private DefaultListModel<Object> listModel;

    private MusicPlayer mp;
    private int lastIndex;

    GUI() {
        UCustom();
        add(rootPanel);
        setTitle("MFD");
        setSize(450, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFilesButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("C:\\Users\\qkreh\\Music");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "mp3 files", "mp3");
            chooser.setFileFilter(filter);
            int retVal = chooser.showOpenDialog(rootPanel);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                String listName = file.getName();
                System.out.println("곡 이름: " + listName);

                MusicList.paths.add(file.getAbsolutePath());

                listName = listName.replace(".mp3", "");

                MusicList.name.add(listName);
                listModel.addElement(listName);
                playlist.setModel(listModel);

                MusicList.printAll();
            }
        });
        addFoldersButton.addActionListener(e -> {

        });
        playlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int Index = playlist.getSelectedIndex();
                    uPlayInCase(Index, MusicList.name.get(Index));
                }
            }
        });
        pause.addActionListener(e -> {
            if (mp != null) {
                mp.pause();
                if (!mp.isPaused()) setStatus("Paused");
            }
        });

        play.addActionListener(e -> {
            int Index = playlist.getSelectedIndex();
            if (Index != -1) {
                uPlayInCase(Index, MusicList.name.get(Index));
            }
        });
        stop.addActionListener(e -> {
            if (mp != null) {
                mp.stop();
                setStatus("Not Playing");
            }
        });
        prev.addActionListener(e -> {
            int prevIndex = lastIndex - 1;
            if (prevIndex >= 0) {
                uPlayInCase(prevIndex, MusicList.name.get(prevIndex));
            }
        });
        next.addActionListener(e -> {
            int nextIndex = lastIndex + 1;
            if (nextIndex < MusicList.paths.size()) {
                uPlayInCase(nextIndex, MusicList.name.get(nextIndex));
            }
        });
    }

    private void UCustom() {
        uSetButtonBorder(new JButton[]{play, next, prev, stop, pause, addFilesButton, addFoldersButton, settingsButton});
        listModel = new DefaultListModel<>();
        if (playlist.getModel().getSize() > 0) {
            listModel.addElement(playlist.getModel());
        }

    }

    private void uSetButtonBorder(JButton[] button) {
        for (JButton aButton : button) {
            aButton.setMargin(new Insets(0, 0, 0, 0));
            aButton.setContentAreaFilled(false);
            aButton.setBorder(null);
        }
    }

    private void uPlayInCase(int Index, String src) {
        String filePath = MusicList.paths.get(Index);
        if (mp == null) { // 프로그램 시작했을 때
            mp = new MusicPlayer();
            try {
                mp.play(filePath);
                setStatus("Playing");
                setTitle(src);
                lastIndex = Index;
            } catch (IOException | JavaLayerException ignored) {
            }
        } else if (mp.isPaused()) { // 재생 중단 됐을 때 다시 그 부분부터 시작
            try {
                mp.resume();
                setStatus("Playing");
                lastIndex = Index;
            } catch (IOException | JavaLayerException ignored) {
            }
        } else { // 평상시(일반적일 때)
            try {
                mp.stop();
                mp.play(filePath);
                setStatus("Playing");
                setTitle(src);
                lastIndex = Index;
            } catch (JavaLayerException | IOException ignored) {
            }
        }
    }

    private void setStatus(String src) {
        Status.setText(src);
        Status.setHorizontalTextPosition(JLabel.CENTER);
        Status.setHorizontalAlignment(JLabel.CENTER);
    }
}
