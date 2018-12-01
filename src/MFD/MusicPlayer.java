package MFD;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class MusicPlayer {
	private InputStream is;

	private Player player;
	private boolean repeat;
	private boolean paused;
	private long pauseLocation;
	private long totalSongLength;
	private String musicFilePath;

	// if nowPlayingIndex is -1, that means not playing now.
	private int nowPlayingIndex;

	private Thread thread;

	/**
	 * this method is used to play a song, if u want to
	 * repeat this song,  set Repeat to true before
	 * call this method
	 * NOTE: the files to play must be in resources folder
	 */
	void play(String musicFilePath, int idx) throws IOException, JavaLayerException {
		this.musicFilePath = musicFilePath;
		nowPlayingIndex = idx;
		is = new FileInputStream(new File(musicFilePath));
		player = new Player(is);

		thread = new Thread(this::run);
		thread.start();
	}

	/**
	 * use this method to remuse current paused song
	 */
	void resume() {
		paused = false;

		try {
			is = new FileInputStream(new File(musicFilePath));
			is.skip(totalSongLength - pauseLocation);
			player = new Player(is);
		} catch (JavaLayerException | IOException e) {
			e.printStackTrace();
		}

		thread = new Thread(this::run2);
		thread.start();
	}


	/**
	 * use this method to stop current song that is being
	 * played
	 */
	void stop() {
		paused = false;
		nowPlayingIndex = -1;
		try {
			is.close();
		} catch (IOException ignored) {
		}

		if (player != null) {
			player.close();

			thread.interrupt();
			totalSongLength = 0;
			pauseLocation = 0;
			musicFilePath = "";
		}
	}


	/**
	 * use this method to pause current played song
	 */
	void pause() {
		paused = true;
		if (player != null) {
			try {
				pauseLocation = is.available();
				player.close();
			} catch (IOException ignored) {
			}
		}
	}

	// 1초 단위
	void forward(int times) {
		Move(22000 * times, 1);
	}

	void back(int times) {
		Move(-22000 * times, 2);
	}

	private void Move(long l, int Type) {
		pause();
		paused = false;

		long a = totalSongLength - pauseLocation + l;

		if ((Type == 1 && a > totalSongLength) || (Type == 2 && a < 0)) a -= l;

		try {
			is = new FileInputStream(new File(musicFilePath));
			is.skip(a);
			player = new Player(is);
		} catch (JavaLayerException | IOException ignored) {
		}
		thread = new Thread(this::run2);
		thread.start();
	}

	boolean isPaused() {
		return paused;
	}

	int getNowPlayingIndex() {
		return this.nowPlayingIndex;
	}

	private void run() {
		try {
			while (!player.isComplete() && !repeat) {
				totalSongLength = is.available();
				player.play();
			}
		} catch (JavaLayerException | IOException ignored) {
		}
	}

	private void run2() {
		try {
			player.play();
		} catch (JavaLayerException ignored) {
		}
	}
}

