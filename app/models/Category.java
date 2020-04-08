package models;

import java.util.List;

public class Category {
    long id;
    String name;
    String fields;

    public Category() {
    };

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(long id, String name, String fields) {
        this.id = id;
        this.name = name;
        this.fields = fields;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFields() { return fields; }

    public void setFields(String fields) { this.fields = fields; }
}
