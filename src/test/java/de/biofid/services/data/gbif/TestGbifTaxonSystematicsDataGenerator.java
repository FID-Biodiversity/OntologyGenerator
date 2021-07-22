package de.biofid.services.data.gbif;

import de.biofid.services.data.Triple;
import de.biofid.services.data.generators.gbif.GbifTaxonSystematicsDataGenerator;
import de.biofid.services.dummy.DummyOntology;
import de.biofid.services.ontologies.Ontology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestGbifTaxonSystematicsDataGenerator {

    GbifTaxonSystematicsDataGenerator generator;

    @Test
    public void testCreateGbifApiUrlFromGbifId() {
        assertEquals("https://api.gbif.org/v1/species/12345?limit=20&offset=0",
                generator.createGbifApiUrlFromGbifId("12345"));
    }

    @Test
    public void testSetSystematicProperties() throws FileNotFoundException {
        String configurationFilePath = "src/test/resources/configuration/gbif-systematics-properties.json";
        Set<String> systematicProperties = generator.readSystematicPropertiesFromFile(configurationFilePath);
        generator.setSystematicProperties(systematicProperties);

        Triple triple = new Triple("gbif:1234", "gbif:phylumKey", "gbif:4567");
        assertTrue(generator.hasSystematicsProperty(triple));

        triple = new Triple("gbif:1234", "gbif:foo", "gbif:4567");
        assertFalse(generator.hasSystematicsProperty(triple));
    }

    @BeforeEach
    public void setup() throws FileNotFoundException {
        generator = new GbifTaxonSystematicsDataGenerator();
        generator.clearSystematicProperties();
    }
}
