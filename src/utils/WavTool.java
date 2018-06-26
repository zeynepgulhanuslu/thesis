package utils;

import zemberek.core.logging.Log;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class WavTool {

    public static float getDurationInSeconds(File wavFile) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        return (frames + 0.0f) / format.getFrameRate();
    }

    public static void toMonoWavWithSox(Path soxPath, File input, File output, int samplingRate) throws Exception {
        if (!soxPath.toFile().exists()) {
            throw new IllegalStateException("Cannot find sox tool in " + soxPath);
        }
        ProcessBuilder pb = new ProcessBuilder(
                soxPath.toString(),
                input.getAbsolutePath(),
                "-c", "1",
                "-r", String.valueOf(samplingRate),
                output.getAbsolutePath()
        );
        runProcess(pb);
    }


    private static void runProcess(ProcessBuilder pb) throws IOException, InterruptedException {
        Log.info(String.join(" ", pb.command()));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String s;
            while ((s = reader.readLine()) != null) {
                Log.info(s);
            }
        }
        p.waitFor();
    }
}
