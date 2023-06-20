package com.bachelors.speecher.service.asterisk;

import com.bachelors.speecher.service.speech.SpeechCoincidenceService;
import com.bachelors.speecher.service.speech.SpeechDetectionService;
import com.bachelors.speecher.service.speech.SpeechVoiceReceiverService;
import com.bachelors.speecher.sox.Sox;
import com.bachelors.speecher.sox.WrongParametersException;
import com.bachelors.speecher.util.Logger;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.linguist.dictionary.Pronunciation;
import edu.cmu.sphinx.linguist.dictionary.Word;
import edu.cmu.sphinx.result.WordResult;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Default Async AGI script implementation. See mapping in
 * AsyncAgiMappingStrategy
 *
 * @author V.Khomyn
 */
public class AsyncAgiScript extends BaseAgiScript {

    private final SpeechVoiceReceiverService voiceReceiverService;
    private final SpeechDetectionService silenceDetectionService;
    private final SpeechCoincidenceService speechCoincidenceService;
    private final StreamSpeechRecognizer recognizer;

    public AsyncAgiScript(
            SpeechVoiceReceiverService voiceReceiverService,
            SpeechDetectionService silenceDetectionService,
            SpeechCoincidenceService speechCoincidenceService,
            StreamSpeechRecognizer recognizer) {
        this.voiceReceiverService = voiceReceiverService;
        this.silenceDetectionService = silenceDetectionService;
        this.speechCoincidenceService = speechCoincidenceService;
        this.recognizer = recognizer;
    }

    @Override
    public void service(AgiRequest request, final AgiChannel channel) {
        Logger.log("Received new incoming call from channel = ".concat(channel.getUniqueId()));
        try {
            answer();
            while (voiceReceiverService.isAsteriskConnectionState()) {
                /* Detect new silence. Change raw extension to wav */
                boolean detect = silenceDetectionService
                        .detectSilence(voiceReceiverService.getVoiceBuffer().toByteArray());
                String uuid = UUID.randomUUID().toString();
                if (detect) {
                    try (FileOutputStream outputStream = new FileOutputStream(uuid.concat(".raw"))) {
                        outputStream.write(voiceReceiverService.getVoiceBuffer().toByteArray());
                    }
                    Sox sox = new Sox("/usr/local/bin/sox");

                    sox.argument("-w")
                            .argument("-s")
                            .argument("-r 8000")
                            .argument("-c 1")
                            .inputFile(uuid.concat(".raw"))
                            .outputFile(uuid.concat(".wav"))
                            .execute();

                    /* Start sphinx recognition on wav file */
                    InputStream stream = null;
                    try {
                        stream = new FileInputStream(new File(uuid.concat(".wav")));
                    } finally {
                        new File(uuid.concat(".raw")).delete();
                    }
                    recognizer.startRecognition(stream);
                    SpeechResult result;
                    List<String> sentence = null;
                    List<String> hypothesis = null;
                    while ((result = recognizer.getResult()) != null) {
                        Logger.log("Sphinx recognition hypothesis: ".concat(result.getHypothesis()));
                        try {
                            Logger.log("Sphinx recognition words: ".concat(result.getWords().toString()));
                            sentence = result.getWords().stream()
                                    .map(WordResult::getWord)
                                    .map(Word::getSpelling)
                                    .filter(word -> !word.equals("<sil>"))
                                    .collect(Collectors.toList());
                            hypothesis = Arrays.stream(result.getHypothesis().split(" ")).toList();
                        } catch (NullPointerException ignored) {
                        }
                    }
                    recognizer.stopRecognition();
                    voiceReceiverService.setVoiceBuffer();
                    /* Start searching word samples to determine file name that is needed to playback user */
                    if (sentence != null && sentence.size() > 0) {
                        Logger.log("Sphinx sentence: ".concat(sentence.toString()));
                        String determined = speechCoincidenceService.determineAudio(sentence, hypothesis);
                        if (determined != null) {
                            /* Stream audio file to client with answer to the speech */
                            Logger.log(determined);
                            streamFile(determined);
                        }
                    }
                } else {
                    silenceDetectionService.detectSpeech(voiceReceiverService.getVoiceBuffer().toByteArray());
                }

                channel.getChannelStatus();
                Thread.sleep(200);
            }
        } catch (AgiException e) {
            Logger.log("AgiException: ".concat(e.getMessage()));
        } catch (InterruptedException e) {
            Logger.log("InterruptedException: ".concat(e.getMessage()));
        } catch (WrongParametersException e) {
            Logger.log("WrongParametersException: ".concat(e.getMessage()));
        } catch (IOException e) {
            Logger.log("IOException: ".concat(e.getMessage()));
        }
    }
}