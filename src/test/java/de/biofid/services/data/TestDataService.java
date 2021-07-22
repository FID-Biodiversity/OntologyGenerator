package de.biofid.services.data;

import de.biofid.services.data.generators.gbif.GbifDirectTaxonChildDataGenerator;
import de.biofid.services.data.processors.PredicateMappingDataProcessor;
import de.biofid.services.dummy.DummyDataSource;
import de.biofid.services.dummy.DummyOntology;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static de.biofid.services.data.DataCollections.isTripleInList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDataService {
    private DummyDataSource dataSource;
    private DataGenerator dataGenerator;
    private DataProcessor dataProcessor;

    @Test
    public void testGetData() throws FileNotFoundException {
        defaultSetup();
        addDataFromJsonFileToDataSource("src/test/resources/data/gbif/gbifFagusSylvaticaChildrenResponse.json");

        DataService service = new DataService(dataSource, dataGenerator, dataProcessor);

        List<Triple> triples = service.getTriples();

        assertEquals(75, triples.size());
        assertTrue(isTripleInList(new Triple("gbif:7984102", "http://rs.tdwg.org/dwc/terms/kingdom", "Plantae"), triples));
        assertTrue(isTripleInList(new Triple("gbif:4928315", "http://rs.tdwg.org/dwc/terms/kingdom", "Plantae"), triples));
    }

    private void defaultSetup() {
        dataSource = new DummyDataSource();
        dataGenerator = new GbifDirectTaxonChildDataGenerator();
        dataProcessor = new PredicateMappingDataProcessor();

        DummyOntology dummyOntology = new DummyOntology();
        String[] uris = {"gbif:2882316"};
        dummyOntology.setResourceUriIterator(Arrays.stream(uris).iterator());
        dataGenerator.setOntology(dummyOntology);
        dataGenerator.setParameters(new JSONObject("{\"ids\": [1234]}"));

        JSONObject processorConfiguration = new JSONObject();
        processorConfiguration.put("gbif:kingdom", "http://rs.tdwg.org/dwc/terms/kingdom");
        dataProcessor.setParameters(processorConfiguration);
    }

    private void addDataFromJsonFileToDataSource(String filePath) throws FileNotFoundException {
        JSONObject testData = JsonDataReader.readJSONObjectFromFile(filePath);
        dataSource.addData(testData.toString());
    }
}