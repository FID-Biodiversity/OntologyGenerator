package de.biofid.services.ontologies;

import de.biofid.services.data.DataService;
import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

import java.util.List;

public class OntologyGenerator {

    private final String ontologyName;
    private final OntologyConnector ontologyConnector;
    private final List<DataService> dataServices;
    private final Serializer serializer;

    public OntologyGenerator(String ontologyName, OntologyConnector ontologyConnector, List<DataService> dataServices,
                             Serializer serializer) {
        this.ontologyName = ontologyName;
        this.ontologyConnector = ontologyConnector;
        this.dataServices = dataServices;
        this.serializer = serializer;
    }

    public String getOntologyName() {
        return ontologyName;
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

    public void generate() {
        for (DataService service : dataServices) {
            List<Triple> triples = service.getTriples();
            addTriples(triples);
        }
    }
}
