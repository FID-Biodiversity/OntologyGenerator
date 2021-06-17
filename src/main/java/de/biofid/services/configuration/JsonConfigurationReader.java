package de.biofid.services.configuration;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JsonConfigurationReader extends ConfigurationReader {

    @Override
    public JSONObject readConfigurationFile(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        JSONTokener jsonTokener = new JSONTokener(inputStream);
        return new JSONObject(jsonTokener);
    }
}
