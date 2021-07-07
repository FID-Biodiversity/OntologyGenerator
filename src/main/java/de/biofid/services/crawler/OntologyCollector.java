package de.biofid.services.crawler;

import de.biofid.services.configuration.Configuration;
import de.biofid.services.configuration.reader.JsonConfigurationReader;

public class OntologyCollector {

    private static final String HARVESTER_CONFIGURATION_FILE_PATH = "./configuration.json";

    public static void main(String[] args) {
        Configuration configuration = new Configuration(new JsonConfigurationReader());
        configuration.loadFromFile(HARVESTER_CONFIGURATION_FILE_PATH);


        // Use Configuration to read config file
        // Create the OntologyGenerator with its DataServices with the ServiceFactory
        // Parse the OntologyGenerator to the Harvester class to run the harvesting
        // Serialize the OntologyGenerator (if not done automatically in the Harvester)
    }
}
