package de.biofid.services.data;

import java.io.IOException;


/**
 * The DataSource establishes the connection to any data source.
 * The data source may be a file, an API, or an URL.
 */
public interface DataSource {
    /** This method should return data that correspond to the given string. */
    Object getDataForString(String string) throws IOException;
}
