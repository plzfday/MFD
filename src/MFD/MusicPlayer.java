package MFD;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {
    private InputStream is;

    private Player player;
    private boolean repeat;
    private boolean paused;
    private long pauseLocation;
    private long totalSongLength;
    private String musicFilePath;

    /**
     * this method is used to play a song, if u want to
     * repeat this song,  set Repeat to true before
     * call this method
     * NOTE: the files to play must be in resources folder
     */
    void play(String musicFilePath) throws IOException, JavaLayerException {
        this.musicFilePath = musicFilePath;
        is = new FileInputStream(new File(musicFilePath));
        player = new Player(is);

        new Thread(() -> {
            try {
                while (!player.isComplete() && !repeat && !Thread.currentThread().isInterrupted()) {
                    totalSongLength = is.available();
                    player.play();
                }
            } catch (JavaLayerException | IOException ignored) {
            }
        }).start();
    }

    /**
     * use this method to remuse current paused song
     */
    void resume() throws IOException, JavaLayerException {
        paused = false;

        is = new FileInputStream(new File(musicFilePath));

        is.skip(totalSongLength - pauseLocation);

        player = new Player(is);

        new Thread(() -> {
            try {
                player.play();
            } catch (JavaLayerException ignored) {
            }
        }).start();
    }


    /**
     * use this method to stop current song that is being
     * played
     */
    void stop() {
        paused = false;

        if (player != null) {
            player.close();

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

    /**
     * @return true if the song i will start once is done,
     * false if not
     */
    public boolean isRepeat() {
        return repeat;
    }


    /**
     * set if the song will start once is done
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

