package de.biofid.services.data.converter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a dataset to a JSONObject.
 */
public class JSONObjectConverter {

    // The ResultSet metadata is not zero-based!
    private static final int RESULT_SET_META_COLUMN_STARTING_INDEX = 1;

    /**
     * Returns a JSONObject representation for the given ResultSet from a SQL query.
     * The result rows are in (in order) in an JSONArray. Per default, the key for the JSONArray is "results".
     */
    public static JSONObject fromSQLiteResultSet(ResultSet resultSet) throws SQLException {
        return fromSQLiteResultSet(resultSet, "results");
    }

    public static JSONObject fromSQLiteResultSet(ResultSet resultSet, String resultKey) throws SQLException {
        List<String> columnNames = getColumnNamesFromResultSet(resultSet);
        JSONArray convertedJsonArray = createJsonArrayFromResultSet(resultSet, columnNames);

        return new JSONObject().put(resultKey, convertedJsonArray);
    }

    private static List<String> getColumnNamesFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        int numberOfColumns = resultSetMetaData.getColumnCount();

        for (int i = RESULT_SET_META_COLUMN_STARTING_INDEX; i <= numberOfColumns; ++i) {
            columnNames.add(resultSetMetaData.getColumnLabel(i));
        }

        return columnNames;
    }

    private static JSONArray createJsonArrayFromResultSet(ResultSet resultSet, List<String> columnNames) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        while(resultSet.next()) {
            JSONObject convertedRow = createJsonObjectForRow(resultSet, columnNames);
            jsonArray.put(convertedRow);
        }

        return jsonArray;
    }

    private static JSONObject createJsonObjectForRow(ResultSet resultSet, List<String> columnNames) throws SQLException {
        JSONObject jsonData = new JSONObject();
        for (String columnName : columnNames) {
            jsonData.put(columnName, resultSet.getObject(columnName));
        }

        return jsonData;
    }
}
