package de.biofid.services.data.processors;

import de.biofid.services.configuration.reader.ConfigurationReader;
import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * Reads the predicate of each Triple object and maps it to a given configuration.
 */
public class PredicateMappingDataProcessor implements DataProcessor {

    private static final String KEY_MAPPING_CONFIGURATION_FILE = "configurationFile";
    private static final String KEY_MAPPING = "mapping";

    private JSONObject configuration = new JSONObject();

    @Override
    public boolean postProcessTriple(Triple triple) {
        String triplePredicate = triple.predicate;

        JSONObject mapping = configuration.getJSONObject(KEY_MAPPING);
        if (mapping.has(triplePredicate)) {
            triple.predicate = mapping.getString(triplePredicate);
        }

        return true;
    }

    @Override
    public void setConfiguration(JSONObject configuration) {
        if (configuration.has(KEY_MAPPING_CONFIGURATION_FILE)) {
            this.configuration = configuration;
            readMappingConfigurationFile();
        } else {
            this.configuration.put(KEY_MAPPING, configuration);
        }
    }

    private void readMappingConfigurationFile() {
        if (!configuration.has(KEY_MAPPING_CONFIGURATION_FILE)) {
            configuration.put(KEY_MAPPING, new JSONObject());
            return;
        }

        String configurationFilePath = configuration.getString(KEY_MAPPING_CONFIGURATION_FILE);
        ConfigurationReader configurationReader = new JsonConfigurationReader();
        try {
            JSONObject additionalConfigurationData =
                    configurationReader.readConfigurationDataFrom(configurationFilePath);
            mergeDataIntoConfiguration(additionalConfigurationData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void mergeDataIntoConfiguration(JSONObject data) {
        configuration.put(KEY_MAPPING, data);
    }
}
