package com.bachelors.speecher.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>Транскрибує українські слова у латинські використовуючи фонеми.</p>
 * */
public class UkrainianTranscriberFactory {
    private static Set<String> phonemes = new HashSet();

    public static void main(String[] args) {
        String inputFilename = "words.txt";
        String outputFilename = "output.txt";
        String outputPhonemeFilename = "ua.phone";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
            FileWriter writer = new FileWriter(outputFilename);

            String line;
            while ((line = reader.readLine()) != null) {
                String transcription = transcribe(line);
                writer.write(transcription + "\n");
            }

            FileWriter phonemeWriter = new FileWriter(outputPhonemeFilename);
            phonemeWriter.write("SIL" + "\n");
            for (String phoneme: phonemes) {
                phonemeWriter.write(phoneme + "\n");
            }

            reader.close();
            writer.close();
            phonemeWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String transcribe(String word) {
        StringBuilder sb = new StringBuilder();
        sb.append(word);

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            switch(c) {
                case 'а':
                    sb.append(" a");
                    putPhoneme("a");
                    break;
                case 'б':
                    sb.append(" b");
                    putPhoneme("b");
                    break;
                case 'в':
                    sb.append(" v");
                    putPhoneme("v");
                    break;
                case 'г':
                    sb.append(" h");
                    putPhoneme("h");
                    break;
                case 'ґ':
                    sb.append(" g");
                    putPhoneme("g");
                    break;
                case 'д':
                    sb.append(" d");
                    putPhoneme("d");
                    break;
                case 'е':
                case 'є':
                    sb.append(" e");
                    putPhoneme("e");
                    break;
                case 'ж':
                    sb.append(" zh");
                    putPhoneme("zh");
                    break;
                case 'з':
                    sb.append(" z");
                    putPhoneme("z");
                    break;
                case 'и':
                    sb.append(" y");
                    putPhoneme("y");
                    break;
                case 'і':
                    sb.append(" i");
                    putPhoneme("i");
                    break;
                case 'ї':
                    sb.append(" ji");
                    putPhoneme("ji");
                    break;
                case 'й':
                    sb.append(" j");
                    putPhoneme("j");
                    break;
                case 'к':
                    sb.append(" k");
                    putPhoneme("k");
                    break;
                case 'л':
                    sb.append(" l");
                    putPhoneme("l");
                    break;
                case 'м':
                    sb.append(" m");
                    putPhoneme("m");
                    break;
                case 'н':
                    sb.append(" n");
                    putPhoneme("n");
                    break;
                case 'о':
                    sb.append(" o");
                    putPhoneme("o");
                    break;
                case 'п':
                    sb.append(" p");
                    putPhoneme("p");
                    break;
                case 'р':
                    sb.append(" r");
                    putPhoneme("r");
                    break;
                case 'с':
                    sb.append(" s");
                    putPhoneme("s");
                    break;
                case 'т':
                    sb.append(" t");
                    putPhoneme("t");
                    break;
                case 'у':
                    sb.append(" u");
                    putPhoneme("u");
                    break;
                case 'ф':
                    sb.append(" f");
                    putPhoneme("f");
                    break;
                case 'х':
                    sb.append(" kh");
                    putPhoneme("kh");
                    break;
                case 'ц':
                    sb.append(" ts");
                    putPhoneme("ts");
                    break;
                case 'ч':
                    sb.append(" ch");
                    putPhoneme("ch");
                    break;
                case 'ш':
                    sb.append(" sh");
                    putPhoneme("sh");
                    break;
                case 'щ':
                    sb.append(" shh");
                    putPhoneme("shh");
                    break;
                case 'ь':
                    break;
                case 'ю':
                    sb.append(" ju");
                    putPhoneme("ju");
                    break;
                case 'я':
                    sb.append(" ja");
                    putPhoneme("ja");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        return sb.toString();
    }

    public static void putPhoneme(String word) {
        phonemes.add(word);
    }
}