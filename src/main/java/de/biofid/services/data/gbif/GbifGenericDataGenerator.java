package de.biofid.services.data.gbif;

import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataSource;
import de.biofid.services.data.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * A highly generic data class to generate Triple objects from the GBIF API.
 * It can be easily used as base class for most GBIF API calls.
 */
public class GbifGenericDataGenerator implements DataGenerator {

    public static final String KEY_GBIF_IDS_TO_PROCESS = "ids";
    public static final String GBIF_KEY_NAMESPACE = "gbif";

    protected static final Logger logger = LogManager.getLogger();
    protected static final String SPECIES_STRING = "species";
    protected static final String RESULTS_STRING = "results";
    protected static final String IS_LAST_PAGE_STRING = "endOfRecords";

    private static final String GBIF_API_BASE_URL = "https://api.gbif.org/v1";

    protected DataSource dataSource;
    protected JSONObject parameters = new JSONObject();
    protected Iterator<String> gbifIdIterator = null;
    protected Iterator<Triple> tripleIterator = null;
    private String currentProcessedTaxonId;

    /**
     * Iterates the data of the DataSource.
     * This class guarantees to return an iterator (even if empty).
     */
    @Override
    public Triple next() throws NoSuchElementException {
        if (!areAllExpectedParametersSet()) {
            logger.error("The parameters expected for the GBIF DataGenerator are not given!");
            throw new NoSuchElementException();
        }

        if (gbifIdIterator == null) {
            setup();
        }

        return getNextTripleOrThrow();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void setParameters(JSONObject parameters) {
        this.parameters = parameters;
    }

    public String getGbifApiBaseUrl() {
        return GBIF_API_BASE_URL;
    }

    protected void setup() {
        gbifIdIterator = getGbifIdsToProcess().iterator();
        updateIterators();
    }

    protected boolean areAllExpectedParametersSet() {
        return parameters.has(KEY_GBIF_IDS_TO_PROCESS);
    }

    protected Triple getNextTripleOrThrow() throws NoSuchElementException {
        Triple triple;

        try {
            triple = tripleIterator.next();
        } catch (NoSuchElementException ex) {
            updateIterators();
            triple = getNextTripleOrThrow();
        }

        return triple;
    }

    protected void setCurrentProcessedGbifId(String currentProcessedTaxonId) {
        this.currentProcessedTaxonId = currentProcessedTaxonId;
    }

    protected String getCurrentProcessedGbifId() {
        return currentProcessedTaxonId;
    }

    protected void updateIterators() {
        String gbifId = gbifIdIterator.next();
        setCurrentProcessedGbifId(gbifId);
        String gbifResponseString = callGbifForDataForCurrentGbifId();
        tripleIterator = convertGbifResponseToData(gbifResponseString).iterator();
    }

    protected List<String> getGbifIdsToProcess() {
        JSONArray ids = parameters.getJSONArray(KEY_GBIF_IDS_TO_PROCESS);
        return StreamSupport.stream(ids.spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    protected String callGbifForDataForCurrentGbifId() {
        return callGbifForDataForId(getCurrentProcessedGbifId());
    }

    protected String callGbifForDataForId(String gbifId) {
        try {
            logger.debug("Processing GBIF ID \"" + gbifId + "\" ...");
            String gbifUrl = createGbifApiUrlFromGbifId(gbifId);
            String gbifResponse = (String) dataSource.getDataForString(gbifUrl);
            logger.debug("Received GBIF response!");
            return gbifResponse;
        } catch (IOException ex) {
            return "{}";
        }
    }

    protected List<Triple> convertGbifResponseToData(String gbifResponse) {
        logger.debug("Convert response to data object!");

        List<Triple> gbifData = Collections.emptyList();
        try {
            JSONObject jsonResponse = new JSONObject(gbifResponse);
            gbifData = convertGbifResponseToData(jsonResponse);
        } catch (JSONException ex) {
            logger.warn("The GBIF response is not a JSON object! Response was: " + gbifResponse);
        }

        return gbifData;
    }

    protected List<Triple> convertGbifResponseToData(JSONObject gbifResponseData) {
        String gbifId = getCurrentProcessedGbifId();

        return gbifResponseData.keySet().stream()
                .map(key -> createTriple(gbifId, key, gbifResponseData.get(key)))
                .collect(Collectors.toList());
    }

    protected String createGbifApiUrlFromGbifId(String gbifId) {
        return getGbifApiBaseUrl() + "/species/" + gbifId;
    }

    protected Triple createTriple(String subject, String predicate, Object object) {
        return new Triple(getNameWithNamespace(subject), getNameWithNamespace(predicate), object);
    }

    protected String getNameWithNamespace(String name) {
        return GBIF_KEY_NAMESPACE + ":" + name;
    }
}
