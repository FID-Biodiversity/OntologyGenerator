package de.biofid.services.data.gbif;

import de.biofid.services.data.DataCollections;
import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import de.biofid.services.data.gbif.generators.GbifDirectTaxonChildDataGenerator;
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

public class TestGbifDirectTaxonChildDataGenerator {

    private DummyDataSource dummyDataSource = new DummyDataSource();
    private DummyOntology dummyOntology = new DummyOntology();
    private DataGenerator directTaxonChildDataGenerator;

    @Test
    public void testNext() throws FileNotFoundException {
        defaultSetup();

        List<Triple> triples = DataCollections.DataGeneratorToList(directTaxonChildDataGenerator);

        assertTrue(isTripleInList(new Triple("gbif:7984102","gbif:taxonID", "gbif:7984102"), triples));
        assertTrue(isTripleInList(new Triple("gbif:4928315","gbif:taxonID", "gbif:4928315"), triples));
        assertTrue(isTripleInList(new Triple("gbif:3906420","gbif:taxonID", "gbif:3906420"), triples));
        assertTrue(isTripleInList(new Triple("gbif:3906414","gbif:taxonID", "gbif:3906414"), triples));
        assertTrue(isTripleInList(new Triple("gbif:3906351","gbif:taxonID", "gbif:3906351"), triples));

        assertEquals(2, dummyDataSource.requestedStrings.size());
        assertEquals("https://api.gbif.org/v1/species/12345/children?limit=20&offset=0", dummyDataSource.requestedStrings.get(0));
        assertEquals("https://api.gbif.org/v1/species/56789/children?limit=20&offset=0", dummyDataSource.requestedStrings.get(1));
    }

    @Test
    public void testNextWithPaging() throws FileNotFoundException {
        pagingSetup();

        List<Triple> triples = DataCollections.DataGeneratorToList(directTaxonChildDataGenerator);

        assertTrue(isTripleInList(new Triple("gbif:4905659","gbif:genusKey", 4905659), triples));
        assertTrue(isTripleInList(new Triple("gbif:3906414","gbif:genusKey", 2877951), triples));
        assertTrue(isTripleInList(new Triple("gbif:7984102","gbif:genusKey", 2874875), triples));
        assertTrue(isTripleInList(new Triple("gbif:4928315","gbif:genusKey", 2874875), triples));

        assertEquals(3, dummyDataSource.requestedStrings.size());
        assertEquals("https://api.gbif.org/v1/species/12345/children?limit=20&offset=0", dummyDataSource.requestedStrings.get(0));
        assertEquals("https://api.gbif.org/v1/species/12345/children?limit=20&offset=20", dummyDataSource.requestedStrings.get(1));
        assertEquals("https://api.gbif.org/v1/species/56789/children?limit=20&offset=0", dummyDataSource.requestedStrings.get(2));
    }

    public void defaultSetup() throws FileNotFoundException {

        directTaxonChildDataGenerator = new GbifDirectTaxonChildDataGenerator();
        directTaxonChildDataGenerator.setOntology(dummyOntology);
        directTaxonChildDataGenerator.setParameters(new JSONObject("{\"ids\": [1234]}"));

        JSONObject fagusSylvaticaChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifFagusSylvaticaChildrenResponse.json");
        JSONObject quercusChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifQuercusChildrenApiResponse.json");

        dummyDataSource.addData(fagusSylvaticaChildrenResponseData.toString());
        dummyDataSource.addData(quercusChildrenResponseData.toString());

        directTaxonChildDataGenerator.setDataSource(dummyDataSource);

        String[] uris = {"https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/12345", "gbif:56789"};
        dummyOntology.setResourceUriIterator(Arrays.stream(uris).iterator());
    }

    public void pagingSetup() throws FileNotFoundException {
        directTaxonChildDataGenerator = new GbifDirectTaxonChildDataGenerator();
        directTaxonChildDataGenerator.setOntology(dummyOntology);
        directTaxonChildDataGenerator.setParameters(new JSONObject("{\"ids\": [1234]}"));

        JSONObject pageableApiResponse =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifApiResponseWithPaging.json");
        JSONObject quercusChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifQuercusChildrenApiResponse.json");
        JSONObject fagusSylvaticaChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifFagusSylvaticaChildrenResponse.json");

        dummyDataSource.addData(pageableApiResponse.toString());
        dummyDataSource.addData(quercusChildrenResponseData.toString());
        dummyDataSource.addData(fagusSylvaticaChildrenResponseData.toString());

        directTaxonChildDataGenerator.setDataSource(dummyDataSource);

        String[] uris = {"https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/12345", "gbif:56789"};
        dummyOntology.setResourceUriIterator(Arrays.stream(uris).iterator());
    }
}
