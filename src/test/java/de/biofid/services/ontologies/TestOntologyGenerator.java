package de.biofid.services.ontologies;

import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataService;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import de.biofid.services.data.gbif.generators.GbifGenericDataGenerator;
import de.biofid.services.data.processors.EmptyDataProcessor;
import de.biofid.services.dummy.DummyDataSource;
import de.biofid.services.dummy.DummyOntology;
import de.biofid.services.dummy.DummySerializer;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOntologyGenerator {
    private DummyOntology ontology;
    private DummySerializer serializer;
    private OntologyGenerator generator;

    @Test
    public void testAddTripleToOntology() {
        generator.addTriple(new Triple("biofid:12345", "rdfs:label", "Bird"));
        generator.addTriple(new Triple("biofid:56789", "rdfs:label", "Plant"));

        generator.serialize();

        String serializedString = serializer.getSerializedString();
        assertEquals("[biofid:12345 rdfs:label Bird, biofid:56789 rdfs:label Plant]", serializedString);
    }

    @Test
    public void testGenerate() {
        generator.generate();
        generator.serialize();

        String serializedString = serializer.getSerializedString();
        assertTrue(serializedString.contains("gbif:1234 gbif:sourceTaxonKey 172761619"));
    }

    @BeforeEach
    public void setup() throws FileNotFoundException {
        String ontologyName = "TestOntology";

        ontology = new DummyOntology();
        OntologyConnector ontologyConnector = new OntologyConnector(ontology);

        List<DataService> dataServices = createDataServices();
        serializer = new DummySerializer();

        generator = new OntologyGenerator(ontologyName, ontologyConnector, dataServices, serializer);
    }

    private List<DataService> createDataServices() throws FileNotFoundException {
        JSONObject apiResponse = JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifPasserDomesticusApiResponse.json");
        DummyDataSource source = new DummyDataSource();
        source.addData(apiResponse.toString());

        DataGenerator dataGenerator = new GbifGenericDataGenerator();
        dataGenerator.setParameters(new JSONObject("{\"ids\": [1234]}"));

        DataService dummyService = new DataService(source, dataGenerator, new EmptyDataProcessor());

        List<DataService> dataServices = new ArrayList<>();
        dataServices.add(dummyService);

        return dataServices;
    }
}
