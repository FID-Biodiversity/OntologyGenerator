package de.biofid.services.factories;

import de.biofid.services.configuration.DataServiceConfiguration;
import de.biofid.services.configuration.OntologyConfiguration;
import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.DataService;
import de.biofid.services.data.DataSource;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.ontologies.OntologyGenerator;
import org.json.JSONObject;

public class ServiceFactory {

    public static final String DATA_GENERATOR_STRING = "dataGenerator";
    public static final String DATA_PROCESSOR_STRING = "dataProcessor";

    public static OntologyGenerator createOntologyGenerator(OntologyConfiguration configuration) {
        // Has to provide the DataGenerator with the appropriate Ontology
        return null;
    }

    public static DataService createDataService(DataServiceConfiguration configuration) throws ValueException {
        DataSource dataSource = createDataSource(configuration);
        DataGenerator generator = createDataGenerator(configuration, dataSource);
        DataProcessor processor = createDataProcessor(configuration);

        return new DataService(dataSource, generator, processor);
    }

    public static DataSource createDataSource(DataServiceConfiguration configuration) throws ValueException {
        return (DataSource) instantiateObjectFromClassNameString(configuration.dataSourceClassString);
    }

    public static DataGenerator createDataGenerator(DataServiceConfiguration configuration, DataSource dataSource)
            throws ValueException {
        DataGenerator generator = (DataGenerator) instantiateObjectFromClassNameString(configuration.dataGeneratorClassString);
        generator.setDataSource(dataSource);

        JSONObject generatorConfiguration = configuration.parameters.getJSONObject(DATA_GENERATOR_STRING);
        generator.setParameters(generatorConfiguration);

        return generator;
    }

    public static DataProcessor createDataProcessor(DataServiceConfiguration configuration) throws ValueException {
        DataProcessor processor = (DataProcessor) instantiateObjectFromClassNameString(configuration.dataProcessorClassString);

        JSONObject processorConfiguration = configuration.parameters.getJSONObject(DATA_PROCESSOR_STRING);
        processor.setParameters(processorConfiguration);

        return processor;
    }

    private static Object instantiateObjectFromClassNameString(String className) throws ValueException {
        return ClassLoader.createInstanceOfClassFromName(className);
    }
}
