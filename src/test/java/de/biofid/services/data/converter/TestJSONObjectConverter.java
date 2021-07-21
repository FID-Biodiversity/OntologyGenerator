package de.biofid.services.data.converter;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJSONObjectConverter {
    @Test
    public void testFromResultSet() throws SQLException {
        ResultSet resultSet = getTestResultSet();
        JSONObject expectedJson = new JSONObject("{\"results\":[" +
                "{\"text\": \"Frankfurt\", \"priority\": 10}," +
                "{\"text\": \"Berlin\", \"priority\": 20}," +
                "{\"text\": \"München\", \"priority\": 30}]}");

        JSONObject resultJson = JSONObjectConverter.fromSQLiteResultSet(resultSet, "results");

        assertEquals(expectedJson.toString(), resultJson.toString());
    }

    private ResultSet getTestResultSet() throws SQLException {
        Connection sqliteConnection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/data/sources/file/sqliteTest.sqlite");
        Statement statement = sqliteConnection.createStatement();
        return statement.executeQuery("SELECT * FROM georeferences");
    }
}
