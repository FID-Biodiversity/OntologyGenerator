package de.biofid.services.dummy;

import de.biofid.services.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class DummySerializer extends Serializer {

    private String serializedString;
    private String sink;
    private OutputStream outputStream = new ByteArrayOutputStream();

    @Override
    public void serialize(String outputString) {
        this.serializedString = outputString;
    }

    @Override
    public void addSink(String sink) {
        this.sink = sink;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String getSerializedString() {
        if (serializedString != null) {
            return serializedString;
        } else {
            return outputStream.toString();
        }
    }
}
