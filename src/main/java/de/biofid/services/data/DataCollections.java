package de.biofid.services.data;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DataCollections {

    public static List<Triple> DataGeneratorToList(DataGenerator dataGenerator) {
        List<Triple> triples = new ArrayList<>();
        try {
            while (true) {
                triples.add(dataGenerator.next());
            }
        } catch (NoSuchElementException ex) {}

        return triples;
    }

    public static boolean isTripleInList(Triple triple, List<Triple> triples) {
        return triples.stream()
                .filter(obj -> obj.subject.equals(triple.subject)
                        && obj.predicate.equals(triple.predicate)
                        && obj.object.equals(triple.object))
                .count() == 1;
    }
}
