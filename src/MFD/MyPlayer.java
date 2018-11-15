package MFD;

import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MyPlayer {
    private static final int BUFFER_SIZE = 100000;

    private Decoder decoder;
    private AudioDevice out;
    private ArrayList<Sample> samples;
    private int size, CurrentRecord;
    private boolean isPlay;

    MyPlayer(String path) {
        CurrentRecord = 0;
        Open(path);
    }

    private boolean IsInvalid() {
        return (decoder == null || out == null || samples == null || !out.isOpen());
    }

    private boolean GetSamples(String path) {
        if(IsInvalid())
            return false;
        try {
            Header header;
            SampleBuffer sampleBuffer;
            FileInputStream in = new FileInputStream(path);
            Bitstream bitstream = new Bitstream(in);
            if((header = bitstream.readFrame()) == null)
                return false;
            while(size < BUFFER_SIZE && header != null) {
                sampleBuffer = (SampleBuffer)decoder.decodeFrame(header, bitstream);
                samples.add(new Sample(sampleBuffer.getBuffer(), sampleBuffer.getBufferLength()));
                size++;
                bitstream.closeFrame();
                header = bitstream.readFrame();
            }

        } catch (FileNotFoundException | BitstreamException | DecoderException e) {
            return false;
        }
        return true;
    }

    private boolean Open(String path) {
        Close();
        try {
            decoder = new Decoder();
            out = FactoryRegistry.systemRegistry().createAudioDevice();
            samples = new ArrayList<>(BUFFER_SIZE);
            size = 0;
            out.open(decoder);
            GetSamples(path);
        } catch (JavaLayerException e) {
            decoder = null;
            out = null;
            samples = null;
            return false;
        }

        return true;
    }

    void Play() {
        if(IsInvalid())
            return;
        isPlay = true;
        try {
            for (int i = CurrentRecord; i < size; ++i) {
                CurrentRecord = i;
                if (!isPlay) break;
                out.write(samples.get(i).getBuffer(), 0, samples.get(i).getSize());
            }
            if (isPlay) out.flush();
        } catch (JavaLayerException e) {
//            e.printStackTrace();
        }
    }

    public void stop() {
        isPlay = true;
    }

    private void Close() {
        if((out != null) && !out.isOpen())
            out.close();
        size = 0;
        samples = null;
        out = null;
        decoder = null;
    }

    static class Sample {
        private short[] buffer;
        private int size;

        Sample(short[] buf, int s) {
            buffer = buf.clone();
            size = s;
        }

        short[] getBuffer() {
            return buffer;
        }

        int getSize() {
            return size;
        }
    }
}