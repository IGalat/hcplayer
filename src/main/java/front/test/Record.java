package front.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Record {

    private static int trackId;
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;

    public Record(String name) {
        this.id = new SimpleIntegerProperty(trackId);
        this.name = new SimpleStringProperty(name);
        trackId++;
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}