package com.bachelors.speecher.sox;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sox {

    private final String soXBinaryPath;

    private List<String> arguments = new ArrayList<String>();

    private boolean outputFileSet = false;

    public Sox(String soxPath) {
        this.soXBinaryPath = soxPath;
    }

    public Sox argument(String ... arguments) {
        Collections.addAll(this.arguments, arguments);
        return this;
    }

    public Sox inputFile(String inputFile) throws WrongParametersException {
        if (outputFileSet) {
            throw new WrongParametersException("The output file has to be later then an input file. Arguments: " + arguments.toString());
        }
        arguments.add(inputFile);
        return this;
    }

    public Sox outputFile(String outputFile) throws WrongParametersException {
        arguments.add(outputFile);
        outputFileSet = true;
        return this;
    }

    public void execute() throws IOException, WrongParametersException {
        File soxBinary = new File(soXBinaryPath);
        if (!soxBinary.exists()) {
            throw new FileNotFoundException("Sox binary is not available under the following path: " + soXBinaryPath);
        }

        if (!outputFileSet) {
            throw new WrongParametersException("The output file argument is missing. Arguments: " + arguments.toString());
        }
        arguments.add(0, soXBinaryPath);
        System.out.println("Sox arguments: {}" + arguments);
        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        processBuilder.redirectErrorStream(true);
        Process process = null;
        IOException errorDuringExecution = null;
        try {
            process = processBuilder.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if (process.exitValue() != 0) {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            else {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }


        } catch (IOException e) {
            errorDuringExecution = e;
            System.out.println("Error while running Sox. {}" + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error while running Sox. {}" + e.getMessage());
        } finally {
            arguments.clear();
            if (process != null) {
                process.destroy();
            }
            if (errorDuringExecution != null) {
                throw errorDuringExecution;
            }
        }
    }
}