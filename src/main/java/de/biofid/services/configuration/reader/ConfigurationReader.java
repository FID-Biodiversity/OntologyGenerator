package de.biofid.services.configuration.reader;

import org.json.JSONObject;

import java.io.FileNotFoundException;

public abstract class ConfigurationReader {
    abstract public JSONObject readConfigurationDataFrom(String filePath) throws FileNotFoundException;
}
