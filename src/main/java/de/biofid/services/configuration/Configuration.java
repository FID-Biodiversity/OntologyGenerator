package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.ConfigurationReader;
import de.biofid.services.exceptions.KeyException;
import de.biofid.services.exceptions.ValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class Configuration {

    public static final String BIOFID_SERIALIZER_PACKAGE_STRING = "de.biofid.services.serializer";

    // Configuration keys should all be lower case
    public final static String KEY_ONTOLOGY_LISTS = "ontologies";
    public final static String KEY_DATA_SERVICE_CONFIGURATIONS = "services";
    public final static String KEY_SERIALIZER = "serializer";

    // Ontology Configuration
    public final static String KEY_DATA_SOURCE_CLASS_NAME = "dataSourceClass";
    public final static String KEY_DATA_GENERATOR_CLASS_NAME = "dataGeneratorClass";
    public final static String KEY_DATA_PROCESSOR_CLASS_NAME = "dataProcessorClass";

    protected static final Logger logger = LogManager.getLogger();

    private final ConfigurationReader reader;
    private JSONObject configuration ;

    public Configuration(ConfigurationReader configurationReader) {
        if (configurationReader == null) {
            throw new NullPointerException("The given configurationReader is null!");
        }

        reader = configurationReader;
        configuration = new JSONObject();
    }

    public void loadFromFile(String filePath) {
        String absoluteFilePath = stringPathToAbsolutePathString(filePath);

        try {
            configuration = reader.readConfigurationDataFrom(filePath);
        } catch (FileNotFoundException e) {
            logger.warn("The configuration file " + absoluteFilePath + " does not exist!");
        }
    }

    // TODO: Iterate OntologyConfiguration

    public JSONObject getConfigurationForOntologyName(String ontologyName) throws KeyException {
        for (Object configObject : getOntologyDataInConfiguration()) {
            JSONObject configuration = (JSONObject) configObject;
            for (Iterator<String> it = configuration.keys(); it.hasNext(); ) {
                String name = it.next();
                if (name.equals(ontologyName)) {
                    return configuration.getJSONObject(name);
                }
            }
        }

        throw new KeyException("The given ontology Name \"" + ontologyName + "\" is not given in the configuration!");
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

    public JSONObject toJSONObject() {
        return this.configuration;
    }

    private JSONArray getOntologyDataInConfiguration() {
        return configuration.getJSONArray(Configuration.KEY_ONTOLOGY_LISTS);
    }

    private String stringPathToAbsolutePathString(String pathString) {
        Path path = Paths.get(pathString);
        return path.toAbsolutePath().toString();
    }
}
