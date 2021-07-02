package de.biofid.services.data.processors;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPredicateMappingDataProcessor {
    @Test
    public void testPostProcessing() {
        DataProcessor processor = new PredicateMappingDataProcessor();
        JSONObject configuration = new JSONObject("{\"configurationFile\": \"src/test/resources/data/processors/genericTermMappingTestConfiguration.json\"}");
        processor.setConfiguration(configuration);

        Triple tripleToModify = new Triple("gbif:12345", "gbif:kingdom", "56789");
        boolean keepTriple = processor.postProcessTriple(tripleToModify);

        assertTrue(keepTriple);
        assertEquals("gbif:12345", tripleToModify.subject);
        assertEquals("http://rs.tdwg.org/dwc/terms/kingdom", tripleToModify.predicate);
        assertEquals("56789", tripleToModify.object);
    }
}
