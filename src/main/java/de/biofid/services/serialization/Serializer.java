package de.biofid.services.serialization;

import java.io.OutputStream;

public abstract class Serializer {
    abstract public void serialize(OutputStream stream);
}
