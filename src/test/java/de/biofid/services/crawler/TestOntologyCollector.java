package de.biofid.services.crawler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestOntologyCollector {
    @Test
    public void testOntologyGeneration() {
        assertDoesNotThrow(OntologyCollector::start);
    }
}
