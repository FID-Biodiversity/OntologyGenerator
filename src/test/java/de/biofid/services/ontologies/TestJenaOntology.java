package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import de.biofid.services.dummy.DummySerializer;
import org.apache.jena.atlas.iterator.Iter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestJenaOntology {

    private JenaOntology ontology;
    private DummySerializer serializer;

    @Test
    public void testSetNamespaces() throws FileNotFoundException {
        HashMap<String, String> namespaces = getNamespaceDataFromFile("src/test/resources/configuration/namespaceTest.json");

        ontology.setNamespaces(namespaces);
        ontology.serialize(serializer);

        String serializedString = serializer.getSerializedString();

        assertTrue(serializedString.contains("xmlns:biofid=\"https://www.biofid.de/bio-ontologies/\""));
        assertTrue(serializedString.contains("gbif-species=\"https://www.gbif.org/species/\""));
        assertTrue(serializedString.contains("biofid-terms:gbifUrl"));
        assertTrue(serializedString.contains("https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/123456"));
    }

    @Test
    public void testIterateResourceUris() {
        Iterator<String> uriIterator = ontology.iterateResourceUris();
        assertEquals("https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/123456", uriIterator.next());
        assertThrows(NoSuchElementException.class, uriIterator::next);
    }

    @Test
    public void testSerialize() {
        DummySerializer serializer = new DummySerializer();
        ontology.serialize(serializer);

        String serializedString = serializer.getSerializedString();
        assertTrue(serializedString.contains("https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/123456"));
        assertTrue(serializedString.contains("https://www.gbif.org/species/123456"));
    }

    @BeforeEach
    public void setup() {
        serializer = new DummySerializer();

        setupOntology();
    }

    private HashMap<String, String> getNamespaceDataFromFile(String filePath) throws FileNotFoundException {
        JSONObject namespaceData = JsonFileReader.read(filePath);

        HashMap<String, String> namespaces = new HashMap<>();
        for (String key : namespaceData.keySet()) {
            namespaces.put(key, namespaceData.getString(key));
        }

        return namespaces;
    }

    private void setupOntology() {
        ontology = new JenaOntology();

        ontology.addTriple(new Triple(
                "https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/123456",
                "http://rs.tdwg.org/dwc/terms/kingdom",
                "https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/12"
        ));
        ontology.addTriple(new Triple(
                "https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/123456",
                "https://www.biofid.de/bio-ontologies/terms/gbifUrl",
                "https://www.gbif.org/species/123456"
        ));
    }
}
