package de.biofid.services.dummy;

import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.Ontology;
import de.biofid.services.serialization.Serializer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DummyOntology implements Ontology {

    Iterator<String> resourceUriIterator;
    List<Triple> triples = new ArrayList<>();

    @Override
    public Iterator<Triple> iterateTriples() {
        return null;
    }

    @Override
    public Iterator<String> iterateResourceUris() {
        return resourceUriIterator;
    }

    @Override
    public void addTriple(Triple triple) {
        triples.add(triple);
    }

    @Override
    public void removeTriple(Triple triple) {
        triples = triples.stream()
                .filter(tripleInList -> tripleInList.equals(triple))
                .collect(Collectors.toList());
    }

    @Override
    public void serialize(Serializer serializer) {
        String tripleString = triples.toString();
        OutputStream outputStream = serializer.getOutputStream();

        try {
            outputStream.write(tripleString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {}
    }

    public void setResourceUriIterator(Iterator<String> iterator) {
        this.resourceUriIterator = iterator;
    }
}
