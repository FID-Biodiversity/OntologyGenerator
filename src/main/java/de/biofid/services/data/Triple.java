package de.biofid.services.data;

public class Triple {

    public String subject;
    public String predicate;
    public Object object;

    public Triple(String subject, String predicate, Object object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    @Override
    public String toString() {
        return subject + " " + predicate + " " + object;
    }
}
