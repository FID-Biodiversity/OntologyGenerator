package de.biofid.services.configuration.reader;

import org.json.JSONObject;

public class StringConfigurationReader extends ConfigurationReader {

    @Override
    public JSONObject readConfigurationDataFrom(String configurationData) {
        return new JSONObject(configurationData);
    }
}
