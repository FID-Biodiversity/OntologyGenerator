package de.biofid.services.crawler;

import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.OntologyGenerator;


public class Harvester {

    private static final String LAST_HARVESTING_DATE_SUBJECT = "biofid:ontology";
    private static final String LAST_HARVESTING_DATE_PREDICATE = "biofid:LastHarvestingDate";

    public static void processOntology(OntologyGenerator ontologyGenerator) {
        generateOntology(ontologyGenerator);
        addLastHarvestingDateToOntology(ontologyGenerator);
        serializeOntology(ontologyGenerator);
    }

    public static void generateOntology(OntologyGenerator ontologyGenerator) {
        ontologyGenerator.generate();
    }

    public static void serializeOntology(OntologyGenerator ontologyGenerator) {
        ontologyGenerator.serialize();
    }

    public static void addLastHarvestingDateToOntology(OntologyGenerator ontologyGenerator) {
        String currentDate = "2021-07-06";
        Triple lastHarvestingDateTriple = new Triple(LAST_HARVESTING_DATE_SUBJECT, LAST_HARVESTING_DATE_PREDICATE, currentDate);
        ontologyGenerator.addTriple(lastHarvestingDateTriple);
    }
}
