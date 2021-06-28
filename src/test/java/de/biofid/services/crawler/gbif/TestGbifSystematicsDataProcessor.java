package de.biofid.services.crawler.gbif;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.JsonDataReader;
import de.biofid.services.data.Triple;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class TestGbifSystematicsDataProcessor {
    @Test
    public void testMapDataToTriple() throws FileNotFoundException {
        JSONObject apiResponseData = JsonDataReader.readJSONObjectFromFile("src/test/resources/data/gbif/gbifSpeciesApiResponse.json");
        //DataProcessor gbifSpeciesProcessor = new GbifSystematicsDataProcessor();

        //List<Triple> triples = gbifSpeciesProcessor.mapDataToTriple(apiResponseData, null);


    }
}
