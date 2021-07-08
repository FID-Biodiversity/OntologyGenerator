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

    public static OntologyConfiguration createOntologyConfiguration(String ontologyName, Configuration configuration)
            throws KeyException, ValueException {
        String serializerClassName = getClassStringFromConfiguration(Configuration.KEY_SERIALIZER,
                configuration.toJSONObject(), ontologyName);

        Serializer serializer = (Serializer) ClassLoader.createInstanceOfClassFromName(serializerClassName);

        JSONObject ontologyConfiguration = configuration.getConfigurationForOntologyName(ontologyName);
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
        String dataSourceClassName = dataServiceConfiguration.getString(Configuration.KEY_DATA_SOURCE_CLASS_NAME);
        String dataGeneratorClassName = dataServiceConfiguration.getString(Configuration.KEY_DATA_GENERATOR_CLASS_NAME);
        String dataProcessorClassName = dataServiceConfiguration.getString(Configuration.KEY_DATA_PROCESSOR_CLASS_NAME);

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
