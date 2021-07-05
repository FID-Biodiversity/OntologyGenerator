package de.biofid.services.data.processors;

import de.biofid.services.data.DataProcessor;
import de.biofid.services.data.Triple;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static de.biofid.services.data.TripleAssertions.assertTripleHas;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPredicateMappingDataProcessor {
    @Test
    public void testPostProcessing() {
        DataProcessor processor = new PredicateMappingDataProcessor();
        JSONObject configuration = new JSONObject("{\"configurationFile\": \"src/test/resources/data/processors/genericTermMappingTestConfiguration.json\"}");
        processor.setParameters(configuration);

        Triple tripleToModify = new Triple("gbif:12345", "gbif:kingdom", "56789");
        boolean keepTriple = processor.postProcessTriple(tripleToModify);

        assertTrue(keepTriple);
        assertTripleHas(tripleToModify, "gbif:12345", "http://rs.tdwg.org/dwc/terms/kingdom", "56789");
    }

    @Test
    public void testAddingAdditionalConfigurationData() {
        DataProcessor processor = new PredicateMappingDataProcessor();
        JSONObject configuration = new JSONObject();
        configuration.put("gbif:kingdom", "http://rs.tdwg.org/dwc/terms/kingdom");
        processor.setParameters(configuration);

        Triple tripleToModify = new Triple("gbif:12345", "gbif:kingdom", "56789");
        boolean keepTriple = processor.postProcessTriple(tripleToModify);

        assertTrue(keepTriple);
        assertTripleHas(tripleToModify, "gbif:12345", "http://rs.tdwg.org/dwc/terms/kingdom", "56789");
    }

}
