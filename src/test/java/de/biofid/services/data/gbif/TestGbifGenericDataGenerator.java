package de.biofid.services.data.gbif;

import de.biofid.services.data.DataCollections;
import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import de.biofid.services.data.generators.gbif.GbifGenericDataGenerator;
import de.biofid.services.dummy.DummyDataSource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static de.biofid.services.data.DataCollections.isTripleInCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGbifGenericDataGenerator {
    private static DummyDataSource dataSource = null;

    @Test
    public void testIterator() throws FileNotFoundException {
        JSONObject apiResponseData = JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifPasserDomesticusApiResponse.json");

        DataGenerator gbifGenericGenerator = new GbifGenericDataGenerator();
        dataSource.addData(apiResponseData.toString());
        dataSource.addData(apiResponseData.toString());
        gbifGenericGenerator.setDataSource(dataSource);
        gbifGenericGenerator.setParameters(new JSONObject("{\"ids\": [1234, 5678]}"));

        List<Triple> result = DataCollections.DataGeneratorToList(gbifGenericGenerator);

        assertEquals(82, result.size());

        assertTrue(isTripleInCollection(new Triple("gbif:1234","gbif:taxonID", "gbif:5231190"), result));
        assertTrue(isTripleInCollection(new Triple("gbif:1234", "gbif:species", "Passer domesticus"), result));
        assertTrue(isTripleInCollection(new Triple("gbif:1234","gbif:synonym", false), result));

        assertTrue(isTripleInCollection(new Triple("gbif:5678","gbif:genusKey", 2492321), result));
        assertTrue(isTripleInCollection(new Triple("gbif:5678", "gbif:species", "Passer domesticus"), result));
        assertTrue(isTripleInCollection(new Triple("gbif:5678","gbif:synonym", false), result));

        assertEquals(2, dataSource.requestedStrings.size());
        assertEquals("https://api.gbif.org/v1/species/1234?limit=20&offset=0", dataSource.requestedStrings.get(0));
        assertEquals("https://api.gbif.org/v1/species/5678?limit=20&offset=0", dataSource.requestedStrings.get(1));
    }

    @Test
    public void testPaging() throws FileNotFoundException {
        DataGenerator generator = pagingSetup();

        DataCollections.DataGeneratorToList(generator);

        assertEquals(3, dataSource.requestedStrings.size());
        assertEquals("https://api.gbif.org/v1/species/1234?limit=20&offset=0", dataSource.requestedStrings.get(0));
        assertEquals("https://api.gbif.org/v1/species/1234?limit=20&offset=20", dataSource.requestedStrings.get(1));
        assertEquals("https://api.gbif.org/v1/species/5678?limit=20&offset=0", dataSource.requestedStrings.get(2));
    }

    @BeforeEach
    public void setup() {
        dataSource = new DummyDataSource();
    }

    public DataGenerator pagingSetup() throws FileNotFoundException {
        JSONObject pageableApiResponse =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifApiResponseWithPaging.json");
        JSONObject quercusChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifQuercusChildrenApiResponse.json");
        JSONObject fagusSylvaticaChildrenResponseData =
                JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifFagusSylvaticaChildrenResponse.json");

        dataSource.addData(pageableApiResponse.toString());
        dataSource.addData(quercusChildrenResponseData.toString());
        dataSource.addData(fagusSylvaticaChildrenResponseData.toString());

        DataGenerator gbifGenericGenerator = new GbifGenericDataGenerator();
        gbifGenericGenerator.setDataSource(dataSource);

        gbifGenericGenerator.setParameters(new JSONObject("{\"ids\": [1234, 5678]}"));

        return gbifGenericGenerator;
    }
}
