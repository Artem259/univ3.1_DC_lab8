package main.common;

public class Album {
    private final Integer id;
    private final Singer singer;
    private String name;
    private Integer year; // released
    private String genre;

    public Album(Integer id, Singer singer, String name, Integer year, String genre) {
        this.id = id;
        this.singer = singer;
        this.name = name;
        this.year = year;
        this.genre = genre;
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

    public String toString(char sep) {
        return id + sep + singer.getId() + sep + name + sep + year + sep + genre;
    }
}
