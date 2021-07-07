package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.JsonConfigurationReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testGetOntologyConfigurations() {
        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(JSON_CONFIGURATION_FILE);

        List<OntologyConfiguration> ontologyConfigurations = configuration.getOntologyConfigurations();

        assertEquals(2, ontologyConfigurations.size());

        List<String> ontologyNames = ontologyConfigurations.stream()
                .map(config -> config.ontologyName)
                .collect(Collectors.toList());
        assertTrue(ontologyNames.contains("Occurrences"));
        assertTrue(ontologyNames.contains("Systematics"));

        assertEquals(1, ontologyConfigurations.get(0).dataServiceConfigurations.size());
        assertEquals(1, ontologyConfigurations.get(1).dataServiceConfigurations.size());
    }
}
