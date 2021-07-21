package de.biofid.services.data.sources.file;

import de.biofid.services.data.DataSource;
import de.biofid.services.data.converter.JSONObjectConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Reads a SQLite database as input and allows to query its data.
 */
public class SqliteFileSource implements DataSource {

    protected static final Logger logger = LogManager.getLogger();

    private static final String KEY_DATABASE_FILE_PATH = "databasePath";
    private static final String SQLITE_LOAD_PREFIX = "jdbc:sqlite:";

    private Connection sqliteConnection = null;
    private File databaseFile = null;
    private JSONObject parameters;

    @Override
    public JSONObject getDataForString(String query) throws IOException {
        setup();

        ResultSet databaseResult = queryDatabase(query);
        try {
            return JSONObjectConverter.fromSQLiteResultSet(databaseResult);
        } catch (SQLException ex) {
            String message = "Could not extract data from SQLite query \"" + query +"\"!" +
                    " Error message: " + ex.getMessage();
            logger.error(message);
            throw new IOException(message);
        }
    }

    @Override
    public void setParameters(JSONObject parameters) {
        this.parameters = parameters;
    }

    private ResultSet queryDatabase(String query) throws IOException {
        try {
            Statement statement = sqliteConnection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            String message = "Database could not process query \"" + query + "\".  Message: " + ex.getMessage();
            logger.error(message);
            throw new IOException(message);
        }
    }

    private void setup() throws IOException {
        if (sqliteConnection != null) {
            return;
        }

        String databasePath = this.parameters.optString(KEY_DATABASE_FILE_PATH, null);

        if (doesDatabaseExist(databasePath)) {
            databaseFile = new File(databasePath);
            connectToDatabase();
        } else {
            String message = "The given SQLite database \"" + databaseFile +"\" does not exist!";
            logger.error(message);
            throw new IOException(message);
        }
    }

    private void connectToDatabase() throws IOException {
        String absolutePath =databaseFile.getAbsolutePath();
        String url = SQLITE_LOAD_PREFIX + absolutePath;
        try {
            sqliteConnection = DriverManager.getConnection(url);
            logger.info("Connected to SQLite Database \"" + absolutePath + "\"");
        } catch (SQLException ex) {
            logger.error("Could not connect to SQLite database \"" + absolutePath +"\"! " +
                    "The error message is: " + ex.getMessage());
            throw new IOException("The given SQLite database \"" + absolutePath +"\" does not exist!");
        }
    }

    private boolean doesDatabaseExist(String databasePath) {
        if (databasePath == null) {
            return false;
        }

        File databaseFile = new File(databasePath);

        if (databaseFile.exists()) {
            return true;
        } else {
            logger.error("The given database \"" + databaseFile.getAbsolutePath() + "\" does not exist!");
            return false;
        }
    }
}
