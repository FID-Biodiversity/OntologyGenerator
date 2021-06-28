package de.biofid.services.data;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JsonDataReader {
    public static JSONObject readJSONObjectFromFile(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        JSONTokener jsonTokener = new JSONTokener(inputStream);
        return new JSONObject(jsonTokener);
    }
}
