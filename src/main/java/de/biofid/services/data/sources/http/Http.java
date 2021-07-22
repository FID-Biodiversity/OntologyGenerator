package de.biofid.services.data.sources.http;

import de.biofid.services.data.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Calls any given URL and returns the response.
 */
public class Http implements DataSource {

    @Override
    public Object getDataForString(String string) throws IOException {
        return callUrl(string);
    }

    protected String callUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        setHttpConnectionParameters(httpConnection);
        return readResponse(httpConnection);
    }

    protected void setHttpConnectionParameters(HttpURLConnection httpConnection) throws ProtocolException {
        httpConnection.setRequestMethod("GET");

        int timeoutInSeconds = 5;
        int timeoutInMilliseconds = timeoutInSeconds * 1000;
        httpConnection.setConnectTimeout(timeoutInMilliseconds);
        httpConnection.setReadTimeout(timeoutInMilliseconds);
    }

    private String readResponse(HttpURLConnection httpConnection) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpConnection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        httpConnection.disconnect();

        return content.toString();
    }
}
