package de.biofid.services.data;

import java.io.IOException;

public interface DataSource {
    abstract public Object getDataForString(String string) throws IOException;
}
