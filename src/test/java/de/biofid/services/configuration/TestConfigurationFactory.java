package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.ConfigurationReader;
import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.exceptions.KeyException;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.factory.ConfigurationFactory;
import de.biofid.services.serialization.FileSerializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestConfigurationFactory {
    @Test
    public void testCreateDataServiceConfiguration() throws ValueException, KeyException {
        Configuration configuration = setupConfiguration();

        List<DataServiceConfiguration> dataServiceConfigurations =
                ConfigurationFactory.createDataServiceConfigurationsForOntologyName("Occurrences", configuration);

        assertEquals(1, dataServiceConfigurations.size());
    }

    @Test
    public void testCreateOntologyConfiguration() throws ValueException, KeyException {
        Configuration configuration = setupConfiguration();

        OntologyConfiguration ontologyConfiguration = ConfigurationFactory.createOntologyConfiguration("Occurrences", configuration);

        assertEquals("Occurrences", ontologyConfiguration.ontologyName);
        assertTrue(ontologyConfiguration.serializer instanceof FileSerializer);
        assertEquals(1, ontologyConfiguration.dataServiceConfigurations.size());

        DataServiceConfiguration dataServiceConfiguration = ontologyConfiguration.dataServiceConfigurations.get(0);
        assertEquals("de.biofid.services.crawler.gbif.GbifIdDataSource", dataServiceConfiguration.dataSourceClassString);
        assertEquals("de.biofid.services.crawler.gbif.GbifOccurencesGenerator", dataServiceConfiguration.dataGeneratorClassString);
        assertEquals("de.biofid.services.crawler.gbif.GbifOccurencesProcessor", dataServiceConfiguration.dataProcessorClassString);
    }

    private Configuration setupConfiguration() {
        ConfigurationReader configurationReader = new JsonConfigurationReader();
        Configuration configuration = new Configuration(configurationReader);
        configuration.loadFromFile("src/test/resources/configuration/testConfiguration.json");
        return configuration;
    }
}
