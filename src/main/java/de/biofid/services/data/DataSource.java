package de.biofid.services.data;

import de.biofid.services.exceptions.ValueException;

import java.io.IOException;

public interface DataSource {
    abstract public Object getDataForString(String string) throws IOException;
}
