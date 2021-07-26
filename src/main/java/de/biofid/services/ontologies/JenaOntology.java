package de.biofid.services.ontologies;

import de.biofid.services.data.Triple;
import de.biofid.services.serialization.Serializer;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;


public class JenaOntology implements Ontology {

    OntModel model = ModelFactory.createOntologyModel();
    Set<Triple> triples = new HashSet<>();

    @Override
    public Iterator<Triple> iterateTriples() {
        return triples.iterator();
    }

    @Override
    public Iterator<String> iterateResourceUris() {
        Set<String> subjectsInOntology = triples.stream()
                .map(triple -> triple.subject)
                .collect(Collectors.toSet());

        return subjectsInOntology.iterator();
    }

    @Override
    public void addTriple(Triple triple) {
        triples.add(triple);
    }

    @Override
    public void removeTriple(Triple triple) {
        triples.remove(triple);
    }

    @Override
    public boolean hasTriple(Triple triple) {
        return triples.contains(triple);
    }

    @Override
    public void serialize(Serializer serializer) {
        updateOntologyWithBufferedTriples();

        OutputStream byteOutput = serializer.getOutputStream();
        model.write(byteOutput, "RDF/XML");
    }

    @Override
    public void setNamespaces(HashMap<String, String> namespaces) {
        for (String namespace : namespaces.keySet()) {
            setNamespace(namespace, namespaces.get(namespace));
        }
    }

    public void setNamespace(String abbreviation, String url) {
        model.setNsPrefix(abbreviation, url);
    }

    private void updateOntologyWithBufferedTriples() {
        for (Triple triple : triples) {
            addTripleToOntology(triple);
        }
    }

    private void addTripleToOntology(Triple triple) {
        Resource resource = model.createResource(triple.subject);
        Property property = ResourceFactory.createProperty(triple.predicate);
        resource.addLiteral(property, triple.object);
    }
}
