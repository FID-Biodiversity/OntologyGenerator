package de.biofid.services.data;

import org.json.JSONObject;

public interface DataProcessor {
    /**
     * This method allows a further processing of a Triple object.
     * When the method returns false, the Triple is removed from any further processing.
     */
    boolean postProcessTriple(Triple triple);
    void setParameters(JSONObject parameters);
}
