package com.bachelors.speecher.configuration;


import com.bachelors.speecher.util.CleanerListener;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SphinxConfiguration {

    @Bean
    public edu.cmu.sphinx.api.Configuration sphinxConfig() {
        edu.cmu.sphinx.api.Configuration configuration = new edu.cmu.sphinx.api.Configuration();
        configuration.setAcousticModelPath("/home/lenovo/Documents/acousticUA/ua5/model_parameters/ua5.cd_cont_8");
        configuration.setDictionaryPath("/home/lenovo/Documents/acousticUA/ua5/etc/ua5.dic");
        configuration.setLanguageModelPath("/home/lenovo/Documents/acousticUA/ua5/etc/ua5.lm.bin");
        configuration.setSampleRate(8000);

        return configuration;
    }

    @Bean
    public StreamSpeechRecognizer speechRecognizer(edu.cmu.sphinx.api.Configuration config) throws IOException {
        return new StreamSpeechRecognizer(config);
    }

    @Bean
    public CleanerListener cleanerListener() {
        return new CleanerListener();
    }
}