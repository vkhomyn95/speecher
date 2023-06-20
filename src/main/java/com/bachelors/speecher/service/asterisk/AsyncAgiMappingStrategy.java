package com.bachelors.speecher.service.asterisk;

import com.bachelors.speecher.service.speech.SpeechCoincidenceService;
import com.bachelors.speecher.service.speech.SpeechDetectionService;
import com.bachelors.speecher.service.speech.SpeechVoiceReceiverService;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.MappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapping strategy for Async AGI. In the dialplan looks like
 * (exten=>777,1,AGI(agi:async))
 *
 * @author V.Khomyn
 */
public class AsyncAgiMappingStrategy implements MappingStrategy {

    @Autowired
    private SpeechVoiceReceiverService voiceReceiverService;
    @Autowired
    private SpeechDetectionService silenceDetectionService;
    @Autowired
    private SpeechCoincidenceService speechCoincidenceService;
    @Autowired
    private StreamSpeechRecognizer recognizer;

    public AsyncAgiMappingStrategy(SpeechVoiceReceiverService voiceReceiverService,
                                   SpeechDetectionService silenceDetectionService,
                                   SpeechCoincidenceService speechCoincidenceService,
                                   StreamSpeechRecognizer recognizer) {
        this.voiceReceiverService = voiceReceiverService;
        this.silenceDetectionService = silenceDetectionService;
        this.speechCoincidenceService = speechCoincidenceService;
        this.recognizer = recognizer;
    }

    @Override
    public AgiScript determineScript(AgiRequest agiRequest, AgiChannel agiChannel) {
        return new AsyncAgiScript(voiceReceiverService, silenceDetectionService, speechCoincidenceService, recognizer);
    }
}