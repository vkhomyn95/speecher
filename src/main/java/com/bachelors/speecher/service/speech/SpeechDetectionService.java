package com.bachelors.speecher.service.speech;

import com.bachelors.speecher.util.Logger;
import org.springframework.stereotype.Service;

@Service
public class SpeechDetectionService {
    private static final int SAMPLE_RATE = 8000;
    private static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = true;
    private static final int BYTES_PER_SECOND = SAMPLE_RATE * 8 * CHANNELS / 8;
    private static final int SILENCE_THRESHOLD = 1800;
    private static final int SPEECH_THRESHOLD = 4500;
    private static final int SILENCE_DURATION_SECONDS = 3;
    private boolean IS_SILENCE_DETECTED = false;
    private boolean IS_SPEECH_DETECTED = false;

    public boolean detectSilence(byte[] buffer) {
        int numBytesPerFrame = SAMPLE_SIZE_IN_BITS / 8 * CHANNELS;
        int numFramesPerSecond = SAMPLE_RATE / CHANNELS;
        int numFramesPerDuration = numFramesPerSecond * SILENCE_DURATION_SECONDS;
        int numFramesOfSilence = 0;
        boolean silenceDetected = false;
        for (int i = 0; i < buffer.length; i += numBytesPerFrame) {
            int amplitude = getAmplitude(buffer, i);
            if (amplitude < SILENCE_THRESHOLD) {
                numFramesOfSilence++;
                if (numFramesOfSilence >= numFramesPerDuration) {
                    silenceDetected = true;
                    break;
                }
            } else {
                numFramesOfSilence = 0;
            }
        }
        if (IS_SILENCE_DETECTED && !IS_SPEECH_DETECTED) {
            if (silenceDetected) {
                System.out.println("====================");
                Logger.log("Silence of " + SILENCE_DURATION_SECONDS + " seconds detected.");
                System.out.println("====================");
                IS_SILENCE_DETECTED = true;
                IS_SPEECH_DETECTED = false;
                return true;
            } else {
                IS_SILENCE_DETECTED = false;
                return false;
            }
        }
        return false;
    }

    public boolean detectSpeech(byte[] buffer) {
        int numBytesPerFrame = SAMPLE_SIZE_IN_BITS / 8 * CHANNELS;
        int numFramesPerSecond = SAMPLE_RATE / CHANNELS;
        int numFramesPerDuration = numFramesPerSecond * 2;
        int numFramesOfSilence = 0;
        boolean speechDetected = false;
        for (int i = 0; i < buffer.length; i += numBytesPerFrame) {
            int amplitude = getAmplitude(buffer, i);
            if (amplitude > SPEECH_THRESHOLD) {
                numFramesOfSilence++;
                if (numFramesOfSilence >= numFramesPerDuration) {
                    speechDetected = true;
                    break;
                }
            } else {
                numFramesOfSilence = 0;
            }
        }
        if (speechDetected) {
            System.out.println("====================");
            Logger.log("Speech of " + 2 + " seconds detected.");
            System.out.println("====================");
            IS_SILENCE_DETECTED = false;
            IS_SPEECH_DETECTED = true;
            return true;
        } else {
            IS_SILENCE_DETECTED = true;
            IS_SPEECH_DETECTED = false;
            return false;
        }
    }

    private static int getAmplitude(byte[] audioData, int offset) {
        int amplitude = 0;
        if (SAMPLE_SIZE_IN_BITS == 16) {
            amplitude = (short) (((audioData[offset + 1] & 0xff) << 8) | (audioData[offset] & 0xff));
        }
        return Math.abs(amplitude);
    }
}