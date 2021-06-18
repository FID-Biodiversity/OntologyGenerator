package org.apache.jena.ontology.learn;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestJenaOntModel {

    String taxonUriString = "https://www.biofid.de/bio-ontologies/Animal/12345";
    String taxonName = "Testus maximus";
    String parentUriString = "https://www.biofid.de/bio-ontologies/Animal/123";
    String parentName = "Testus";

    @Test
    public void testAddStatementToModel() {
        OntModel model = ModelFactory.createOntologyModel();
        Resource taxon = model.createResource(taxonUriString);
        taxon.addProperty(RDFS.label, taxonName);

        String ontologyString = modelToString(model);
        assertTrue(ontologyString.contains(
                "<rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Animal/12345\">\n" +
                "    <rdfs:label>Testus maximus</rdfs:label>\n" +
                "  </rdf:Description>"
        ));
    }

    @Test
    public void testCreateLiteral() {
        OntModel model = ModelFactory.createOntologyModel();
        Resource taxon = model.createResource(taxonUriString);
        taxon.addProperty(RDFS.label, model.createLiteral("Testus", "en"))
                .addProperty(RDFS.label,model.createLiteral("Testus", "de"));

        String ontologyString = modelToString(model);
        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Animal/12345\">\n" +
                "    <rdfs:label xml:lang=\"de\">Testus</rdfs:label>\n" +
                "    <rdfs:label xml:lang=\"en\">Testus</rdfs:label>\n"));
    }

    @Test
    public void testReferencingOtherResource() {
        OntModel model = ModelFactory.createOntologyModel();
        Resource taxon = model.createResource(taxonUriString);

        Resource genusClass = model.createResource(parentUriString);
        genusClass.addProperty(RDFS.label, parentName);

        taxon.addProperty(RDFS.label, taxonName);
        taxon.addProperty(RDFS.subClassOf, genusClass);

        String ontologyString = modelToString(model);
        assertEquals(
                "<rdf:RDF\n" +
                        "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
                        "    xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n" +
                        "    xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n" +
                        "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" > \n" +
                        "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Animal/123\">\n" +
                        "    <rdfs:label>Testus</rdfs:label>\n" +
                        "  </rdf:Description>\n" +
                        "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Animal/12345\">\n" +
                        "    <rdfs:subClassOf rdf:resource=\"https://www.biofid.de/bio-ontologies/Animal/123\"/>\n" +
                        "    <rdfs:label>Testus maximus</rdfs:label>\n" +
                        "  </rdf:Description>\n" +
                        "</rdf:RDF>\n", ontologyString);
    }

    @Test
    public void testIterateStatements() {
        OntModel model = ModelFactory.createOntologyModel();

        Resource genusClass = model.createResource(parentUriString);
        Resource taxon = model.createResource(taxonUriString);
        taxon.addProperty(RDFS.subClassOf, genusClass);

        StmtIterator statementIterator = model.listStatements();
        assertTrue(statementIterator.hasNext());

        boolean assertionWasTriggered = false;

        while (statementIterator.hasNext()) {
            Statement statement = statementIterator.nextStatement();
            String subjectString = statement.getSubject().toString();
            String predicateString = statement.getPredicate().toString();
            String objectString = statement.getObject().toString();

            if (subjectString.equals(taxonUriString) && predicateString.contains("subClassOf")) {
                assertEquals(parentUriString, objectString);
                assertionWasTriggered = true;

                // One statement also holds that the class is a subClassOf itself. Hence, the break!
                break;
            }
        }

        assertTrue(assertionWasTriggered);
    }

    @Test
    public void testDeclaringPrefixedNamespace() {
        OntModel model = ModelFactory.createOntologyModel();
        String biofidBaseURI = "https://www.biofid.de/bio-ontologies/";
        String biofidTermsURI = biofidBaseURI + "terms/";

        model.setNsPrefix("biofid-terms", biofidTermsURI);
        Resource biofidNamespace = model.createResource(biofidBaseURI);

        Resource tracheophytaNamespace = model.createResource(biofidNamespace + "Tracheophyta/");
        Resource tracheophytaGbifNamespace = model.createResource(tracheophytaNamespace + "gbif/");
        Resource tracheophytaEolNamespace = model.createResource(tracheophytaNamespace + "eol/");
        Resource gbifPlant = model.createResource(tracheophytaGbifNamespace + "1234");
        Resource eolPlant = model.createResource(tracheophytaEolNamespace + "1234");

        Property hasGbifURL = model.createProperty(biofidTermsURI + "hasGbifURL");

        gbifPlant.addProperty(RDFS.label, "GBIF Plant");
        gbifPlant.addProperty(hasGbifURL, "https://www.gbif.org/species/9876");
        eolPlant.addProperty(RDFS.label, "EOL Plant");

        String ontologyString = modelToString(model);
        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Tracheophyta/eol/1234\">\n" +
                "    <rdfs:label>EOL Plant</rdfs:label>\n" +
                "  </rdf:Description>"));
        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/1234\">\n" +
                "    <biofid-terms:hasGbifURL>https://www.gbif.org/species/9876</biofid-terms:hasGbifURL>\n" +
                "    <rdfs:label>GBIF Plant</rdfs:label>\n" +
                "  </rdf:Description>"));
    }

    @Test
    public void testIndividualCreation() {
        OntModel model = ModelFactory.createOntologyModel();
        OntClass gbifTaxonClass = model.createClass("https://www.biofid.de/bio-ontologies#GBIF_taxon");
        gbifTaxonClass.addProperty(RDFS.subClassOf, "https://dwc.tdwg.org/terms/#taxon");

        Individual individual = gbifTaxonClass.createIndividual("https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/1234");
        individual.addProperty(RDFS.label, "Testus maximus");
        individual.addRDFType(OWL2.NamedIndividual);

        String ontologyString = modelToString(model);
        assertTrue(ontologyString.contains(
            "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies/Tracheophyta/gbif/1234\">\n" +
            "    <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#NamedIndividual\"/>\n" +
            "    <rdfs:label>Testus maximus</rdfs:label>\n" +
            "    <rdf:type rdf:resource=\"https://www.biofid.de/bio-ontologies#GBIF_taxon\"/>\n" +
            "  </rdf:Description>"
        ));

        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologies#GBIF_taxon\">\n" +
                "    <rdfs:subClassOf>https://dwc.tdwg.org/terms/#taxon</rdfs:subClassOf>\n" +
                "    <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#Class\"/>\n" +
                "  </rdf:Description>"
        ));
    }

    @Test
    public void testAccessUri() {
        OntModel model = ModelFactory.createOntologyModel();

        Resource taxon = model.createResource(taxonUriString);
        taxon.addProperty(RDFS.label, taxonName);

        Resource accessUri = model.getResource(taxonUriString);
        assertEquals(taxon, accessUri);
    }

    @Test
    public void testCustomPropertyClass() {
        OntModel model = ModelFactory.createOntologyModel();
        model.setNsPrefix("dwc", "https://dwc.tdwg.org/terms/#");

        Resource parentTaxon = model.createResource("https://www.biofid.de/bio-ontologie/Tracheophyta/gbif/1");
        Resource taxon = model.createResource("https://www.biofid.de/bio-ontologie/Tracheophyta/gbif/2");
        taxon.addProperty(DarwinCore.getParentNameUsageIdTerm(model), parentTaxon);
        taxon.addProperty(DarwinCore.getVernacularNameTerm(model), model.createLiteral("Testus maximus", "de"));

        String ontologyString = modelToString(model);
        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://www.biofid.de/bio-ontologie/Tracheophyta/gbif/2\">\n" +
                "    <dwc:vernacularName xml:lang=\"de\">Testus maximus</dwc:vernacularName>\n" +
                "    <dwc:parentNameUsageID rdf:resource=\"https://www.biofid.de/bio-ontologie/Tracheophyta/gbif/1\"/>\n" +
                "  </rdf:Description>\n"
        ));

        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://dwc.tdwg.org/terms/#vernacularName\">\n" +
                "    <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#AnnotationProperty\"/>\n" +
                "  </rdf:Description>"
        ));

        assertTrue(ontologyString.contains(
                "  <rdf:Description rdf:about=\"https://dwc.tdwg.org/terms/#parentNameUsageID\">\n" +
                "    <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#ObjectProperty\"/>\n" +
                "  </rdf:Description>\n"
        ));
    }

    private String modelToString(Model model) {
        OutputStream byteOutput = new ByteArrayOutputStream();
        model.write(byteOutput, "RDF/XML");
        return byteOutput.toString();
    }

    private static class DarwinCore {

        public static Property getVernacularNameTerm(OntModel model) {
            return model.createAnnotationProperty("https://dwc.tdwg.org/terms/#vernacularName");
        }

        public static Property getParentNameUsageIdTerm(OntModel model) {
            return model.createObjectProperty("https://dwc.tdwg.org/terms/#parentNameUsageID");
        }
    }

}
