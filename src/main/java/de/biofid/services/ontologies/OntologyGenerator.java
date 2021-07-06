package de.biofid.services.ontologies;

import de.biofid.services.data.DataService;
import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

import java.util.List;

public class OntologyGenerator {

    private String ontologyName;
    private OntologyConnector ontologyConnector;
    private List<DataService> dataServices;
    private Serializer serializer;

    public OntologyGenerator(String ontologyName, OntologyConnector ontologyConnector, List<DataService> dataServices,
                             Serializer serializer) {
        this.ontologyName = ontologyName;
        this.ontologyConnector = ontologyConnector;
        this.dataServices = dataServices;
        this.serializer = serializer;
    }

    public void addTriples(List<Triple> triples) {
        for (Triple triple : triples) {
            addTriple(triple);
        }
    }

    public void addTriple(Triple triple) {
        ontologyConnector.addTripleToOntology(triple);
    }

    public void serialize() {
        ontologyConnector.serializeOntology(serializer);
    }
}
