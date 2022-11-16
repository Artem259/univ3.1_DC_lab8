package main.common;

public class Singer {
    private final Integer id;
    private String name;

    public Singer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(char sep) {
        return id + sep + name;
    }
}
