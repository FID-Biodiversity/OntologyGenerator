package de.biofid.services.configuration;

import de.biofid.services.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

public class OntologyConfiguration {

    public String ontologyName = "";
    public Serializer serializer = null;
    public List<DataServiceConfiguration> dataServiceConfigurations = new ArrayList<>();

    public OntologyConfiguration() {}

    public OntologyConfiguration(String ontologyName, Serializer serializer, List<DataServiceConfiguration> dataServiceConfigurations) {
        this.ontologyName = ontologyName;
        this.serializer = serializer;
        this.dataServiceConfigurations = dataServiceConfigurations;
    }
}
