package de.biofid.services.configuration;

public class DataServiceConfiguration {
    public String dataGeneratorClassString;
    public String dataProcessorClassString;
    public String dataSourceClassString;

    public DataServiceConfiguration() {}

    public DataServiceConfiguration(String dataSourceClassString, String dataGeneratorClassString, String dataProcessorClassString) {
        this.dataGeneratorClassString = dataGeneratorClassString;
        this.dataProcessorClassString = dataProcessorClassString;
        this.dataSourceClassString = dataSourceClassString;
    }
}
