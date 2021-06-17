package de.biofid.services.configuration;

import org.json.JSONObject;

import java.io.FileNotFoundException;

public abstract class ConfigurationReader {
    abstract public JSONObject readConfigurationFile(String filePath) throws FileNotFoundException;
}
