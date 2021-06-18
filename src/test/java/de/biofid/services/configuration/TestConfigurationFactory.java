package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.ConfigurationReader;
import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.exceptions.KeyException;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.factory.ConfigurationFactory;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestConfigurationFactory {
    @Test
    public void testCreateDataServiceConfiguration() throws ValueException, KeyException {
        Configuration configuration = setupConfiguration();

        JSONArray services = configuration.getJsonArray("ontologies")
                .getJSONObject(0)
                .getJSONObject("Occurrences")
                .getJSONArray("services");
        assertTrue(services.length() > 0);

        List<DataServiceConfiguration> dataServiceConfigurations =
                ConfigurationFactory.createDataServiceConfigurationForOntologyName("Occurrences", configuration);

        assertEquals(1, dataServiceConfigurations.size());
    }

    private Configuration setupConfiguration() {
        ConfigurationReader configurationReader = new JsonConfigurationReader();
        Configuration configuration = new Configuration(configurationReader);
        configuration.loadFromFile("src/test/resources/configuration/testConfiguration.json");
        return configuration;
    }
}
