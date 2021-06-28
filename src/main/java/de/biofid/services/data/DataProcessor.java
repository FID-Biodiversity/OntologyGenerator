package de.biofid.services.data;

import de.biofid.services.ontologies.Ontology;

import java.util.List;

public interface DataProcessor {
    abstract public List<Triple> mapDataToTriple(Object data, Ontology ontology);
}
