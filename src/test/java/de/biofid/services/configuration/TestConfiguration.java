package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.exceptions.KeyException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestConfiguration {

    String JSON_TEST_FILE = "src/test/resources/configuration/jsonReaderTest.json";
    String JSON_CONFIGURATION_FILE = "src/test/resources/configuration/testConfiguration.json";

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

    @Test
    public void testGetOntologyConfigurations() throws KeyException {
        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(JSON_CONFIGURATION_FILE);

        List<OntologyConfiguration> ontologyConfigurations = configuration.getOntologyConfigurations();

        assertEquals(2, ontologyConfigurations.size());

        List<String> ontologyNames = ontologyConfigurations.stream()
                .map(config -> config.ontologyName)
                .collect(Collectors.toList());
        assertTrue(ontologyNames.contains("Occurrences"));
        assertTrue(ontologyNames.contains("Systematics"));

        JSONObject occurrencesOntologyConfiguration = configuration.getJSONConfigurationForOntologyName("Occurrences");
        assertNumberOfDataServicesInConfiguration(occurrencesOntologyConfiguration, 2);

        JSONObject systematicsOntologyConfiguration = configuration.getJSONConfigurationForOntologyName("Systematics");
        assertNumberOfDataServicesInConfiguration(systematicsOntologyConfiguration, 1);
    }

    private void assertNumberOfDataServicesInConfiguration(JSONObject configuration, int expectedNumberOfDataServices) {
        assertEquals(expectedNumberOfDataServices, configuration.getJSONArray("services").length());
    }
}
