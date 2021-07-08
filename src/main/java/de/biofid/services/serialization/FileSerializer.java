package de.biofid.services.serialization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileSerializer extends Serializer {

    protected static final Logger logger = LogManager.getLogger();

    private String outputPath = null;

    @Override
    public void serialize(String outputString) {
        try {
            FileOutputStream fileOutputStream = (FileOutputStream) getOutputStream();
            fileOutputStream.write(outputString.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch (IOException ex) {
            logger.error("Could not write the ontology to the given path \"" + outputPath + "\"!");
        }
    }

    @Override
    public void addSink(String sink) {
        outputPath = sink;
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(outputPath);
        } catch (FileNotFoundException e) {
            logger.error("Could not open file \"" + outputPath + "\"!");
            return null;
        }
    }
}
