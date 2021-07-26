package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import de.biofid.services.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * The OntologyConnector links the OntologyGenerator and the Ontology implementation.
 * It also reads the namespace data and hands it to the Ontology.
 */
public class OntologyConnector {

    protected static final Logger logger = LogManager.getLogger();

    private static final String NAMESPACES_JSON_FILE_PATH = "config/namespaces.json";

    private final Ontology ontology;

    public OntologyConnector(Ontology ontology) {
        this.ontology = ontology;

        try {
            setOntologyNamespacesFromFile(NAMESPACES_JSON_FILE_PATH);
        } catch (FileNotFoundException ex) {
            logger.info("Could not load namespace data! It should be located in \"" + NAMESPACES_JSON_FILE_PATH +
                    "\". Proceeding without defining namespaces!");
        }
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void addTripleToOntology(Triple triple) {
        ontology.addTriple(triple);
    }

    public void removeTripleFromOntology(Triple triple) {
        ontology.removeTriple(triple);
    }

    public void serializeOntology(Serializer serializer) {
        ontology.serialize(serializer);
    }

    public void setOntologyNamespacesFromFile(String filePathString) throws FileNotFoundException {
        JSONObject namespaceData = JsonFileReader.read(filePathString);
        ontology.setNamespaces(jsonDataToMap(namespaceData));
    }

    private HashMap<String, String> jsonDataToMap(JSONObject jsonData) {
        HashMap<String, String> convertedMap = new HashMap<>();
        for (String key : jsonData.keySet()) {
            convertedMap.put(key, jsonData.getString(key));
        }
        return convertedMap;
    }
}
