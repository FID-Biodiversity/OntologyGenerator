package de.biofid.services.configuration;

import de.biofid.services.configuration.reader.JsonConfigurationReader;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJsonConfigurationReader {

    static final String JSON_CONFIG_FILE_PATH_STRING = "src/test/resources/configuration/jsonReaderTest.json";
    static final ArrayList<String> expectedCourses = new ArrayList<>();
    static final ArrayList<Integer> expectedNumbers = new ArrayList<>();

    @Test
    @DisplayName("Reads a JSON configuration file correctly.")
    public void testReadingJsonFile() throws FileNotFoundException {
        setup();
        JsonConfigurationReader jsonConfigurationReader = new JsonConfigurationReader();
        JSONObject jsonObject = jsonConfigurationReader.readConfigurationDataFrom(JSON_CONFIG_FILE_PATH_STRING);

        assertEquals(1, jsonObject.getInt("id"));
        assertEquals("Alice", jsonObject.getString("name"));
        assertEquals(expectedCourses, jsonObject.getJSONArray("courses").toList());
        assertEquals(expectedNumbers, jsonObject.getJSONObject("objects").getJSONArray("nested").toList());
    }

    public void setup() {
        expectedCourses.clear();
        expectedNumbers.clear();

        expectedCourses.add("Engineering");
        expectedCourses.add("Finance");
        expectedCourses.add("Chemistry");

        expectedNumbers.add(1);
        expectedNumbers.add(2);
        expectedNumbers.add(3);
    }
}
