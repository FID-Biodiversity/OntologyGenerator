package de.biofid.services.dummy;

import de.biofid.services.serialization.Serializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DummySerializer extends Serializer {

    private InputStream stream;

    @Override
    public void serialize(InputStream stream) {
        this.stream = stream;
    }

    public String getSerializedString() {
        return new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
