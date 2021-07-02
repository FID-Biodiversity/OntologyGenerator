package de.biofid.services.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripleAssertions {

    public static void assertTripleHas(Triple triple, String subject, String predicate, Object object) {
        assertEquals(subject, triple.subject);
        assertEquals(predicate, triple.predicate);
        assertEquals(object, triple.object);
    }
}
