package de.biofid.services.crawler;

import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.OntologyGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class Harvester {

    private static final String LAST_HARVESTING_DATE_SUBJECT = "https://www.biofid.de/bio-ontologies/terms/ontology";
    private static final String LAST_HARVESTING_DATE_PREDICATE = "https://www.biofid.de/bio-ontologies/terms/LastHarvestingDate";

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
        String currentDate = getCurrentDateAsIso();
        Triple lastHarvestingDateTriple = new Triple(LAST_HARVESTING_DATE_SUBJECT, LAST_HARVESTING_DATE_PREDICATE, currentDate);
        ontologyGenerator.addTriple(lastHarvestingDateTriple);
    }

    private static String getCurrentDateAsIso() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String text = date.format(formatter);
        return LocalDate.parse(text, formatter).toString();
    }
}
