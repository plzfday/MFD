package MFD;

import javazoom.jl.player.*;
import javazoom.jl.decoder.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileInputStream;

public class MyPlayer {
    public static final int BUFFER_SIZE = 10000;

    private Decoder decoder;
    private AudioDevice out;
    private ArrayList<Sample> samples;
    private int size;

    public MyPlayer(String path) {
        Open(path);
    }

    public boolean IsInvalid() {
        return (decoder == null || out == null || samples == null || !out.isOpen());
    }

    protected boolean GetSamples(String path) {
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

        } catch (FileNotFoundException e) {
            return false;
        } catch (BitstreamException e) {
            return false;
        } catch (DecoderException e) {
            return false;
        }
        return true;
    }

    public boolean Open(String path) {
        Close();
        try {
            decoder = new Decoder();
            out = FactoryRegistry.systemRegistry().createAudioDevice();
            samples = new ArrayList<Sample>(BUFFER_SIZE);
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

    public void Play() {
        if(IsInvalid())
            return;
        try {
            for(int i = 0; i < size; ++i) {
                out.write(samples.get(i).getBuffer(), 0, samples.get(i).getSize());
            }
            out.flush();
        } catch (JavaLayerException e) {
//            e.printStackTrace();
        }
    }

    public void Close() {
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

        public Sample(short[] buf, int s) {
            buffer = buf.clone();
            size = s;
        }

        public short[] getBuffer() {
            return buffer;
        }

        public int getSize() {
            return size;
        }
    }
}