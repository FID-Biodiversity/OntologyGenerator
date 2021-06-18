package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.JsonConfigurationReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConfiguration {

    String JSON_TEST_FILE = "src/test/resources/configuration/jsonReaderTest.json";

    @Test
    public void testGetJsonObjectFromConfiguration() throws Exception {
        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(JSON_TEST_FILE);
        assertEquals("{\"also-nested\":[4,5,6],\"nested\":[1,2,3]}",
                configuration.getJsonObject("objects").toString());
    }

    @Test
    public void testGetJsonArrayFromConfiguration() throws Exception {
        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(JSON_TEST_FILE);
        assertEquals("[\"Engineering\",\"Finance\",\"Chemistry\"]",
                configuration.getJsonArray("courses").toString());
    }
}
