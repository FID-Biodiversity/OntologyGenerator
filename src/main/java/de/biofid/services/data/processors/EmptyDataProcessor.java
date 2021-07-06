package de.biofid.services.data.processors;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import org.json.JSONObject;

/**
 * This DataGenerator just passes back the given data and does nothing.
 */
public class EmptyDataProcessor implements DataProcessor {

    @Override
    public boolean postProcessTriple(Triple triple) {
        return true;
    }

    @Override
    public void setParameters(JSONObject parameters) {}
}
