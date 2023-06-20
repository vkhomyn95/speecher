package com.bachelors.speecher.configuration;

import com.bachelors.speecher.service.asterisk.*;
import com.bachelors.speecher.service.speech.SpeechCoincidenceService;
import com.bachelors.speecher.service.speech.SpeechDetectionService;
import com.bachelors.speecher.service.asterisk.AsteriskManagerConnection;
import com.bachelors.speecher.service.speech.SpeechVoiceReceiverService;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsteriskConfiguration {

    @Value("${asterisk.host}")
    private String asteriskHost;
    @Value("${asterisk.user}")
    private String asteriskUser;
    @Value("${asterisk.password}")
    private String asteriskPassword;
    @Value("${asterisk.port}")
    private String asteriskPort;
    @Value("${asterisk.responce.timeout}")
    private Integer asteriskResponseTimeout;

    @Bean
    public ManagerConnection speecherMangerConnection() {
        return new AsteriskManagerConnection(
                asteriskHost,
                Integer.parseInt(asteriskPort),
                asteriskUser,
                asteriskPassword,
                asteriskResponseTimeout
        );
    }

    @Bean
    public ManagerEventListener managerEventListener() {
        return new AsteriskManagerEventListenerImpl();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public AsteriskAmiBean amiMBean() {
        return new AsteriskAmiBean(null, null);
    }

    @Bean
    @Qualifier("asyncAgiMappingStrategy")
    public AsyncAgiMappingStrategy asyncAgiMappingStrategy(
            SpeechVoiceReceiverService voiceReceiverService,
            SpeechDetectionService silenceDetectionService,
            SpeechCoincidenceService speechCoincidenceService,
            StreamSpeechRecognizer recognizer
    ) {
        return new AsyncAgiMappingStrategy(
                voiceReceiverService, silenceDetectionService, speechCoincidenceService, recognizer
        );
    }

    @Bean
    public AsteriskAsyncAgiBean asteriskAsyncAgiBean(
            @Qualifier("asyncAgiMappingStrategy") AsyncAgiMappingStrategy mappingStrategy,
            AsteriskAmiBean acdManager
    ) {
        AsteriskAsyncAgiBean bean = new AsteriskAsyncAgiBean();
        bean.setAcdManager(acdManager);
        bean.setMappingStrategy(mappingStrategy);
        return bean;
    }
}