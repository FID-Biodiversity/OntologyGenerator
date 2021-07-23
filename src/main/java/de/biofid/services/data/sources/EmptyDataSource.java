package de.biofid.services.data.sources;

import de.biofid.services.data.DataSource;

import java.io.IOException;

/**
 * This is a dummy DataSource that is used as default, when no DataSource is given in the configuration.
 * It returns null and nothing else!
 */
public class EmptyDataSource implements DataSource {
    @Override
    public Object getDataForString(String string) throws IOException {
        return null;
    }
}
