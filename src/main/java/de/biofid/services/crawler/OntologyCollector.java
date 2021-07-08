package de.biofid.services.crawler;

import de.biofid.services.configuration.Configuration;
import de.biofid.services.configuration.OntologyConfiguration;
import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.factories.ServiceFactory;
import de.biofid.services.ontologies.OntologyGenerator;

import java.util.List;

public class OntologyCollector {

    private static final String HARVESTER_CONFIGURATION_FILE_PATH = "./config/general.json";

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(HARVESTER_CONFIGURATION_FILE_PATH);

        List<OntologyConfiguration> ontologyConfigurations = configuration.getOntologyConfigurations();
        for (OntologyConfiguration config : ontologyConfigurations) {
            OntologyGenerator generator = ServiceFactory.createOntologyGenerator(config);
            Harvester.processOntology(generator);
        }
    }
}
