package de.biofid.services.data.generators;

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


public class GbifGenericDataGenerator implements DataGenerator {

    public static final String KEY_GBIF_IDS_TO_PROCESS = "ids";
    public static final String GBIF_KEY_NAMESPACE = "gbif";

    protected static final Logger logger = LogManager.getLogger();

    private static final String GBIF_API_BASE_URL = "https://api.gbif.org/v1/species";

    private DataSource dataSource;
    private JSONObject parameters = new JSONObject();
    private Iterator<String> gbifIdIterator = null;
    private Iterator<Triple> tripleIterator = null;

    /**
     * Iterates the data of the DataSource.
     * This class guarantees to return an iterator (even if empty).
     */
    @Override
    public Triple next() throws NoSuchElementException {
        if (!parameters.has(KEY_GBIF_IDS_TO_PROCESS)) {
            logger.warn("There are no GBIF IDs given!");
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

    private void setup() {
        gbifIdIterator = getGbifIdsToProcess().iterator();
        tripleIterator = callGbifForDataForId(gbifIdIterator.next()).iterator();
    }

    private Triple getNextTripleOrThrow() throws NoSuchElementException {
        Triple triple;

        try {
            triple = tripleIterator.next();
        } catch (NoSuchElementException ex) {
            String gbifId = gbifIdIterator.next();
            tripleIterator = callGbifForDataForId(gbifId).iterator();
            triple = getNextTripleOrThrow();
        }

        return triple;
    }

    private List<String> getGbifIdsToProcess() {
        JSONArray ids = parameters.getJSONArray(KEY_GBIF_IDS_TO_PROCESS);
        return StreamSupport.stream(ids.spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<Triple> callGbifForDataForId(String gbifId) {
        try {
            logger.debug("Processing GBIF ID \"" + gbifId + "\" ...");
            String gbifUrl = createGbifApiUrlFromGbifId(gbifId);
            String gbifResponse = (String) dataSource.getDataForString(gbifUrl);
            logger.debug("Received GBIF response!");
            return convertGbifResponseToData(gbifId, gbifResponse);
        } catch (IOException ex) {
            return Collections.emptyList();
        }
    }

    private List<Triple> convertGbifResponseToData(String gbifId, String gbifResponse) {
        logger.debug("Convert response to data object!");

        List<Triple> gbifData = Collections.emptyList();
        try {
            JSONObject jsonResponse = new JSONObject(gbifResponse);

            gbifData = jsonResponse.keySet().stream()
                    .map(key -> createTriple(gbifId, key, jsonResponse.get(key)))
                    .collect(Collectors.toList());
        } catch (JSONException ex) {
            logger.warn("The GBIF response is not a JSON object! Response was: " + gbifResponse);
        }

        return gbifData;
    }

    private Triple createTriple(String subject, String predicate, Object object) {
        return new Triple(getNameWithNamespace(subject), getNameWithNamespace(predicate), object);
    }

    private String getNameWithNamespace(String name) {
        return GBIF_KEY_NAMESPACE + ":" + name;
    }

    private String createGbifApiUrlFromGbifId(String gbifId) {
        return GBIF_API_BASE_URL + "/" + gbifId;
    }
}
