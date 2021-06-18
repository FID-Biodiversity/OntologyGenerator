package de.biofid.services.configuration;

import de.biofid.services.exceptions.ValueException;
import de.biofid.services.serialization.Serializer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OntologyConfiguration {

    String ontologyName = "";
    Serializer serializer = null;
    List<DataServiceConfiguration> dataServiceConfigurations = new ArrayList<>();

    public OntologyConfiguration() {}

    public OntologyConfiguration(String ontologyName, Serializer serializer, List<DataServiceConfiguration> dataServiceConfigurations) {
        this.ontologyName = ontologyName;
        this.serializer = serializer;
        this.dataServiceConfigurations = dataServiceConfigurations;
    }
}
