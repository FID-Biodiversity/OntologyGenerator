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
import java.util.Iterator;

public class JenaOntology implements Ontology {

    OntModel model = ModelFactory.createOntologyModel();

    @Override
    public Iterator<Triple> iterateTriples() {
        return null;
    }

    @Override
    public Iterator<String> iterateResourceUris() {
        return null;
    }

    @Override
    public void addTriple(Triple triple) {
        Resource resource = model.createResource(triple.subject);
        Property property = ResourceFactory.createProperty(triple.predicate);
        resource.addLiteral(property, triple.object);
    }

    @Override
    public void removeTriple(Triple triple) {

    }

    @Override
    public void serialize(Serializer serializer) {
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
}
