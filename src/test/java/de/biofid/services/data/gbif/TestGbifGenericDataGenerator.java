package de.biofid.services.data.gbif;

import de.biofid.services.data.DataCollections;
import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import de.biofid.services.dummy.DummyDataSource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static de.biofid.services.data.DataCollections.isTripleInList;
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

        assertTrue(isTripleInList(new Triple("gbif:1234","gbif:taxonID", "gbif:5231190"), result));
        assertTrue(isTripleInList(new Triple("gbif:1234", "gbif:species", "Passer domesticus"), result));
        assertTrue(isTripleInList(new Triple("gbif:1234","gbif:synonym", false), result));

        assertTrue(isTripleInList(new Triple("gbif:5678","gbif:genusKey", 2492321), result));
        assertTrue(isTripleInList(new Triple("gbif:5678", "gbif:species", "Passer domesticus"), result));
        assertTrue(isTripleInList(new Triple("gbif:5678","gbif:synonym", false), result));
    }

    @BeforeEach
    public void setup() {
        dataSource = new DummyDataSource();
    }
}
