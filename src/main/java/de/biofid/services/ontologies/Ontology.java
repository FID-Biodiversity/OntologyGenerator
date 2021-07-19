package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The interface for handing data to an ontology implementation.
 */
public interface Ontology {
    /** Iterate all Triples in the ontology. */
    Iterator<Triple> iterateTriples();

    /** Iterate over all Subjects of the ontology. */
    Iterator<String> iterateResourceUris();

    void addTriple(Triple triple);
    void removeTriple(Triple triple);

    /** Write the ontology to the given serializer. */
    void serialize(Serializer serializer);

    /** Add the namespace definitions for the ontology.
     * In this, the key is the abbreviation and the value is the namespace URI.
     * E.g: "biofid": "https://www.biofid.de/bio-ontologies/"
     */
    void setNamespaces(HashMap<String, String> namespaces);
}
