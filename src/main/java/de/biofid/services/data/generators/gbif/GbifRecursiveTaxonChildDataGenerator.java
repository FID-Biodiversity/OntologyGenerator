package de.biofid.services.data.generators.gbif;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Same as the GbifDirectTaxonChildDataGenerator, but also collects the data of the all children recursively.
 * Returns all (!) data received from the API as Triple objects.
 */
public class GbifRecursiveTaxonChildDataGenerator extends GbifDirectTaxonChildDataGenerator {
    protected List<String> childrenGbifIds = new ArrayList<>();
    protected List<String> childrenGbifIdsToProcess = new ArrayList<>();

    @Override
    protected void updateIterators() {
        if (!gbifIdIterator.hasNext()) {
            updateGbifIdIterator();
        }

        super.updateIterators();
    }

    protected void updateGbifIdIterator() {
        childrenGbifIds = List.copyOf(childrenGbifIdsToProcess);
        gbifIdIterator = childrenGbifIds.iterator();
        childrenGbifIdsToProcess.clear();
    }

    @Override
    protected void updateCurrentChildId(JSONObject data) {
        super.updateCurrentChildId(data);
        childrenGbifIdsToProcess.add(getCurrentProcessedGbifId());
    }
}
