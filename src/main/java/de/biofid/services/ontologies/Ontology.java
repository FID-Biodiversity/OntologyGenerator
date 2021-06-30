package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

import java.util.Iterator;

public interface Ontology {
    Iterator<Triple> iterateTriples();
    Iterator<String> iterateResourceUris();
    void addTriple(Triple triple);
    void removeTriple(Triple triple);
    void serialize(Serializer serializer);
}
