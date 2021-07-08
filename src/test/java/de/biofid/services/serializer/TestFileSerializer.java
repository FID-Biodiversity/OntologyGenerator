package de.biofid.services.serializer;

import de.biofid.services.serialization.FileSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TestFileSerializer {

    private static final String TEST_FILE_OUTPUT_PATH = "src/test/resources/testOntology.xml";

    private FileSerializer fileSerializer;

    @Test
    public void testSerialize() {
        fileSerializer.serialize("This is a test!");
        assertTrue(doesTestFileExist());
    }

    @Test
    public void testGetOutputStream() throws IOException {
        OutputStream outputStream = fileSerializer.getOutputStream();
        outputStream.write("This is a test".getBytes(StandardCharsets.UTF_8));
        assertTrue(doesTestFileExist());
    }

    @BeforeEach
    public void setUp() {
        fileSerializer = new FileSerializer();
        fileSerializer.addSink(TEST_FILE_OUTPUT_PATH);
    }

    @AfterEach
    public void cleanUp() {
        File testOutputFile = new File(TEST_FILE_OUTPUT_PATH);
        if (testOutputFile.exists() && testOutputFile.isFile()) {
            testOutputFile.delete();
        }
    }

    private boolean doesTestFileExist() {
        File testOutputFile = new File(TEST_FILE_OUTPUT_PATH);
        return testOutputFile.exists();
    }
}
