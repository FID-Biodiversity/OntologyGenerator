package de.biofid.services.data.sources.file;

import de.biofid.services.data.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestSqliteFile {

    private static final String sqliteDatabasePath = "src/test/resources/data/sources/file/sqliteTest.sqlite";

    private DataSource sqliteSource;

    @Test
    public void testReadDataFromSqliteDatabase() throws IOException {
        String query = "SELECT * FROM georeferences WHERE priority < 30;";
        JSONObject responseData = (JSONObject) sqliteSource.getDataForString(query);
        JSONArray responseList = responseData.getJSONArray("results");

        assertEquals(2, responseList.length());

        JSONObject resultRow = responseList.getJSONObject(1);
        assertEquals("Berlin", resultRow.getString("text"));
        assertEquals(20, resultRow.getInt("priority"));
    }

    @BeforeEach
    public void setup() {
        JSONObject configuration = new JSONObject("{\"databasePath\": \"" + sqliteDatabasePath + "\"}");

        sqliteSource = new SqliteFileSource();
        sqliteSource.setParameters(configuration);
    }
}
