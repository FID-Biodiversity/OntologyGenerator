package de.biofid.services.dummy;

import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.Ontology;
import de.biofid.services.serialization.Serializer;

import java.util.Iterator;

public class DummyOntology implements Ontology {

    Iterator<String> resourceUriIterator;

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

    }

    @Override
    public void removeTriple(Triple triple) {

    }

    @Override
    public void serialize(Serializer serializer) {

    }

    public void setResourceUriIterator(Iterator<String> iterator) {
        this.resourceUriIterator = iterator;
    }
}
