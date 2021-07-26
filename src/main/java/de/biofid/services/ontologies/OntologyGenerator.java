package de.biofid.services.ontologies;

import de.biofid.services.data.DataService;
import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The OntologyGenerator calls the DataServices and hands their output to the OntologyConnector.
 */
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

    public void addTriplesToOntology(Collection<Triple> triples) {
        for (Triple triple : triples) {
            addTriple(triple);
        }
    }

    public void removeTriplesFromOntology(Collection<Triple> triplesToRemove) {
        for (Triple triple : triplesToRemove) {
            removeTriple(triple);
        }
    }

    public void addTriple(Triple triple) {
        ontologyConnector.addTripleToOntology(triple);
    }

    public void removeTriple(Triple triple) {
        ontologyConnector.removeTripleFromOntology(triple);
    }

    public void serialize() {
        ontologyConnector.serializeOntology(serializer);
    }

    public void generate() {
        for (DataService service : dataServices) {
            updateOntology(service.getTriplesToAdd(), service.getTriplesToRemove());
        }
    }

    private void updateOntology(Collection<Triple> triplesToAdd, Collection<Triple> triplesToRemove) {
        addTriplesToOntology(triplesToAdd);
        removeTriplesFromOntology(triplesToRemove);
    }
}
