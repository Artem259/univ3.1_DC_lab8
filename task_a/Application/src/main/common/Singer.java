package main.common;

public class Singer {
    private final Integer id;
    private String name;
    private char sep;

    public Singer(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.sep = '#';
    }

    public Singer(String[] params) throws IllegalArgumentException {
        if (params.length != 2) {
            throw new IllegalArgumentException();
        }
        this.id = Integer.parseInt(params[0]);
        this.name = params[1];
        this.sep = '#';
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

    public char getSep() {
        return sep;
    }

    public void setSep(char sep) {
        this.sep = sep;
    }

    @Override
    public String toString() {
        String idStr = String.valueOf(id);
        String nameStr = String.valueOf(name);
        return idStr + sep + nameStr;
    }
}
