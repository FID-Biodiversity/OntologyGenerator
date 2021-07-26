package de.biofid.services.factories;

import de.biofid.services.configuration.DataServiceConfiguration;
import de.biofid.services.data.*;
import de.biofid.services.dummy.DummyDataSource;
import de.biofid.services.dummy.DummyOntology;
import de.biofid.services.exceptions.ValueException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static de.biofid.services.data.TripleAssertions.assertTripleHas;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestServiceFactory {
    public static DataServiceConfiguration dataServiceConfiguration;

    @Test
    public void testCreateDataSource() throws IOException, ValueException {
        DummyDataSource dataSource = (DummyDataSource) ServiceFactory.createDataSource(dataServiceConfiguration);
        dataSource.addData("Foo");
        assertEquals("Foo", dataSource.getDataForString("Bar"));
    }

    @Test
    public void testCreateDataGenerator() throws FileNotFoundException, ValueException {
        DummyDataSource dataSource = getDataSource();

        DataGenerator generator = ServiceFactory.createDataGenerator(dataServiceConfiguration, dataSource,
                new DummyOntology());

        assertEquals("de.biofid.services.data.generators.gbif.GbifGenericDataGenerator", generator.getClass().getName());

        List<Triple> triples = DataCollections.DataGeneratorToList(generator);
        assertEquals(41, triples.size());
        assertEquals(1, dataSource.requestedStrings.size());
        assertEquals("https://api.gbif.org/v1/species/12345?limit=20&offset=0", dataSource.requestedStrings.get(0));
    }

    @Test
    public void testCreateDataProcessor() throws ValueException {
        DataProcessor processor = ServiceFactory.createDataProcessor(dataServiceConfiguration);

        Triple tripleToModify = new Triple("gbif:12345", "gbif:kingdom", "56789");
        boolean keepTriple = processor.postProcessTriple(tripleToModify);

        assertTrue(keepTriple);
        assertTripleHas(tripleToModify, "gbif:12345", "http://rs.tdwg.org/dwc/terms/kingdom", "56789");
    }

    @BeforeAll
    public static void setup() {
        DataServiceConfiguration configuration = new DataServiceConfiguration(
                "de.biofid.services.dummy.DummyDataSource",
                "de.biofid.services.data.generators.gbif.GbifGenericDataGenerator",
                "de.biofid.services.data.processors.PredicateMappingDataProcessor"
        );

        JSONObject parameters = new JSONObject();
        parameters.put("dataGenerator", new JSONObject("{\"ids\": [\"12345\"]}"));
        parameters.put("dataProcessor", new JSONObject(
                "{\"configurationFile\": \"src/test/resources/data/processors/genericTermMappingTestConfiguration.json\"}"
        ));
        configuration.parameters = parameters;

        dataServiceConfiguration = configuration;
    }

    private DummyDataSource getDataSource() throws FileNotFoundException {
        JSONObject apiResponse =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifPasserDomesticusApiResponse.json");

        DummyDataSource dataSource = new DummyDataSource();
        dataSource.addData(apiResponse.toString());
        return dataSource;
    }
}
