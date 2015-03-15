package de.lighti.model.state;

public class EntityClass {
    private final int id;
    private final String dtName;
    private final String name;

    public EntityClass( int id, String dtName, String name ) {
        super();
        this.id = id;
        this.dtName = dtName;
        this.name = name;
    }

    public String getDtName() {
        return dtName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EntityClass [id=" + id + ", dtName=" + dtName + ", name=" + name + "]";
    }

}