package com.bachelors.speecher.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class FilesDurationFactory {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        File[] directory = new File("/home/lenovo/Documents/acousticUA/audioUA").listFiles();
        float durationInSeconds = 0;
        if (directory != null) {
            for (File file: directory) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = audioInputStream.getFormat();
                long audioFileLength = file.length();
                int frameSize = format.getFrameSize();
                float frameRate = format.getFrameRate();
                durationInSeconds += (audioFileLength / (frameSize * frameRate));
            }
        }
        System.out.println("Total files wav duration: ".concat(String.valueOf(durationInSeconds)));
    }
}
