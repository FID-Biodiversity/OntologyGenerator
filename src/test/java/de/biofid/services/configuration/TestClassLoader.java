package de.biofid.services.configuration;

import de.biofid.services.dummy.DummyClass;
import de.biofid.services.exceptions.ValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClassLoader {
    @Test
    public void testLoadingObjectByNameWithEmptyParameters() throws ValueException {
        String className = "de.biofid.services.dummy.DummyClass";
        assertTrue(ClassLoader.createInstanceOfClassFromName(className) instanceof DummyClass);
    }

    @Test
    public void testCannotLoadName() throws ValueException {
        String notExistingClassName = "de.biofid.services.dummy.ClassThatDoesNotExist";
        assertThrows(ValueException.class, () -> {ClassLoader.createInstanceOfClassFromName(notExistingClassName);});
    }
}
