package de.biofid.services.data.sources.http;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

public class JsonHttpApi extends Http {
    @Override
    public JSONObject getDataForString(String string) throws IOException {
        String response =  callUrl(string);
        return convertStringToJsonObject(response);
    }

    @Override
    protected void setHttpConnectionParameters(HttpURLConnection httpConnection) throws ProtocolException {
        super.setHttpConnectionParameters(httpConnection);

        httpConnection.setRequestProperty("Content-Type", "application/json");
    }

    private JSONObject convertStringToJsonObject(String string) {
        InputStream inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        JSONTokener jsonTokener = new JSONTokener(inputStream);
        return new JSONObject(jsonTokener);
    }
}
