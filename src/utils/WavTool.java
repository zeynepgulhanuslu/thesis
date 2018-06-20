package utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class WavTool {

    public static float getDurationInSeconds(File wavFile) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        return (frames + 0.0f) / format.getFrameRate();
    }
}
