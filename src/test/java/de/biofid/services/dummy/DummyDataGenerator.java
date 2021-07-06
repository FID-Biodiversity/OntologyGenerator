package de.biofid.services.dummy;

import de.biofid.services.data.DataGenerator;
import de.biofid.services.data.DataSource;
import de.biofid.services.data.Triple;
import org.json.JSONObject;

import java.util.NoSuchElementException;

public class DummyDataGenerator implements DataGenerator {
    @Override
    public Triple next() throws NoSuchElementException {
        return null;
    }

    @Override
    public void setDataSource(DataSource dataSource) {

    }

    @Override
    public void setParameters(JSONObject parameters) {

    }
}
