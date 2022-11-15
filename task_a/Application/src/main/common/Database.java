package main.common;

public interface Database {

    boolean clear();

    Singer getSingerById(int id);

    Album getAlbumById(int id);

    boolean addCollection(Collection collection);

    // 1
    int addSinger(Singer singer);

    // 2
    boolean deleteSingerById(int id);

    // 3
    int addAlbum(int singerId, Album album);

    // 4
    boolean deleteAlbumById(int id);

    // 5
    boolean updateSinger(Singer singer);

    boolean updateAlbum(Album album);

    // 6
    Integer countAlbumsOfSingerById(int id);

    // 7
    Collection getAll();

    // 8
    Collection getAlbumsOfSingerById(int id);

    // 9
    Collection getAllSingers();
}
