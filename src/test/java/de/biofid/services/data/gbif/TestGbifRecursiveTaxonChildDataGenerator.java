package de.biofid.services.data.gbif;

import de.biofid.services.data.DataCollections;
import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import de.biofid.services.data.generators.gbif.GbifRecursiveTaxonChildDataGenerator;
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

public class TestGbifRecursiveTaxonChildDataGenerator {

    DummyDataSource dataSource = new DummyDataSource();

    @Test
    public void testNext() throws FileNotFoundException {
        DataGenerator generator = defaultSetup();

        List<Triple> triples = DataCollections.DataGeneratorToList(generator);

        assertTrue(isTripleInList(new Triple("gbif:4928315","gbif:genusKey", 2874875), triples));
        assertTrue(isTripleInList(new Triple("gbif:4980947","gbif:genusKey", 4901450), triples));

        assertEquals(3, dataSource.requestedStrings.size());
        assertEquals("https://api.gbif.org/v1/species/12345/children?limit=20&offset=0", dataSource.requestedStrings.get(0));
        assertEquals("https://api.gbif.org/v1/species/7984102/children?limit=20&offset=0", dataSource.requestedStrings.get(1));
        assertEquals("https://api.gbif.org/v1/species/4980947/children?limit=20&offset=0", dataSource.requestedStrings.get(2));
    }

    public DataGenerator defaultSetup() throws FileNotFoundException {

        DataGenerator dataGenerator = new GbifRecursiveTaxonChildDataGenerator();
        DummyOntology dummyOntology = new DummyOntology();
        dataGenerator.setOntology(dummyOntology);

        JSONObject fagusSylvaticaChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifFagusSylvaticaChildrenResponse.json");
        JSONObject quercusChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifRecursiveChildrenApiResponse.json");

        dataSource = new DummyDataSource();
        dataSource.addData(fagusSylvaticaChildrenResponseData.toString());
        dataSource.addData(quercusChildrenResponseData.toString());

        dataGenerator.setDataSource(dataSource);
        dataGenerator.setParameters(new JSONObject("{\"ids\": [1234]}"));

        String[] uris = {"https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/12345"};
        dummyOntology.setResourceUriIterator(Arrays.stream(uris).iterator());

        return dataGenerator;
    }
}
