package de.biofid.services.data;

import org.json.JSONObject;

import java.util.NoSuchElementException;


/**
 * A DataGenerator calls a DataSource and is aware of the items to call.
 * To get the data from the DataGenerator, the iterator() method will be used.
 * All set methods will be called for the object, handing data to modify behaviour.
 * The setParameters(JsonObject) method will receive the user configuration.
 */
public interface DataGenerator {
    abstract public Triple next() throws NoSuchElementException;
    abstract public void setDataSource(DataSource dataSource);
    abstract public void setParameters(JSONObject parameters);
}
