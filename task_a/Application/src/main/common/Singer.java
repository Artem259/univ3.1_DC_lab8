package main.common;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Singer singer)) {
            return false;
        }
        return Objects.equals(id, singer.id) && Objects.equals(name, singer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
