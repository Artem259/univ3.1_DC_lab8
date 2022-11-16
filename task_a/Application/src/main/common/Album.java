package main.common;

public class Album {
    private final Integer id;
    private final Singer singer;
    private String name;
    private Integer year; // released
    private String genre;
    private char sep;

    public Album(Integer id, Singer singer, String name, Integer year, String genre) {
        this.id = id;
        this.singer = singer;
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.sep = '#';
    }

    public Album(Singer singer, String[] params) throws IllegalArgumentException {
        if (params.length != 4) {
            throw new IllegalArgumentException();
        }
        this.id = Integer.parseInt(params[0]);
        this.singer = singer;
        this.name = params[1];
        this.year = Integer.parseInt(params[2]);
        this.genre = params[3];
        this.sep = '#';
    }

    public Integer getId() {
        return id;
    }

    public Singer getSinger() {
        return singer;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
        String singerIdStr = String.valueOf(singer.getId());
        String nameStr = String.valueOf(name);
        String yearStr = String.valueOf(year);
        String genreStr = String.valueOf(genre);
        return idStr + sep + singerIdStr + sep + nameStr + sep + yearStr + sep + genreStr;
    }
}
