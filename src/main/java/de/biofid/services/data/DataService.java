package de.biofid.services.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * A DataService orchestrates the calling of DataSource, DataGenerator, and DataProcessor, in this order.
 */
public class DataService {

    private static final Logger logger = LogManager.getLogger();

    private final DataSource dataSource;
    private final DataGenerator dataGenerator;
    private final DataProcessor dataProcessor;

    private final Set<Triple> triplesToRemove = new HashSet<>();
    private final Set<Triple> triplesToAdd = new HashSet<>();

    private boolean isComplete = false;

    public DataService(DataSource dataSource, DataGenerator dataGenerator, DataProcessor dataProcessor) {
        this.dataSource = dataSource;
        this.dataGenerator = dataGenerator;
        this.dataProcessor = dataProcessor;

        setup();
    }

    public Set<Triple> getTriplesToAdd() {
        if (!isComplete) {
            updateTriples();
        }

        return triplesToAdd;
    }

    public Set<Triple> getTriplesToRemove() {
        if (!isComplete) {
            updateTriples();
        }

        return triplesToRemove;
    }

    private void updateTriples() {
        Triple triple;
        do {
            triple = getNextTriple();
            if (triple != null) {
                boolean keepTriple = dataProcessor.postProcessTriple(triple);
                updateTriples(triple, keepTriple);
            }
        } while (triple != null);

        isComplete = false;
    }

    private Triple getNextTriple() {
        Triple triple = null;
        try {
            while (triple == null) {
                triple = dataGenerator.next();
            }
        } catch (NoSuchElementException ex) {
            logger.debug("DataGenerator reached end!");
        }

        return triple;
    }

    private void updateTriples(Triple triple, boolean keepTriple) {
        if (keepTriple) {
            triplesToAdd.add(triple);
        } else {
            triplesToRemove.add(triple);
        }
    }

    protected void setup() {
        dataGenerator.setDataSource(dataSource);
    }
}
