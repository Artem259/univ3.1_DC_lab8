package main.common.collection;

import main.common.Album;
import main.common.Singer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Collection implements Serializable {
    protected List<Singer> singers;
    protected final List<Album> albums;

    public Collection() {
        singers = new ArrayList<>();
        albums = new ArrayList<>();
    }

    private Integer indexOfSingerById(int id) {
        for (int i=0; i<singers.size(); i++) {
            if (singers.get(i).getId() == id) {
                return i;
            }
        }
        return null;
    }

    private Integer indexOfAlbumById(int id) {
        for (int i=0; i<albums.size(); i++) {
            if (albums.get(i).getId() == id) {
                return i;
            }
        }
        return null;
    }

    private List<Integer> sortedIndicesOfAlbumsOfSingerById(int id) {
        List<Integer> res = new ArrayList<>();
        for (int i=albums.size()-1; i>=0; i--) {
            if (albums.get(i).getSinger().getId() == id) {
                res.add(i);
            }
        }
        return res;
    }

    public void clear() {
        singers.clear();
        albums.clear();
    }

    // 5
    public Singer getSingerById(int id) {
        Integer i = indexOfSingerById(id);
        if (i == null) {
            return null;
        }
        return singers.get(i);
    }

    public Album getAlbumById(int id) {
        Integer i = indexOfAlbumById(id);
        if (i == null) {
            return null;
        }
        return albums.get(i);
    }

    public Integer getNextSingerId() {
        int res = 0;
        for (Singer singer : singers) {
            res = Math.max(res, singer.getId());
        }
        return res + 1;
    }

    public Integer getNextAlbumId() {
        int res = 0;
        for (Album album : albums) {
            res = Math.max(res, album.getId());
        }
        return res + 1;
    }

    // 1
    public void addSinger(Singer singer) {
        if (indexOfSingerById(singer.getId()) != null) {
            throw new IllegalArgumentException();
        }
        singers.add(singer);
    }

    // 2
    public boolean deleteSingerById(int id) {
        Integer i = indexOfSingerById(id);
        if (i != null) {
            singers.remove(i.intValue());
            List<Integer> indices = sortedIndicesOfAlbumsOfSingerById(id);
            for (Integer index : indices) {
                albums.remove(index.intValue());
            }
            return true;
        }
        return false;
    }

    // 3
    public void addAlbum(Album album) {
        if (indexOfSingerById(album.getSinger().getId()) == null) {
            throw new IllegalArgumentException();
        }
        if (indexOfAlbumById(album.getId()) != null) {
            throw new IllegalArgumentException();
        }
        albums.add(album);
    }

    // 4
    public boolean deleteAlbumById(int id) {
        Integer i = indexOfAlbumById(id);
        if (i != null) {
            albums.remove(i.intValue());
            return true;
        }
        return false;
    }

    // 6
    public Integer countAlbumsOfSingerById(int id) {
        if (indexOfSingerById(id) == null) {
            return null;
        }
        List<Integer> indices = sortedIndicesOfAlbumsOfSingerById(id);
        return indices.size();
    }

    // 7
    public List<Album> getAlbumsCopy() {
        return new ArrayList<>(albums);
    }

    // 8
    public List<Album> getAlbumsOfSingerById(int id) {
        Integer i = indexOfSingerById(id);
        if (i == null) {
            return null;
        }
        List<Album> list = getSortedAlbums().get(i);
        return new ArrayList<>(list);
    }

    // 9
    public List<Singer> getSingersCopy() {
        return new ArrayList<>(singers);
    }

    public List<List<Album>> getSortedAlbums() {
        List<List<Album>> res = new ArrayList<>();
        for (int i=0; i<singers.size(); i++) {
            res.add(new ArrayList<>());
        }
        for (Album album : albums) {
            Integer index = indexOfSingerById(album.getSinger().getId());
            if (index == null) {
                throw new RuntimeException();
            }
            res.get(index).add(album);
        }
        return res;
    }
}
