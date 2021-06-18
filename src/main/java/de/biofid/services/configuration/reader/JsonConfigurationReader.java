package de.biofid.services.configuration.reader;

import de.biofid.services.configuration.reader.ConfigurationReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JsonConfigurationReader extends ConfigurationReader {

    @Override
    public JSONObject readConfigurationDataFrom(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        JSONTokener jsonTokener = new JSONTokener(inputStream);
        return new JSONObject(jsonTokener);
    }
}
