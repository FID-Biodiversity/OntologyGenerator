package de.biofid.services.data.processors;

import de.biofid.services.data.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFilterDataProcessor {

    private static final String TEST_UNWANTED_PREDICATES_CONFIGURATION_FILE_PATH =
            "src/test/resources/data/processors/unwanted-predicate-configuration.json";
    private static final String TEST_DESIRED_PREDICATES_CONFIGURATION_FILE_PATH =
            "src/test/resources/data/processors/desired-predicate-configuration.json";

    private Set<String> desiredPredicates;
    private Set<String> unwantedPredicates;
    private FilterDataProcessor filterProcessor;

    @Test
    public void testShallTripleSurvive() {
        filterProcessor.setUnwantedPredicates(unwantedPredicates);
        assertShallTripleSurviveWithTestTriples();
    }

    @Test
    public void testLoadConfigurationDataFromFile() {
        filterProcessor.loadConfigurationDataFromFile(TEST_UNWANTED_PREDICATES_CONFIGURATION_FILE_PATH);
        assertShallTripleSurviveWithTestTriples();
    }

    @Test
    public void testPostProcessTripleWithUnwantedPredicates() {
        filterProcessor.loadConfigurationDataFromFile(TEST_UNWANTED_PREDICATES_CONFIGURATION_FILE_PATH);

        Triple triple = new Triple("gbif:1234", "gbif:taxonID", "gbif:1234");
        assertTrue(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:foo", "gbif:1234");
        assertFalse(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:bar", "gbif:1234");
        assertFalse(filterProcessor.postProcessTriple(triple));
    }

    @Test
    public void testPostProcessTripleWithDesiredPredicates() {
        filterProcessor.loadConfigurationDataFromFile(TEST_DESIRED_PREDICATES_CONFIGURATION_FILE_PATH);

        Triple triple = new Triple("gbif:1234", "gbif:taxonID", "gbif:1234");
        assertFalse(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:foo", "gbif:1234");
        assertTrue(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:bar", "gbif:1234");
        assertTrue(filterProcessor.postProcessTriple(triple));
    }

    @Test
    public void testPostProcessTripleWithMultiplePredicateKeywords() {
        filterProcessor.setUnwantedPredicates(unwantedPredicates);
        filterProcessor.setDesiredPredicates(desiredPredicates);

        Triple triple = new Triple("gbif:1234", "gbif:taxonID", "gbif:1234");
        assertTrue(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:foo", "gbif:1234");
        assertTrue(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:bar", "gbif:1234");
        assertFalse(filterProcessor.postProcessTriple(triple));

        triple = new Triple("gbif:1234", "gbif:notInLists", "gbif:1234");
        assertFalse(filterProcessor.postProcessTriple(triple));
    }


    @BeforeEach
    public void setup() {
        filterProcessor = new FilterDataProcessor();

        unwantedPredicates = new HashSet<>();
        unwantedPredicates.add("gbif:foo");
        unwantedPredicates.add("gbif:bar");

        desiredPredicates = new HashSet<>();
        desiredPredicates.add("gbif:taxonID");
        desiredPredicates.add("gbif:foo");
    }

    private void assertShallTripleSurviveWithTestTriples() {
        Triple triple = new Triple("gbif:1234", "gbif:taxonID", "gbif:1234");
        assertTrue(filterProcessor.shallTripleSurvive(triple));

        triple = new Triple("gbif:1234", "gbif:foo", "gbif:1234");
        assertFalse(filterProcessor.shallTripleSurvive(triple));

        triple = new Triple("gbif:1234", "gbif:bar", "gbif:1234");
        assertFalse(filterProcessor.shallTripleSurvive(triple));
    }
}
