package de.biofid.services.data.gbif.generators;

import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataSource;
import de.biofid.services.data.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
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
    protected static final String OFFSET_PARAMETER_STRING = "offset";
    protected static final String LIMIT_PARAMETER_STRING = "limit";
    protected static final int NUMBER_OF_DATASETS_PER_REQUEST = 20;
    protected static final int OFFSET_DEFAULT = NUMBER_OF_DATASETS_PER_REQUEST * -1;

    private static final String GBIF_API_BASE_URL = "https://api.gbif.org/v1";

    protected DataSource dataSource;
    protected JSONObject parameters = new JSONObject();

    protected Iterator<Triple> tripleIterator = null;
    protected List<String> gbifIds = Collections.emptyList();
    protected boolean isLastPage = true;
    protected Iterator<String> gbifIdIterator = null;
    protected int currentOffset = OFFSET_DEFAULT;

    private String currentProcessedTaxonId;
    private boolean isSetup = false;

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

        if (!isSetup) {
            setup();
            isSetup = true;
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
        gbifIds = getGbifIdsToProcess();
        gbifIdIterator = gbifIds.iterator();
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
        if (isLastPage) {
            getNextGbifIdToProcess();
        }
        JSONObject newData = getNextPageForDataset();

        tripleIterator = convertGbifResponseToData(newData).iterator();
    }

    protected JSONObject getNextPageForDataset() {
        JSONObject gbifResponseData = callGbifForDataForCurrentGbifId();
        updateMetadata(gbifResponseData);

        return gbifResponseData;
    }

    protected String getNextGbifIdToProcess() {
        String gbifId = gbifIdIterator.next();
        setCurrentProcessedGbifId(gbifId);
        resetMetadata();

        return gbifId;
    }

    protected void resetMetadata() {
        updateMetadata(new JSONObject());
    }

    protected void updateMetadata(JSONObject data) {
        isLastPage = isLastDataPage(data);
        currentOffset = getOffsetFromDataset(data) + NUMBER_OF_DATASETS_PER_REQUEST;
    }

    protected List<String> getGbifIdsToProcess() {
        JSONArray ids = parameters.getJSONArray(KEY_GBIF_IDS_TO_PROCESS);
        return StreamSupport.stream(ids.spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    protected JSONObject callGbifForDataForCurrentGbifId() {
        return callGbifForDataForId(getCurrentProcessedGbifId());
    }

    protected JSONObject callGbifForDataForId(String gbifId) {
        try {
            logger.debug("Processing GBIF ID \"" + gbifId + "\" ...");
            String gbifUrl = createGbifApiUrlFromGbifId(gbifId);
            String gbifResponse = (String) dataSource.getDataForString(gbifUrl);
            logger.debug("Received GBIF response!");
            return new JSONObject(gbifResponse);
        } catch (IOException ex) {
            return new JSONObject();
        }
    }

    protected boolean isLastDataPage(JSONObject data) {
        return !data.has(IS_LAST_PAGE_STRING) || data.getBoolean(IS_LAST_PAGE_STRING);
    }

    protected int getOffsetFromDataset(JSONObject data) {
        return data.has(OFFSET_PARAMETER_STRING) ? data.getInt(OFFSET_PARAMETER_STRING) : OFFSET_DEFAULT;
    }

    protected List<Triple> convertGbifResponseToData(JSONObject gbifResponseData) {
        String gbifId = getCurrentProcessedGbifId();

        return gbifResponseData.keySet().stream()
                .map(key -> createTriple(gbifId, key, gbifResponseData.get(key)))
                .collect(Collectors.toList());
    }

    protected String createGbifApiUrlFromGbifId(String gbifId) {
        return getGbifApiBaseUrl() + "/species/" + gbifId + getRequestParametersAsString();
    }

    protected String getRequestParametersAsString() {
        return "?" + LIMIT_PARAMETER_STRING + "=" + NUMBER_OF_DATASETS_PER_REQUEST +
                "&" + OFFSET_PARAMETER_STRING + "=" + currentOffset;
    }

    protected Triple createTriple(String subject, String predicate, Object object) {
        return new Triple(getNameWithNamespace(subject), getNameWithNamespace(predicate), object);
    }

    protected String getNameWithNamespace(String name) {
        return GBIF_KEY_NAMESPACE + ":" + name;
    }
}
