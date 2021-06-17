package de.biofid.services.configuration;

import de.biofid.services.exceptions.ValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    protected static final Logger logger = LogManager.getLogger();

    private final ConfigurationReader reader;
    private JSONObject configuration;

    Configuration(ConfigurationReader configurationReader) {
        if (configurationReader == null) {
            throw new NullPointerException("The given configurationReader is null!");
        }

        reader = configurationReader;
        configuration = new JSONObject();
    }

    public void loadFromFile(String filePath) {
        String absoluteFilePath = stringPathToAbsolutePathString(filePath);

        try {
            configuration = reader.readConfigurationFile(filePath);
        } catch (FileNotFoundException e) {
            logger.warn("The configuration file " + absoluteFilePath + " does not exist!");
        }
    }

    public JSONArray getJsonArray(String key) throws ValueException {
        return (JSONArray) get(key);
    }

    public JSONObject getJsonObject(String key) throws ValueException {
        return (JSONObject) get(key);
    }

    public Object get(String key) throws ValueException {
        try {
            return configuration.get(key);
        } catch (JSONException e) {
            String message = "Could not retrieve key \"" + key + "\" from Configuration!";
            logger.warn(message);
            throw new ValueException(message);
        }
    }

    private String stringPathToAbsolutePathString(String pathString) {
        Path path = Paths.get(pathString);
        return path.toAbsolutePath().toString();
    }
}
