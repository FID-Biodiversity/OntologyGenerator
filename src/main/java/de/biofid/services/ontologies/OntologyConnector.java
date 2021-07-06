package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

public class OntologyConnector {

    private final Ontology ontology;

    public OntologyConnector(Ontology ontology) {
        this.ontology = ontology;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void addTripleToOntology(Triple triple) {
        ontology.addTriple(triple);
    }

    public void serializeOntology(Serializer serializer) {
        ontology.serialize(serializer);
    }
}
