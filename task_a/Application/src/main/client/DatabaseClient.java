package main.client;

import main.common.*;

public class DatabaseClient implements Database {
    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public Singer getSingerById(int id) {
        return null;
    }

    @Override
    public Album getAlbumById(int id) {
        return null;
    }

    @Override
    public boolean addCollection(Collection collection) {
        return false;
    }

    @Override
    public int addSinger(Singer singer) {
        return 0;
    }

    @Override
    public boolean deleteSingerById(int id) {
        return false;
    }

    @Override
    public int addAlbum(int singerId, Album album) {
        return 0;
    }

    @Override
    public boolean deleteAlbumById(int id) {
        return false;
    }

    @Override
    public boolean updateSinger(Singer singer) {
        return false;
    }

    @Override
    public boolean updateAlbum(Album album) {
        return false;
    }

    @Override
    public Integer countAlbumsOfSingerById(int id) {
        return null;
    }

    @Override
    public Collection getAll() {
        return null;
    }

    @Override
    public Collection getAlbumsOfSingerById(int id) {
        return null;
    }

    @Override
    public Collection getAllSingers() {
        return null;
    }
}
