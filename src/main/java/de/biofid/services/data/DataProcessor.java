package de.biofid.services.data;

import org.json.JSONObject;


/**
 * A DataProcessor is the last of the classes called in a DataService. It allows modifications of the Triples that
 * will be passed to the ontology.
 */
public interface DataProcessor {
    /**
     * This method allows a further processing of a Triple object.
     * When the method returns false, the Triple is removed from any further processing.
     */
    boolean postProcessTriple(Triple triple);

    /** This method will receive the user configuration for this DataProcessor as defined in the config file. */
    void setParameters(JSONObject parameters);
}
