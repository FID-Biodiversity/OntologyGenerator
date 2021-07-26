package de.biofid.services.data.generators.gbif;

import de.biofid.services.data.Triple;
import de.biofid.services.data.gbif.GbifUriFactory;
import de.biofid.services.data.gbif.NoGbifUriException;
import de.biofid.services.ontologies.Ontology;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calls data from the GBIF API URL {BASE_API}/species/{SPECIES_ID}/children .
 * Returns all (!) data received from the API as Triple objects.
 */
public class GbifDirectTaxonChildDataGenerator extends GbifGenericDataGenerator {

    private static final String CHILDREN_STRING = "children";
    private static final String TAXON_ID_STRING = "taxonID";

    private Iterator<Object> childTaxonData = Collections.emptyIterator();
    private Ontology ontology;
    private String currentParentId;

    @Override
    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    @Override
    public String createGbifApiUrlFromGbifId(String gbifId) {
        return getGbifApiBaseUrl() + "/" + SPECIES_STRING + "/" + gbifId + "/" + CHILDREN_STRING + getRequestParametersAsString();
    }

    @Override
    protected List<String> getGbifIdsToProcess() {
        Iterator<String> taxonUriIterator = ontology.iterateResourceUris();
        List<String> taxonUrisToProcess = iteratorToList(taxonUriIterator);

        return taxonUrisToProcess.stream()
                .map(this::extractGbifIdFromUri)
                .collect(Collectors.toList());
    }

    @Override
    protected void updateIterators() {
        logger.debug("Updating iterators!");

        if (childTaxonData.hasNext()) {
            logger.debug("Updating from local data!");
            updateIteratorsWithLocalDataset();
        } else if (isLastPage) {
            logger.debug("Is last page in dataset!");
            logger.debug("Moving to next GBIF Parent ID!");
            getNextGbifIdToProcess();
            currentParentId = getCurrentProcessedGbifId();
            updateTripleIteratorWithNewData();
        } else {
            updateTripleIteratorWithNewData();
        }
    }

    protected void updateIteratorsWithLocalDataset() {
        JSONObject taxonData = (JSONObject) childTaxonData.next();
        setCurrentProcessedGbifId(getGbifIdFromDataset(taxonData));
        tripleIterator = super.convertGbifResponseToData(taxonData).iterator();
    }

    protected void updateTripleIteratorWithNewData() {
        JSONObject nextPageData = getNextPageForDataset();
        updateCurrentChildId(nextPageData);

        logger.debug("Updating Triple Iterator");
        tripleIterator = convertGbifResponseToData(nextPageData).iterator();
    }

    @Override
    protected List<Triple> convertGbifResponseToData(JSONObject gbifResponseData) {
        logger.debug("Converting GBIF Response to data!");

        childTaxonData = gbifResponseData.getJSONArray(RESULTS_STRING).iterator();
        JSONObject taxonChildData = (JSONObject) childTaxonData.next();

        return super.convertGbifResponseToData(taxonChildData);
    }

    @Override
    protected boolean areAllExpectedParametersSet() {
        return ontology != null || super.areAllExpectedParametersSet();
    }

    protected String getGbifIdFromDataset(JSONObject taxonData) {
        try {
            return GbifUriFactory.extractGbifIdFromUri(taxonData.getString(TAXON_ID_STRING));
        } catch (NoGbifUriException ex) {
            logger.error("The given dataset does not contain an proper GBIF ID!\nDataset: " + taxonData);
            return null;
        }
    }

    private List<String> iteratorToList(Iterator<String> iterator) {
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    private String extractGbifIdFromUri(String uri) {
        try {
            return GbifUriFactory.extractGbifIdFromUri(uri);
        } catch (NoGbifUriException ex) {
            logger.error("An error occurred while extracting the GBIF ID from " + uri +" . Returning null!");
            return null;
        }
    }

    @Override
    protected JSONObject callGbifForDataForCurrentGbifId() {
        return callGbifForDataForId(currentParentId);
    }

    protected void updateCurrentChildId(JSONObject data) {
        Object firstDatasetInResponse = data.getJSONArray(RESULTS_STRING).get(0);
        setCurrentProcessedGbifId(getGbifIdFromDataset((JSONObject) firstDatasetInResponse));
    }
}
