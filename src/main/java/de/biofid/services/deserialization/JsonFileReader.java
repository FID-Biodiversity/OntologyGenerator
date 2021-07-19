package de.biofid.services.deserialization;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JsonFileReader {
    public static JSONObject read(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        JSONTokener jsonTokener = new JSONTokener(inputStream);
        return new JSONObject(jsonTokener);
    }
}
