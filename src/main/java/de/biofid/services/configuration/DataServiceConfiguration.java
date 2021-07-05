package de.biofid.services.configuration;

import org.json.JSONObject;

public class DataServiceConfiguration {
    public String dataGeneratorClassString;
    public String dataProcessorClassString;
    public String dataSourceClassString;
    public JSONObject parameters;

    public DataServiceConfiguration(String dataSourceClassString, String dataGeneratorClassString, String dataProcessorClassString) {
        this(dataSourceClassString, dataGeneratorClassString, dataProcessorClassString, new JSONObject());
    }

    public DataServiceConfiguration(String dataSourceClassString, String dataGeneratorClassString, String dataProcessorClassString, JSONObject parameters) {
        this.dataGeneratorClassString = dataGeneratorClassString;
        this.dataProcessorClassString = dataProcessorClassString;
        this.dataSourceClassString = dataSourceClassString;
        this.parameters = parameters;
    }
}
