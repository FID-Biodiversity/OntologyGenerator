package de.biofid.services.factories;

import de.biofid.services.configuration.DataServiceConfiguration;
import de.biofid.services.configuration.OntologyConfiguration;
import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.DataService;
import de.biofid.services.data.DataSource;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.ontologies.JenaOntology;
import de.biofid.services.ontologies.OntologyConnector;
import de.biofid.services.ontologies.OntologyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceFactory {

    public static final String DATA_GENERATOR_STRING = "dataGenerator";
    public static final String DATA_PROCESSOR_STRING = "dataProcessor";

    protected static final Logger logger = LogManager.getLogger();

    public static OntologyGenerator createOntologyGenerator(OntologyConfiguration configuration) {
        // Has to provide the DataGenerator with the appropriate Ontology
        OntologyConnector ontologyConnector = new OntologyConnector(new JenaOntology());
        List<DataService> dataServices = createDataServices(configuration.dataServiceConfigurations);

        return new OntologyGenerator(
                configuration.ontologyName, ontologyConnector, dataServices, configuration.serializer
        );
    }

    public static List<DataService> createDataServices(List<DataServiceConfiguration> configurations) {
        return configurations.stream()
                .map(ServiceFactory::createDataService)
                .collect(Collectors.toList());
    }

    public static DataService createDataService(DataServiceConfiguration configuration) {
        try {
            DataSource dataSource = createDataSource(configuration);
            DataGenerator generator = createDataGenerator(configuration, dataSource);
            DataProcessor processor = createDataProcessor(configuration);

            return new DataService(dataSource, generator, processor);
        } catch (ValueException ex) {
            logger.error(ex.getMessage());
            System.exit(1);
            return null;
        }
    }

    public static DataSource createDataSource(DataServiceConfiguration configuration) throws ValueException {
        return (DataSource) instantiateObjectFromClassNameString(configuration.dataSourceClassString);
    }

    public static DataGenerator createDataGenerator(DataServiceConfiguration configuration, DataSource dataSource)
            throws ValueException {
        DataGenerator generator = (DataGenerator) instantiateObjectFromClassNameString(configuration.dataGeneratorClassString);
        generator.setDataSource(dataSource);

        JSONObject generatorConfiguration = accessKeyOrEmptyJSON(configuration.parameters, DATA_GENERATOR_STRING);
        generator.setParameters(generatorConfiguration);

        return generator;
    }

    public static DataProcessor createDataProcessor(DataServiceConfiguration configuration) throws ValueException {
        DataProcessor processor = (DataProcessor) instantiateObjectFromClassNameString(configuration.dataProcessorClassString);

        JSONObject processorConfiguration = accessKeyOrEmptyJSON(configuration.parameters, DATA_PROCESSOR_STRING);
        processor.setParameters(processorConfiguration);

        return processor;
    }

    private static Object instantiateObjectFromClassNameString(String className) throws ValueException {
        return ClassLoader.createInstanceOfClassFromName(className);
    }

    private static JSONObject accessKeyOrEmptyJSON(JSONObject jsonObject, String key) {
        return jsonObject.has(key) ? jsonObject.getJSONObject(key) : new JSONObject();
    }
}
