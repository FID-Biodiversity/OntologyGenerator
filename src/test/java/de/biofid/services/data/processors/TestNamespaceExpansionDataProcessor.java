package de.biofid.services.data.processors;

import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This DataProcessor swaps the pseudo-namespace of the Triple data by real URLs.
 * This can be configured in the file config/namespaces.json .
 */
public class TestNamespaceExpansionDataProcessor {

    private static final String NAMESPACE_CONFIGURATION_TEST_FILE = "src/test/resources/configuration/namespaceTest.json";

    NamespaceExpansionDataProcessor dataProcessor;

    @Test
    public void testPostProcessTriple() {

    }

    @ParameterizedTest
    @CsvSource({"gbif-species:1234,gbif-species", "biofid:foo,biofid"})
    public void testExtractNamespaceFromString(String input, String expected) {
        assertEquals(expected, dataProcessor.extractNamespaceFromString(input));
    }

    @ParameterizedTest
    @EmptySource
    public void testExtractNamespaceFromStringWithNullAndEmptyValues(String input) {
        assertNull(dataProcessor.extractNamespaceFromString(input));
    }

    @Test
    public void testProcessTriple() {
        Triple triple = new Triple("gbif-species:1234", "biofid-terms:bar", "gbif-species:1234");
        dataProcessor.processTriple(triple);
        
        assertEquals("https://www.gbif.org/species/1234", triple.subject);
        assertEquals("https://www.biofid.de/bio-ontologies/terms/bar", triple.predicate);
        assertEquals("https://www.gbif.org/species/1234", triple.object);
    }

    @BeforeEach
    public void setup() throws FileNotFoundException {
        dataProcessor = new NamespaceExpansionDataProcessor();
        dataProcessor.readConfigurationFile(NAMESPACE_CONFIGURATION_TEST_FILE);
    }
}
