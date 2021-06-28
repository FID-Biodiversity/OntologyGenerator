package de.biofid.services.dummy;

import de.biofid.services.data.DataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DummyDataSource implements DataSource {

    List<Object> data = new ArrayList<>();
    Iterator<Object> iterator = null;

    @Override
    public Object getDataForString(String string) throws IOException {
        if (iterator == null) {
            iterator = data.iterator();
        }

        return iterator.next();
    }

    public void addData(Object data) {
        this.data.add(data);
    }
}
