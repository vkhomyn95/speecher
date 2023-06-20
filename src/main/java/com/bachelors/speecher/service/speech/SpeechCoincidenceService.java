package com.bachelors.speecher.service.speech;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class SpeechCoincidenceService {
    private static final List<String> audio1 = List.of("добрий", "день");
    private static final List<String> audio2 = List.of("привіт");
    private static final List<String> audio3 = List.of("мене", "звати", "Степан", "Володимир");
    private static final List<String> audio4 = List.of("який", "баланс", "картки");
    private static final List<String> audio5 = List.of("як", "поповнити", "картку");
    private static final List<String> audio6 = List.of("як", "погасити", "кредит");
    private static final List<String> audio7 = List.of("як", "оформити", "кредит");
    private static final List<String> audio8 = List.of("як", "оформити", "лізинг");
    private static final List<String> audio9 = List.of("як", "заблокувати", "картку");
    private static final List<String> audio10 = List.of("який", "графік", "роботи", "відділення");
    private static final List<String> audio11 = List.of("яка", "сума", "на", "балансі", "картки");
    private static final List<String> audio12 = List.of("яка", "сума", "мінімального", "платежу");
    private static final List<String> audio13 = List.of("яка", "сума", "заборгованості");
    private static final List<String> audio14 = List.of("як", "поповнити", "рахунок");
    private static final List<String> audio15 = List.of("який", "відсоток");
    private static final List<String> audio16 = List.of("втратив", "картку");
    private static final List<String> audio17 = List.of("загубив", "картку");
    private static final List<String> audio18 = List.of("викрадено", "картку");
    private static final List<String> audio19 = List.of("до", "побачення");


    public String determineAudio(List<String> sentence, List<String> hypothesis) {
        try {
            Field[] declaredFields = SpeechCoincidenceService.class.getDeclaredFields();
            for (Field field : declaredFields) {
                List<String> audio = (List<String>) field.get(this);
                if (audio.containsAll(sentence)) {
                    return field.getName();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
