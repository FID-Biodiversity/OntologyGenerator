package de.biofid.services.dummy;

import de.biofid.services.data.Triple;
import de.biofid.services.ontologies.Ontology;
import de.biofid.services.serialization.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DummyOntology implements Ontology {

    Iterator<String> resourceUriIterator;
    List<Triple> triples = new ArrayList<>();
    HashMap<String, String> namespaces = new HashMap<>();

    @Override
    public Iterator<Triple> iterateTriples() {
        return triples.iterator();
    }

    @Override
    public Iterator<String> iterateResourceUris() {
        return resourceUriIterator;
    }

    @Override
    public void addTriple(Triple triple) {
        triples.add(triple);
    }

    @Override
    public void removeTriple(Triple triple) {
        triples = triples.stream()
                .filter(tripleInList -> tripleInList.equals(triple))
                .collect(Collectors.toList());
    }

    @Override
    public void serialize(Serializer serializer) {
        String tripleString = triples.toString();
        OutputStream outputStream = serializer.getOutputStream();

        try {
            outputStream.write(tripleString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {}
    }

    @Override
    public void setNamespaces(HashMap<String, String> namespaces) {
        this.namespaces.putAll(namespaces);
    }

    public HashMap<String, String> getNamespaces() {
        return namespaces;
    }

    public void setResourceUriIterator(Iterator<String> iterator) {
        this.resourceUriIterator = iterator;
    }
}
