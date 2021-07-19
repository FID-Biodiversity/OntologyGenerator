package de.biofid.services.data;

import de.biofid.services.ontologies.Ontology;
import org.json.JSONObject;

import java.util.NoSuchElementException;


/**
 * A DataGenerator calls a DataSource and is aware of the items to call.
 * All set methods will be called for the object, handing data to modify behaviour.
 */
public interface DataGenerator {
    /** This method will be called from the to get the next Triple. */
    Triple next() throws NoSuchElementException;

    /** This method sets the DataSource that shall be used for the DataGenerator. */
    void setDataSource(DataSource dataSource);

    /** This method will receive the user configuration for this DataGenerator  as defined in the config file. */
    void setParameters(JSONObject parameters);

    /** This method will receive the current state of the generated ontology. */
    default void setOntology(Ontology ontology) {};
}
