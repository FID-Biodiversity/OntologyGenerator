package de.biofid.services.serialization;

import java.io.OutputStream;

public abstract class Serializer {
    abstract public void serialize(String outputString);
    abstract public void addSink(String sink);
    abstract public OutputStream getOutputStream();
}
