package de.biofid.services.data.sources.http;

import de.biofid.services.data.DataSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHttpApi {
    @Test
    public void testRequestToUrl() throws IOException {
        DataSource httpSource = new Http();
        String response = (String) httpSource.getDataForString("https://api.gbif.org/v1/species/5231190");

        assertTrue(response.contains("\"nameKey\":8290258"));
    }
}
