package de.biofid.services.crawler;

import de.biofid.services.configuration.Configuration;
import de.biofid.services.configuration.OntologyConfiguration;
import de.biofid.services.configuration.reader.JsonConfigurationReader;
import de.biofid.services.factories.ServiceFactory;
import de.biofid.services.ontologies.OntologyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OntologyCollector {

    protected static final Logger logger = LogManager.getLogger();

    private static final String HARVESTER_CONFIGURATION_FILE_PATH = "config/general.json";

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        logger.info("Starting Ontology Collector!");
        logger.info("Looking for configuration data in \"" + HARVESTER_CONFIGURATION_FILE_PATH + "\".");

        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(HARVESTER_CONFIGURATION_FILE_PATH);

        List<OntologyConfiguration> ontologyConfigurations = configuration.getOntologyConfigurations();
        for (OntologyConfiguration config : ontologyConfigurations) {
            OntologyGenerator generator = ServiceFactory.createOntologyGenerator(config);
            Harvester.processOntology(generator);
        }
    }
}
