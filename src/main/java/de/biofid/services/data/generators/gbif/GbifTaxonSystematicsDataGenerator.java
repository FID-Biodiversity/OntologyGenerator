package de.biofid.services.data.generators.gbif;

import de.biofid.services.data.Triple;
import de.biofid.services.deserialization.JsonFileReader;
import de.biofid.services.ontologies.Ontology;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class works directly on the current ontology.
 * Hence, there have to be already some GBIF taxa in the ontology.
 * Calls the GBIF-API for the systematic details of the Subject of a Triple.
 * This class uses the API: https://api.gbif.org/v1/species/{SPECIES_ID}
 *
 * This is a work in progress and still needs further implementation:
 *      - Get the GBIF ID from the respective subject of a Triple.
 *      - Return only systematical data.
 */
public class GbifTaxonSystematicsDataGenerator extends GbifGenericDataGenerator {

    private static final String SYSTEMATICS_CONFIGURATION_FILE = "config/generators/gbif-systematics-properties.json";
    private static final String SYSTEMATICS_PROPERTIES_KEY = "properties";

    private Ontology ontology;
    private final Set<String> systematicProperties = new HashSet<>();

    public GbifTaxonSystematicsDataGenerator() throws FileNotFoundException {
        isSetup = true;
        Set<String> systematicProperties = readSystematicPropertiesFromFile(SYSTEMATICS_CONFIGURATION_FILE);
        setSystematicProperties(systematicProperties);
    }

    @Override
    public boolean areAllExpectedParametersSet() {
        return ontology != null;
    }

    public boolean hasSystematicsProperty(Triple triple) {
        String property = triple.predicate;
        return systematicProperties.contains(property);
    }

    @Override
    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public void setSystematicProperties(Collection<String> systematicProperties) {
        this.systematicProperties.addAll(systematicProperties);
    }

    public void clearSystematicProperties() {
        systematicProperties.clear();
    }

    public Set<String> readSystematicPropertiesFromFile(String filePathString) throws FileNotFoundException {
        JSONObject propertyData = JsonFileReader.read(filePathString);
        JSONArray properties = propertyData.getJSONArray(SYSTEMATICS_PROPERTIES_KEY);

        return StreamSupport.stream(properties.spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Override
    protected Triple getNextTripleOrThrow() throws NoSuchElementException {
        while(true) {
            Triple triple = super.getNextTripleOrThrow();

            if (hasSystematicsProperty(triple)) {
                return triple;
            }
        }
    }
}
