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
	private JButton addFilesButton;
	private JButton settingsButton;
	private JList<Object> playlist;
	private JButton pause;
	private JLabel Status;
	private JButton removeButton;
	private JButton addFolderButton;
	private JButton forButton;
	private JButton backButton;
	private DefaultListModel<Object> listModel;

	private MusicPlayer mp;
	private int lastIndex;
	private int FBTimes;

	GUI() {
		lastIndex = -1;
		FBTimes = 5;
		uCustom();
		add(rootPanel);
		setTitle("MFD");
		setSize(450, 500);
		setPreferredSize(new Dimension(450, 500));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addFilesButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser("C:\\Users\\qkreh\\Music");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3 files", "mp3");
			chooser.setFileFilter(filter);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(true);
			int retVal = chooser.showOpenDialog(rootPanel);
			if (retVal == JFileChooser.APPROVE_OPTION) {
				File[] file = chooser.getSelectedFiles();

				for (File f : file) {
					if (!f.exists())
						JOptionPane.showMessageDialog(chooser, "Invalid File");
					else
						AddMusic(f);
				}
			}
		});
		addFolderButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File dirPath = chooser.getSelectedFile();

				System.out.println(dirPath.getAbsolutePath());

				File[] listOfFiles = dirPath.listFiles();

				assert listOfFiles != null;
				for (File listOfFile : listOfFiles) {
					if (isMP3File(listOfFile)) {
						AddMusic(listOfFile);
					}
				}
			}
		});
		playlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					int Index = playlist.getSelectedIndex();
					uPlayInCase(Index);
				}
			}
		});
		pause.addActionListener(e -> {
			if (mp != null) {
				mp.pause();
				if (mp.isPaused()) setStatus("Paused");
			}
		});
		play.addActionListener(e -> {
			int Index = playlist.getSelectedIndex();
			if (Index != -1) {
				uPlayInCase(Index);
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
				playlist.setSelectedIndex(prevIndex);
				uPlayInCase(prevIndex);
			} else {
				playlist.setSelectedIndex(MusicList.name.size() - 1);
				uPlayInCase(MusicList.name.size() - 1);
			}
		});
		next.addActionListener(e -> {
			int nextIndex = lastIndex + 1;
			playlist.clearSelection();
			if (nextIndex < MusicList.paths.size()) {
				playlist.setSelectedIndex(nextIndex);
				uPlayInCase(nextIndex);
			} else {
				playlist.setSelectedIndex(0);
				uPlayInCase(0);
			}
		});
		removeButton.addActionListener(e -> {
			int selectedIndex = playlist.getSelectedIndex();
			if (selectedIndex != -1) {
				listModel.removeElementAt(selectedIndex);

				if (selectedIndex == mp.getNowPlayingIndex()) {
					mp.stop();
					setStatus("Stopped");
					setTitle("MFD");
				}

				playlist.setModel(listModel);
				MusicList.paths.remove(selectedIndex);
				MusicList.name.remove(selectedIndex);
			}
		});
		settingsButton.addActionListener(e -> new SettingFrame(this));
		forButton.addActionListener(e -> mp.forward(FBTimes));
		backButton.addActionListener(e -> mp.back(FBTimes));
	}

	private boolean isMP3File(File file) {
		if (file.isDirectory()) return false;
		String name = file.getName();
		String extension = name.substring(name.length() - 4);
		return file.isFile() && extension.equals(".mp3");
	}

	private void AddMusic(File file) {
		String listName = file.getName();
		System.out.println("곡 이름: " + listName);

		MusicList.paths.add(file.getAbsolutePath());

		listName = listName.substring(0, listName.length() - 4);

		MusicList.name.add(listName);
		listModel.addElement(listName);
		playlist.setModel(listModel);
	}

	void uAddData_list(String src) {
		listModel.addElement(src);
	}

	void uDefaultToList() {
		playlist.setModel(listModel);
	}

	private void uCustom() {
		uSetButtonBorder(new JButton[]{play, next, prev, stop, pause, addFilesButton, addFolderButton, settingsButton, removeButton});
		listModel = new DefaultListModel<>();
		if (playlist.getModel().getSize() > 0) {
			listModel.addElement(playlist.getModel());
		}
	}

	private void uSetButtonBorder(JButton[] button) {
		for (JButton aButton : button) {
//            aButton.setMargin(new Insets(0, 0, 0, 0));
			aButton.setContentAreaFilled(false);
//            aButton.setBorder(null);
		}
	}

	private void uPlayInCase(int Index) {
		String filePath = MusicList.paths.get(Index);
		String src = MusicList.name.get(Index);
		if (mp == null) { // 프로그램 시작했을 때
			mp = new MusicPlayer();
			try {
				mp.play(filePath, Index);
				setStatus("Playing");
				setTitle(src);
				lastIndex = Index;
			} catch (IOException | JavaLayerException ignored) {
			}
		} else if (mp.isPaused()) { // 재생 중단 됐을 때 다시 그 부분부터 시작
			mp.resume();
			setStatus("Playing");
			lastIndex = Index;
		} else { // 평상시(일반적일 때)
			try {
				mp.stop();
				mp.play(filePath, Index);
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

	class SettingFrame extends JFrame {
		private JPanel p1, p2, p3;
		private JSlider acceleration;
		private JButton ok;
		private JLabel gauge;

		SettingFrame(Frame o) {
			setLayout(new BorderLayout());

			p1 = new JPanel(new FlowLayout());
			p1.setSize(30, 40);
			p1.add(new JLabel("Music for Dummies"));
			p1.add(new JLabel("Made By"));
			p1.add(new JLabel("박동연"));
			p1.add(new JLabel("안건희"));
			p1.add(new JLabel("이제혁"));

			p2 = new JPanel();
			acceleration = new JSlider(1, 10, 1);
			acceleration.setValue(5);
			gauge = new JLabel("5");
			acceleration.addChangeListener(e -> gauge.setText(Integer.toString(acceleration.getValue())));

			p2.add(new JLabel("Acceleration of moving Forward/Backward"));
			p2.add(acceleration);
			p2.add(gauge);

			p3 = new JPanel(new FlowLayout());

			ok = new JButton("OK");

			ok.addActionListener(e -> {
				FBTimes = acceleration.getValue();
				forButton.setText("Forward " + FBTimes + "s");
				backButton.setText("Backward " + FBTimes + "s");
				dispose();
			});

			p3.add(ok);
			add(p1, BorderLayout.NORTH);
			add(p2, BorderLayout.CENTER);
			add(p3, BorderLayout.SOUTH);

			setSize(250, 150);
			setResizable(false);
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setTitle("Settings");
			setLocationRelativeTo(o);
		}
	}
}
