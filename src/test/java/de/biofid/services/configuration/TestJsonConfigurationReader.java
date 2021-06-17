package de.biofid.services.configuration;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestJsonConfigurationReader {

    static final String JSON_CONFIG_FILE_PATH_STRING = "src/test/resources/configuration/jsonReaderTest.json";
    ArrayList<String> expectedCourses = new ArrayList<>();
    ArrayList<Integer> expectedNumbers = new ArrayList<>();

    @Test
    public void testReadingJsonFile() throws FileNotFoundException {
        JsonConfigurationReader jsonConfigurationReader = new JsonConfigurationReader();
        JSONObject jsonObject = jsonConfigurationReader.readConfigurationFile(JSON_CONFIG_FILE_PATH_STRING);

        assertEquals(1, jsonObject.getInt("id"));
        assertEquals("Alice", jsonObject.getString("name"));
        assertEquals(expectedCourses, jsonObject.getJSONArray("courses").toList());
        assertEquals(expectedNumbers, jsonObject.getJSONObject("objects").getJSONArray("nested").toList());
    }

    @Before
    public void setup() {
        expectedCourses.add("Engineering");
        expectedCourses.add("Finance");
        expectedCourses.add("Chemistry");

        expectedNumbers.add(1);
        expectedNumbers.add(2);
        expectedNumbers.add(3);
    }
}
