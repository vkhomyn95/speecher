package com.bachelors.speecher.service.speech;

import com.bachelors.speecher.sox.Sox;
import com.bachelors.speecher.util.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.*;
import java.util.UUID;


@Component
public class SpeechVoiceReceiverService extends BinaryWebSocketHandler {

    private static ByteArrayOutputStream voiceBuffer;
    private boolean asteriskConnectionState = true;

    public SpeechVoiceReceiverService() {
        voiceBuffer = new ByteArrayOutputStream();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Logger.log("Asterisk broadcaster connected using websocket");
        asteriskConnectionState = true;
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        voiceBuffer.writeBytes(message.getPayload().array());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        asteriskConnectionState = false;
        String uuid = UUID.randomUUID().toString();
        try (FileOutputStream outputStream = new FileOutputStream(uuid.concat(".raw"))) {
            outputStream.write(voiceBuffer.toByteArray());
        }
        Sox sox = new Sox("/usr/local/bin/sox");
        sox.argument("-w")
                .argument("-s")
                .argument("-r 8000")
                .argument("-c 1")
                .inputFile(uuid.concat(".raw"))
                .outputFile(uuid.concat(".wav"))
                .execute();
        Logger.log("Asterisk broadcaster disconnected using websocket");
        session.close(status);
        voiceBuffer.reset();
    }

    public boolean isAsteriskConnectionState() {
        return asteriskConnectionState;
    }

    public ByteArrayOutputStream getVoiceBuffer() {
        return voiceBuffer;
    }

    public void setVoiceBuffer() {
        voiceBuffer = new ByteArrayOutputStream();
    }
}