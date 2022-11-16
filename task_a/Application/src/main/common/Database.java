package main.common;

import main.common.collection.Collection;

public interface Database {

    // Operation code: 1
    boolean clear();

    // Operation code: 2
    Singer getSingerById(int id);

    // Operation code: 3
    Album getAlbumById(int id);

    // Operation code: 4
    boolean addCollection(Collection collection);

    // 1
    // Operation code: 5
    int addSinger(Singer singer);

    // 2
    // Operation code: 6
    boolean deleteSingerById(int id);

    // 3
    // Operation code: 7
    int addAlbum(int singerId, Album album);

    // 4
    // Operation code: 8
    boolean deleteAlbumById(int id);

    // 5
    // Operation code: 9
    boolean updateSinger(Singer singer);

    // Operation code: 10
    boolean updateAlbum(Album album);

    // 6
    // Operation code: 11
    Integer countAlbumsOfSingerById(int id);

    // 7
    // Operation code: 12
    Collection getAll();

    // 8
    // Operation code: 13
    Collection getAlbumsOfSingerById(int id);

    // 9
    // Operation code: 14
    Collection getAllSingers();
}
