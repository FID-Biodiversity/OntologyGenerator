package de.biofid.services.data.generators;

import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataSource;
import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.Ontology;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class simply returns the Triple objects that are in the Ontology at the time of calling.
 */
public class OntologyDataGenerator implements DataGenerator {

    private Iterator<Triple> tripleIterator = Collections.emptyIterator();
    private Ontology ontology;
    private boolean isSetup = false;

    @Override
    public Triple next() throws NoSuchElementException {
        if (!isSetup) {
            setup();
        }

        return tripleIterator.next();
    }

    @Override
    public void setDataSource(DataSource dataSource) {}

    @Override
    public void setParameters(JSONObject parameters) {}

    @Override
    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    private void setup() {
        tripleIterator = ontology != null ? ontology.iterateTriples() : Collections.emptyIterator();
        isSetup = true;
    }
}
