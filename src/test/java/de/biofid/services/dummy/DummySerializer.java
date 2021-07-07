package de.biofid.services.dummy;

import de.biofid.services.serialization.Serializer;

import java.io.OutputStream;

public class DummySerializer extends Serializer {

    private OutputStream stream;

    @Override
    public void serialize(OutputStream stream) {
        this.stream = stream;
    }

    public String getSerializedString() {
        return stream.toString();
    }
}
