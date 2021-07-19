package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.dummy.DummyOntology;
import de.biofid.services.dummy.DummySerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOntologyConnector {

    private DummyOntology ontology;


    @Test
    public void testReadNamespaceData() throws FileNotFoundException {
        OntologyConnector ontologyConnector = new OntologyConnector(ontology);

        ontologyConnector.setOntologyNamespacesFromFile("src/test/resources/configuration/namespaceTest.json");

        HashMap<String, String> namespaces = ontology.getNamespaces();
        assertEquals("https://www.biofid.de/bio-ontologies/", namespaces.get("biofid"));
        assertEquals("https://www.gbif.org/species/", namespaces.get("gbif-species"));
    }

    @BeforeEach
    public void setup() {
        ontology = new DummyOntology();
    }
}
