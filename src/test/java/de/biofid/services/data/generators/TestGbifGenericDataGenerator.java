package de.biofid.services.data.generators;

import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import de.biofid.services.dummy.DummyDataSource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGbifGenericDataGenerator {
    private static DummyDataSource dataSource = null;

    @Test
    public void testIterator() throws FileNotFoundException {
        JSONObject apiResponseData = JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifSpeciesApiResponse.json");

        DataGenerator gbifGenericGenerator = new GbifGenericDataGenerator();
        dataSource.addData(apiResponseData.toString());
        dataSource.addData(apiResponseData.toString());
        gbifGenericGenerator.setDataSource(dataSource);
        gbifGenericGenerator.setParameters(new JSONObject("{\"ids\": [1234, 5678]}"));

        List<Triple> result = toList(gbifGenericGenerator);

        assertEquals(82, result.size());

        //Triple tripleFromGbifId1234 = result.get()
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

    private List<Triple> toList(DataGenerator dataGenerator) {
        List<Triple> triples = new ArrayList<>();
        try {
            while (true) {
                triples.add(dataGenerator.next());
            }
        } catch (NoSuchElementException ex) {}

        return triples;
    }

    private boolean isTripleInList(Triple triple, List<Triple> triples) {
        return triples.stream()
                .filter(obj -> obj.subject.equals(triple.subject)
                        && obj.predicate.equals(triple.predicate)
                        && obj.object.equals(triple.object))
                .count() == 1;
    }
}
