package com.bachelors.speecher.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIdsGeneratorFactory {
    public static void main(String[] args) throws IOException {
        File[] directory = new File("/home/lenovo/Documents/acousticUA/audioUA").listFiles();
        String outputFilename = "an5_train.fileids";
        FileWriter writer = new FileWriter(outputFilename);
        if (directory != null) {
            for (int i = 1; i <= directory.length; i++) {
                if (i < 10)
                    writer.write("arctic-0" + i + "\n");
                else
                    writer.write("arctic-" + i + "\n");
            }
            writer.close();
        }
    }
}
