package de.biofid.services.data.sources.http;

import de.biofid.services.data.DataSource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJsonHttpApi {

    private static String httpResponse = "";

    @Test
    public void testRequestToUrl() throws IOException {
        DataSource jsonSource = getJsonApiMock();
        JSONObject jsonResponse = (JSONObject) jsonSource.getDataForString("https://foo.example/bar");

        assertEquals(8290258, jsonResponse.getInt("nameKey"));
    }

    @BeforeAll
    static void setup() throws IOException {
        Path testFilePath = Path.of("src/test/resources/data/gbif/gbifPasserDomesticusApiResponse.json");
        TestJsonHttpApi.httpResponse = Files.readString(testFilePath);
    }

    private JsonHttpApi getJsonApiMock() throws IOException {
        JsonHttpApi jsonApiSpy = Mockito.spy(new JsonHttpApi());
        Mockito.doReturn(TestJsonHttpApi.httpResponse).when(jsonApiSpy).callUrl(Mockito.anyString());

        return jsonApiSpy;
    }
}
