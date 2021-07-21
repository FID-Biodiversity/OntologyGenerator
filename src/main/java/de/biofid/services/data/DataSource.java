package de.biofid.services.data;

import org.json.JSONObject;

import java.io.IOException;


/**
 * The DataSource establishes the connection to any data source.
 * The data source may be a file, an API, or an URL.
 */
public interface DataSource {
    /** This method should return data that correspond to the given string. */
    Object getDataForString(String string) throws IOException;

    /** This method will receive the user configuration for this DataProcessor as defined in the config file. */
    default void setParameters(JSONObject parameters) {};
}
