package de.biofid.services.data;

import de.biofid.services.data.generators.OntologyDataGenerator;
import de.biofid.services.dummy.DummyOntology;
import de.biofid.services.ontologies.Ontology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestOntologyDataGenerator {

    OntologyDataGenerator ontologyDataGenerator;
    Ontology ontology;

    @Test
    public void testNextWithEmptyOntology() {
        ontologyDataGenerator = new OntologyDataGenerator();
        assertThrows(NoSuchElementException.class, () -> ontologyDataGenerator.next());
    }

    @Test
    public void testNext() {
        Triple triple = ontologyDataGenerator.next();
        assertEquals("gbif:12345", triple.object);
        assertEquals("gbif:taxonID", triple.predicate);

        triple = ontologyDataGenerator.next();
        assertEquals("gbif:kingdomKey", triple.predicate);

        triple = ontologyDataGenerator.next();
        assertEquals("gbif:789", triple.subject);

        assertThrows(NoSuchElementException.class, () -> ontologyDataGenerator.next());
    }

    @BeforeEach
    public void setup() {
        ontologyDataGenerator = new OntologyDataGenerator();
        ontology = new DummyOntology();
        addTriplesToOntology();

        ontologyDataGenerator.setOntology(ontology);
    }

    private void addTriplesToOntology() {
        ontology.addTriple(new Triple("gbif:12345", "gbif:taxonID", "gbif:12345"));
        ontology.addTriple(new Triple("gbif:12345", "gbif:kingdomKey", "gbif:789"));
        ontology.addTriple(new Triple("gbif:789", "gbif:taxonID", "gbif:789"));
    }
}
