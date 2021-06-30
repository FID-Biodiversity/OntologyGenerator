package de.biofid.services.data.gbif;

import de.biofid.services.exceptions.NoGbifUriException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGbifUriFactory {
    @Test
    public void testExtractGbifIdFromUriWithBiofidUri() throws NoGbifUriException {
        String testString = "https://www.biofid.de/bio-ontology/Tracheophyta/gbif/12345";
        assertEquals("12345", GbifUriFactory.extractGbifIdFromUri(testString));
    }

    @Test
    public void testExtractGbifIdFromUriWithGbifNamespace() throws NoGbifUriException {
        String testString = "gbif:12345";
        assertEquals("12345", GbifUriFactory.extractGbifIdFromUri(testString));
    }
}
