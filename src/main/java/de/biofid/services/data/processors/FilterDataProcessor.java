package de.biofid.services.data.processors;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Filter the Triple objects according to the given configuration.
 * There are currently two possible key words you may give:
 *      - "unwantedPredicates"
 *      - "desiredPredicates"
 *  Each keyword should be followed by a list of terms. If a term is contained in both lists, it will remain (i.e.
 *  the "desiredPredicates" has precedence). If the term does not appear in any list, it will be filtered out (i.e
 *  it behaves like it would have been included in the "unwantedPredicates" list).
 */
public class FilterDataProcessor implements DataProcessor {

    protected static final Logger logger = LogManager.getLogger();

    private static final String CONFIGURATION_FILE_KEY = "configurationFile";
    private static final String UNWANTED_PREDICATES_KEY = "unwantedPredicates";
    private static final String DESIRED_PREDICATES_KEY = "desiredPredicates";

    private String configurationFile = null;
    private Set<String> unwantedPredicates = new HashSet<>();
    private Set<String> desiredPredicates = new HashSet<>();
    private boolean configurationIsLoaded = false;

    @Override
    public boolean postProcessTriple(Triple triple) {
        if (!configurationIsLoaded) {
            loadConfigurationDataFromFile(configurationFile);
        }

        return shallTripleSurvive(triple);
    }

    public boolean shallTripleSurvive(Triple triple) {
        String predicate = triple.predicate;

        return (desiredPredicates.isEmpty() && !unwantedPredicates.contains(predicate))
                || desiredPredicates.contains(predicate);
    }

    @Override
    public void setParameters(JSONObject parameters) {
        configurationFile = parameters.optString(CONFIGURATION_FILE_KEY);
    }

    public void loadConfigurationDataFromFile(String configurationFile) {
        if (configurationFile == null) {
            logger.info("No configuration file set for FilterDataProcessor! Returning unfiltered data!");
            configurationIsLoaded = true;
            return;
        }

        JSONObject configurationData = null;
        try {
            configurationData = JsonFileReader.read(configurationFile);
        } catch (FileNotFoundException ex) {
            logger.error("Could not load configuration file \"" + configurationFile + "\"!" +
                    "\nError Message: " + ex.getMessage());
            System.exit(1);
        }

        setDesiredPredicates(loadDesiredPredicates(configurationData));
        setUnwantedPredicates(loadUnwantedPredicates(configurationData));

        configurationIsLoaded = true;
    }

    public void setDesiredPredicates(Set<String> desiredPredicates) {
        this.desiredPredicates = desiredPredicates;
    }

    public void setUnwantedPredicates(Set<String> unwantedPredicates) {
        this.unwantedPredicates = unwantedPredicates;
    }

    private Set<String> loadDesiredPredicates(JSONObject configurationData) {
        return loadFromConfiguration(configurationData, DESIRED_PREDICATES_KEY);
    }

    private Set<String> loadUnwantedPredicates(JSONObject configurationData) {
        return loadFromConfiguration(configurationData, UNWANTED_PREDICATES_KEY);
    }

    private Set<String> loadFromConfiguration(JSONObject configuration, String key) {
        return StreamSupport
                .stream(getJSONArrayForKeyOrEmptyArray(configuration, key).spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    private JSONArray getJSONArrayForKeyOrEmptyArray(JSONObject jsonObject, String key) {
        return jsonObject.has(key) ? jsonObject.getJSONArray(key) : new JSONArray();
    }
}
