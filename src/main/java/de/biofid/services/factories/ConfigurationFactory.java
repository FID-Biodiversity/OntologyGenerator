package de.biofid.services.factories;

import de.biofid.services.configuration.Configuration;
import de.biofid.services.configuration.DataServiceConfiguration;
import de.biofid.services.configuration.OntologyConfiguration;
import de.biofid.services.exceptions.KeyException;
import de.biofid.services.exceptions.ValueException;
import de.biofid.services.serialization.Serializer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationFactory {
    private static final String KEY_OUTPUT_SINK = "output";

    private static final String DATA_SOURCE_DEFAULT_CLASS = "de.biofid.services.data.sources.EmptyDataSource";
    private static final String DATA_GENERATOR_DEFAULT_CLASS = "de.biofid.services.data.generators.OntologyDataGenerator";
    private static final String DATA_PROCESSOR_DEFAULT_CLASS = "de.biofid.services.data.processor.EmptyDataProcessor";

    public static OntologyConfiguration createOntologyConfiguration(String ontologyName, Configuration configuration)
            throws KeyException, ValueException {

        JSONObject ontologyConfiguration = configuration.getConfigurationForOntologyName(ontologyName);
        String serializerClassName = getClassStringFromConfiguration(Configuration.KEY_SERIALIZER,
                ontologyConfiguration, ontologyName);

        Serializer serializer = (Serializer) ClassLoader.createInstanceOfClassFromName(serializerClassName);

        if (ontologyConfiguration.has(KEY_OUTPUT_SINK)) {
            serializer.addSink((String) ontologyConfiguration.get(KEY_OUTPUT_SINK));
        }

        List<DataServiceConfiguration> dataServiceConfigurations =
                createDataServiceConfigurationsForOntologyName(ontologyName, configuration);

        return new OntologyConfiguration(ontologyName, serializer, dataServiceConfigurations);
    }

    public static List<DataServiceConfiguration> createDataServiceConfigurationsForOntologyName(
            String ontologyName, Configuration configuration) throws KeyException {
        JSONObject ontologyConfiguration = configuration.getConfigurationForOntologyName(ontologyName);
        JSONArray dataServiceConfigurationsAsJson = ontologyConfiguration.getJSONArray(Configuration.KEY_DATA_SERVICE_CONFIGURATIONS);

        List<DataServiceConfiguration> dataServiceConfigurations = new ArrayList<>(dataServiceConfigurationsAsJson.length());
        for (Object dataServiceObject : dataServiceConfigurationsAsJson) {
            JSONObject dataServiceConfiguration = (JSONObject) dataServiceObject;
            dataServiceConfigurations.add(createDataServiceConfigurationObjectFromJSONObject(dataServiceConfiguration));
        }

        return dataServiceConfigurations;
    }

    public static DataServiceConfiguration createDataServiceConfigurationObjectFromJSONObject(JSONObject dataServiceConfiguration) {
        return createDataServiceConfigurationObjectFromJSONObject(dataServiceConfiguration,
                DATA_SOURCE_DEFAULT_CLASS, DATA_GENERATOR_DEFAULT_CLASS, DATA_PROCESSOR_DEFAULT_CLASS);
    }

    public static DataServiceConfiguration createDataServiceConfigurationObjectFromJSONObject(
            JSONObject dataServiceConfiguration, String dataSourceClassDefault, String dataGeneratorClassDefault,
            String dataProcessorClassDefault) {
        String dataSourceClassName = dataServiceConfiguration.optString(Configuration.KEY_DATA_SOURCE_CLASS_NAME, dataSourceClassDefault);
        String dataGeneratorClassName = dataServiceConfiguration.optString(Configuration.KEY_DATA_GENERATOR_CLASS_NAME, dataGeneratorClassDefault);
        String dataProcessorClassName = dataServiceConfiguration.optString(Configuration.KEY_DATA_PROCESSOR_CLASS_NAME, dataProcessorClassDefault);

        JSONObject dataServiceParameters;
        if (dataServiceConfiguration.has(Configuration.KEY_DATA_SERVICE_PARAMETERS)) {
            dataServiceParameters = dataServiceConfiguration.getJSONObject(Configuration.KEY_DATA_SERVICE_PARAMETERS);
        } else {
            dataServiceParameters = new JSONObject();
        }

        return new DataServiceConfiguration(dataSourceClassName, dataGeneratorClassName, dataProcessorClassName, dataServiceParameters);
    }

    private static String getClassStringFromConfiguration(String key, JSONObject configuration, String ontologyName) throws KeyException {
        String serializerClass = (String) accessKeyInConfiguration(key, configuration, "");

        if (serializerClass.equals("")) {
            throw new KeyException("The configuration of the ontology \"" + ontologyName +"\" is missing a \"serializer\" keyword!");
        }

        return serializerClass;
    }

    private static Object accessKeyInConfiguration(String key, JSONObject configuration, Object defaultReturnValue) {
        try {
            return configuration.get(key);
        } catch (JSONException e) {
            return defaultReturnValue;
        }
    }
}
