package de.biofid.services.data.processors;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Filter the Triple objects according to the given configuration.
 * Hence, returns for the postProcessTriple(Triple) false, if the predicate is given in the "unwantedPredicates" list.
 * True, otherwise, letting the Triple be stored in the ontology.
 */
public class FilterDataProcessor implements DataProcessor {

    protected static final Logger logger = LogManager.getLogger();

    private static final String CONFIGURATION_FILE_KEY = "configurationFile";
    private static final String UNWANTED_PREDICATES_KEY = "unwantedPredicates";

    private String configurationFile = null;
    private Set<String> unwantedPredicates = new HashSet<>();
    private boolean configurationIsLoaded = false;

    @Override
    public boolean postProcessTriple(Triple triple) {
        if (!configurationIsLoaded) {
            loadConfigurationDataFromFile(configurationFile);
        }

        return shallTripleSurvive(triple);
    }

    public boolean shallTripleSurvive(Triple triple) {
        return !unwantedPredicates.contains(triple.predicate);
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

        setUnwantedPredicates(loadUnwantedPredicates(configurationData));

        configurationIsLoaded = true;
    }

    public void setUnwantedPredicates(Set<String> unwantedPredicates) {
        this.unwantedPredicates = unwantedPredicates;
    }

    private Set<String> loadUnwantedPredicates(JSONObject configurationData) {
        return StreamSupport
                .stream(configurationData.optJSONArray(UNWANTED_PREDICATES_KEY).spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }
}
