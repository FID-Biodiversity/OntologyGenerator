package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.ConfigurationReader;
import de.biofid.services.exceptions.KeyException;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.factories.ConfigurationFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {

    // Configuration keys should all be lower case
    public final static String KEY_ONTOLOGY_LISTS = "ontologies";
    public final static String KEY_DATA_SERVICE_CONFIGURATIONS = "services";
    public final static String KEY_SERIALIZER = "serializer";

    // Ontology Configuration
    public final static String KEY_DATA_SOURCE_CLASS_NAME = "dataSourceClass";
    public final static String KEY_DATA_GENERATOR_CLASS_NAME = "dataGeneratorClass";
    public final static String KEY_DATA_PROCESSOR_CLASS_NAME = "dataProcessorClass";
    public final static String KEY_DATA_SERVICE_PARAMETERS = "parameters";

    protected static final Logger logger = LogManager.getLogger();

    protected final ConfigurationReader reader;
    protected JSONObject configuration ;

    public Configuration(ConfigurationReader configurationReader) {
        if (configurationReader == null) {
            throw new NullPointerException("The given configurationReader is null!");
        }

        reader = configurationReader;
        configuration = new JSONObject();
    }

    public void loadFromFile(String filePath) {
        String absoluteFilePath = stringPathToAbsolutePathString(filePath);

        logger.info("Reading configuration \"" + absoluteFilePath + "\"...");

        try {
            configuration = reader.readConfigurationDataFrom(filePath);
        } catch (FileNotFoundException e) {
            logger.warn("The configuration file " + absoluteFilePath + " does not exist!");
        }
    }

    public List<OntologyConfiguration> getOntologyConfigurations() {
        List<OntologyConfiguration> configurations = new ArrayList<>();
        JSONObject ontologyConfigurations = getOntologyDataInConfiguration();

        try{
            Iterator<String> ontologyNameIterator = ontologyConfigurations.keys();
            while (ontologyNameIterator.hasNext()) {
                String ontologyName = ontologyNameIterator.next();
                OntologyConfiguration ontologyConfiguration =
                        ConfigurationFactory.createOntologyConfiguration(ontologyName, this);
                configurations.add(ontologyConfiguration);
            }
        } catch (ValueException | KeyException ex) {
            logger.error(ex.getMessage());
            System.exit(1);
        }

        return configurations;
    }

    public JSONObject getConfigurationForOntologyName(String ontologyName) throws KeyException {
        JSONObject ontologyConfigurations = getOntologyDataInConfiguration();
        if (ontologyConfigurations.has(ontologyName)) {
            return ontologyConfigurations.getJSONObject(ontologyName);
        } else {
            throw new KeyException("The given ontology Name \"" + ontologyName + "\" is not given in the configuration!");
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

    public JSONObject toJSONObject() {
        return this.configuration;
    }

    private JSONObject getOntologyDataInConfiguration() {
        JSONObject ontologyConfigurations = null;
        try {
            ontologyConfigurations = configuration.getJSONObject(Configuration.KEY_ONTOLOGY_LISTS);
        } catch (JSONException ex) {
            logger.error("No ontology configurations are given. Use the key \"" + KEY_ONTOLOGY_LISTS + "\" to define them!");
            System.exit(1);
        }

        return ontologyConfigurations;
    }

    private String stringPathToAbsolutePathString(String pathString) {
        Path path = Paths.get(pathString);
        return path.toAbsolutePath().toString();
    }
}
