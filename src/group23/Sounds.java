package group23;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds implements Runnable {

    /*File yourFile = new File("res/sounds/despacito.wav");
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;*/

    private static boolean stopThread = false;
    private static boolean stopThreadState = false;

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    public Sounds(String url){
        soundFile = new File(url);

    }

    public boolean stopAudio() {
        //System.out.println("stopAudio");
        stopThread = stopThreadState;
        return stopThread;
    }

    public void run() {

            try {
                audioStream = AudioSystem.getAudioInputStream(soundFile);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            audioFormat = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            try {
                sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open(audioFormat);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            sourceLine.start();

            int nBytesRead = 0;
            byte[] abData = new byte[BUFFER_SIZE];
            while (nBytesRead != -1) {
                try {
                    nBytesRead = audioStream.read(abData, 0, abData.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }

            //Always drain & close
            sourceLine.drain();
            sourceLine.close();

        stopThread = stopAudio();

        if (stopThread == false) {
            run();
            stopThread = true;
        }

    }

    public void changeState() {
        stopThreadState = true;
    }
}
