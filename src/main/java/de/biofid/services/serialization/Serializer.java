package de.biofid.services.serialization;

import java.io.InputStream;

public abstract class Serializer {
    abstract public void serialize(InputStream stream);
}
