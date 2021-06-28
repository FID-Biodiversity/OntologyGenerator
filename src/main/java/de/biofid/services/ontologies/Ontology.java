package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

import java.util.Iterator;

public interface Ontology {
    abstract public Iterator<Triple> iterateTriples();
    abstract Iterator<String> iterateResourceUris();
    abstract void addTriple(Triple triple);
    abstract void removeTriple(Triple triple);
    abstract void serialize(Serializer serializer);
    //abstract void inference(Reasoner reasoner);
}
