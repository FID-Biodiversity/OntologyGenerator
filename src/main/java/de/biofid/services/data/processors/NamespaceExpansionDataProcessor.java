package de.biofid.services.data.processors;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;


/**
 * Looks into a Triple object and substitutes all (!) namespaces it can find by configured values.
 * Reads its configuration from config/namespaces.json . This has mostly the reason, that the namespaces in the
 * Ontology should be the same as those that are expanded. If this is not true, please open an issue!
 */
public class NamespaceExpansionDataProcessor implements DataProcessor {

    protected static final Logger logger = LogManager.getLogger();

    private static final String NAMESPACE_CONFIGURATION_FILE = "config/namespaces.json";
    private static final String NAMESPACE_SEPARATOR = ":";

    private final HashMap<String, String> namespaces = new HashMap<>();

    @Override
    public boolean postProcessTriple(Triple triple) {
        processTriple(triple);
        return true;
    }

    @Override
    public void setParameters(JSONObject parameters) {
        try {
            readConfigurationFile(NAMESPACE_CONFIGURATION_FILE);
        } catch (FileNotFoundException e) {
            logger.error("The configuration file \"" + NAMESPACE_CONFIGURATION_FILE + "\" does not exist!");
            System.exit(1);
        }
    }

    public void readConfigurationFile(String configFile) throws FileNotFoundException {
        JSONObject configurationData = JsonFileReader.read(configFile);
        for (String key : configurationData.keySet()) {
            namespaces.put(key, configurationData.getString(key));
        }
    }

    /**
     * Searches for a known namespace in the given string and returns it.
     * If no namespace can be found, null is returned.
     */
    public String extractNamespaceFromString(String string) {
        String namespace = string.split(NAMESPACE_SEPARATOR)[0];
        return namespaces.containsKey(namespace) ? namespace : null;
    }

    public void processTriple(Triple triple) {
        triple.subject = expandNamespaceForString(triple.subject);
        triple.predicate = expandNamespaceForString(triple.predicate);

        if (triple.object instanceof String) {
            triple.object = expandNamespaceForString(triple.object.toString());
        }
    }

    private String expandNamespaceForString(String string) {
        String namespace = extractNamespaceFromString(string);
        if (namespace != null) {
            return string.replace(namespace + NAMESPACE_SEPARATOR, namespaces.get(namespace));
        } else {
            return string;
        }
    }
}
