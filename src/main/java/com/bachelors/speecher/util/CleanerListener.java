package com.bachelors.speecher.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CleanerListener {

    private static final List<String> SUFFIX_LIST = Arrays.asList(".wav", ".raw");

    public CleanerListener() {
        File[] directory = new File("/home/lenovo/IdeaProjects/speecher").listFiles();
        if (directory != null) {
            for (File file: directory) {
                if (file.getName().contains(".")) {
                    String extension = file.getName().substring(file.getName().lastIndexOf("."));
                    if (SUFFIX_LIST.stream().anyMatch(name -> name.equals(extension))) {
                        file.delete();
                    }
                }
            }
        }
    }
}
