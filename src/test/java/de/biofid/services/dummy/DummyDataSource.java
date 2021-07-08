package de.biofid.services.dummy;

import de.biofid.services.data.DataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DummyDataSource implements DataSource {

    public List<String> requestedStrings = new ArrayList<>();

    List<Object> data = new ArrayList<>();
    Iterator<Object> iterator = null;

    @Override
    public Object getDataForString(String string) {
        requestedStrings.add(string);

        if (iterator == null) {
            iterator = data.iterator();
        }

        return iterator.next();
    }

    public void addData(Object data) {
        this.data.add(data);
    }
}
