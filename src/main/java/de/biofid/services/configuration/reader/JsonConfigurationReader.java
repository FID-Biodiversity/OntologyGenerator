package de.biofid.services.configuration.reader;

import de.biofid.services.deserialization.JsonFileReader;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public class JsonConfigurationReader extends ConfigurationReader {
    @Override
    public JSONObject readConfigurationDataFrom(String filePath) throws FileNotFoundException {
        return JsonFileReader.read(filePath);
    }
}
