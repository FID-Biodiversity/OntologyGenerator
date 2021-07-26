package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.ConfigurationReader;
import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.exceptions.KeyException;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.factories.ClassLoader;
import de.biofid.services.factories.ConfigurationFactory;
import de.biofid.services.factories.ServiceFactory;
import de.biofid.services.serialization.FileSerializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestConfigurationFactory {
    @Test
    public void testCreateDataServiceConfiguration() throws KeyException {
        Configuration configuration = setupConfiguration();

        List<DataServiceConfiguration> dataServiceConfigurations =
                ConfigurationFactory.createDataServiceConfigurationsForOntologyName("Systematics", configuration);

        assertEquals(1, dataServiceConfigurations.size());
    }

    @Test
    public void testCreateOntologyConfiguration() throws ValueException, KeyException {
        Configuration configuration = setupConfiguration();

        OntologyConfiguration ontologyConfiguration = ConfigurationFactory.createOntologyConfiguration(
                "Occurrences", configuration);

        assertEquals("Occurrences", ontologyConfiguration.ontologyName);
        assertTrue(ontologyConfiguration.serializer instanceof FileSerializer);
        assertEquals(2, ontologyConfiguration.dataServiceConfigurations.size());

        DataServiceConfiguration dataServiceConfiguration = ontologyConfiguration.dataServiceConfigurations.get(0);
        assertDataServiceClassNames(dataServiceConfiguration,
                "de.biofid.services.data.sources.http.JsonHttpApi",
                "de.biofid.services.data.generators.gbif.GbifGenericDataGenerator",
                "de.biofid.services.data.processors.FilterDataProcessor"
                );
    }

    @Test
    public void testDefaultDataServiceClasses() throws ValueException, KeyException {
        Configuration configuration = setupConfiguration();

        OntologyConfiguration ontologyConfiguration = ConfigurationFactory.createOntologyConfiguration(
                "Occurrences", configuration);

        DataServiceConfiguration dataServiceConfiguration = ontologyConfiguration.dataServiceConfigurations.get(1);
        assertDataServiceClassNames(dataServiceConfiguration,
                "de.biofid.services.data.sources.EmptyDataSource",
                "de.biofid.services.data.generators.OntologyDataGenerator",
                "de.biofid.services.data.processors.PredicateMappingDataProcessor");

        assertDataServicesAreInstantiable(dataServiceConfiguration);
    }

    @Test
    public void testDefaultInstantiation() throws KeyException {
        Configuration configuration = setupConfiguration();
        OntologyConfiguration ontologyConfiguration = configuration.getOntologyConfigurationForOntologyName("Occurrences");
        assertDoesNotThrow(() -> ServiceFactory.createOntologyGenerator(ontologyConfiguration));
    }

    private void assertDataServiceClassNames(DataServiceConfiguration dataServiceConfiguration,
                                             String dataSourceClassString, String dataGeneratorClassString,
                                             String dataProcessorClassString) {
        assertEquals(dataSourceClassString, dataServiceConfiguration.dataSourceClassString);
        assertEquals(dataGeneratorClassString, dataServiceConfiguration.dataGeneratorClassString);
        assertEquals(dataProcessorClassString, dataServiceConfiguration.dataProcessorClassString);
    }

    private void assertDataServicesAreInstantiable(DataServiceConfiguration dataServiceConfiguration) throws ValueException {
        assertNotNull(ClassLoader.createInstanceOfClassFromName(dataServiceConfiguration.dataSourceClassString));
        assertNotNull(ClassLoader.createInstanceOfClassFromName(dataServiceConfiguration.dataGeneratorClassString));
        assertNotNull(ClassLoader.createInstanceOfClassFromName(dataServiceConfiguration.dataProcessorClassString));
    }

    private Configuration setupConfiguration() {
        ConfigurationReader configurationReader = new JsonConfigurationReader();
        Configuration configuration = new Configuration(configurationReader);
        configuration.loadFromFile("src/test/resources/configuration/testConfiguration.json");
        return configuration;
    }
}
