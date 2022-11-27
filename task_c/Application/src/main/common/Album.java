package main.common;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album album)) {
            return false;
        }
        return Objects.equals(id, album.id)
                && Objects.equals(singer, album.singer)
                && Objects.equals(name, album.name)
                && Objects.equals(year, album.year)
                && Objects.equals(genre, album.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, singer, name, year, genre);
    }
}
