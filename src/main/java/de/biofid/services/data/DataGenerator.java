package de.biofid.services.data;

import de.biofid.services.ontologies.Ontology;
import org.json.JSONObject;

import java.util.NoSuchElementException;


/**
 * A DataGenerator calls a DataSource and is aware of the items to call.
 * To get the data from the DataGenerator, the iterator() method will be used.
 * All set methods will be called for the object, handing data to modify behaviour.
 * The setParameters(JsonObject) method will receive the user configuration.
 */
public interface DataGenerator {
    Triple next() throws NoSuchElementException;
    void setDataSource(DataSource dataSource);
    void setParameters(JSONObject parameters);
    default void setOntology(Ontology ontology) {};
}
