package de.biofid.services.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A DataService orchestrates the calling of DataSource, DataGenerator, and DataProcessor, in this order.
 */
public class DataService {

    private static final Logger logger = LogManager.getLogger();

    private DataSource dataSource;
    private DataGenerator dataGenerator;
    private DataProcessor dataProcessor;

    public DataService(DataSource dataSource, DataGenerator dataGenerator, DataProcessor dataProcessor) {
        this.dataSource = dataSource;
        this.dataGenerator = dataGenerator;
        this.dataProcessor = dataProcessor;

        setup();
    }

    public List<Triple> getTriples() {
        List<Triple> triples = new ArrayList<>();
        Triple triple;
        do {
            triple = getNextTriple();
            if (triple != null) {
                boolean keepTriple = dataProcessor.postProcessTriple(triple);
                if (keepTriple) {
                    triples.add(triple);
                }
            }
        } while (triple != null);

        return triples;
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

    protected void setup() {
        dataGenerator.setDataSource(dataSource);
    }
}
