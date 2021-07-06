package de.biofid.services.dummy;

import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.Ontology;
import de.biofid.services.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DummyOntology implements Ontology {

    Iterator<String> resourceUriIterator;
    Set<Triple> triples = new HashSet<>();

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
        triples.remove(triple);
    }

    @Override
    public void serialize(Serializer serializer) {
        String tripleString = triples.toString();
        InputStream inputStream = new ByteArrayInputStream(tripleString.getBytes(StandardCharsets.UTF_8));
        serializer.serialize(inputStream);
    }

    public void setResourceUriIterator(Iterator<String> iterator) {
        this.resourceUriIterator = iterator;
    }
}
