package MFD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Base extends JFrame {
    private JFrame frame;
    private JPanel panel;

    private JMenuBar menuBar;
    private JMenu file, help;
    private JMenuItem open, addFiles, addFolders, addPlaylist, loadPlaylist, savePlaylist, exit;

    Base() {
        panel = new JPanel();

        frame = new JFrame("MFD");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(670, 470));
        frame.setSize(new Dimension(670, 470));

        // Add MenuBar
        menuBar = new JMenuBar();

        /* File 메뉴바
         * [항목]
         * - Open
         * - Add files
         * - Add folders
         * - Add playlist
         * - Load playlist
         * - Save playlist
         * - Exit
         */
        file = new JMenu("File");
        /* Open
         * 파일을 열기만하고 재생목록에는 추가하지 않음
         */
        open = new JMenuItem("Open");
        open.addActionListener(e -> {

        });
        file.add(open);

        /* Add files
         * 파일을 열어서 재생목록에 추가함
         */
        addFiles = new JMenuItem("Add Files");
        addFiles.addActionListener(e -> {

        });
        file.add(addFiles);

        /* Add folders
         * 폴더 전체를 스캔해서 재생목록에 추가함
         */
        addFolders = new JMenuItem("Add Folders");
        addFolders.addActionListener(e -> {

        });
        file.add(addFolders);

        /* Add playlist
         * 재생목록을 하나 만든다.
         * 저장은 안 했기 때문에 만약 프로그램을 종료시키려고 하면 진짜로 끌껀지 물어봐야 함.
         */
        addPlaylist = new JMenuItem("Add Playlist");
        addPlaylist.addActionListener(e -> {

        });
        file.add(addPlaylist);

        /* Load playlist
         * 저장되어 있던 재생목록을 불러온다.
         */
        loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.addActionListener(e -> {

        });
        file.add(loadPlaylist);

        /* Save playlist
         * 재생목록을 저장한다.
         */
        savePlaylist = new JMenuItem("Save Playlist");
        savePlaylist.addActionListener(e -> {

        });
        file.add(savePlaylist);

        /* Exit
         * 프로그램 종료
         */
        exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        file.add(exit);
        menuBar.add(file);


        help = new JMenu("Help");

        menuBar.add(help);

        panel.setBackground(new Color(255, 96, 91));
        frame.add(panel);
        panel.add(menuBar);
        frame.setJMenuBar(menuBar);
    }
}
