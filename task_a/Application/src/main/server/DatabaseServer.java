package main.server;

import main.common.*;
import main.common.collection.Collection;

import java.sql.*;
import java.util.List;

public class DatabaseServer implements Database {
    private final Connection connection;

    public DatabaseServer(Connection connection) {
        this.connection = connection;
    }

    private int execAdd(String sql) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean execUpdateDelete(String sql) {
        try {
            Statement stm = connection.createStatement();
            stm.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM singer";
        return execUpdateDelete(sql);
    }

    @Override
    public Singer getSingerById(int id) {
        String sql = "SELECT * FROM singer WHERE id = " + id;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            Singer singer = null;
            if (rs.next()) {
                String name = rs.getString("name");
                singer = new Singer(id, name);
            }

            rs.close();
            return singer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Album getAlbumById(int id) {
        String sql = "SELECT * FROM album WHERE id = " + id;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            Album album = null;
            if (rs.next()) {
                String name = rs.getString("name");
                Singer singer = getSingerById(rs.getInt("singer_id"));
                Integer year = rs.getInt("year");
                String genre = rs.getString("genre");
                album = new Album(id, singer, name, year, genre);
            }

            rs.close();
            return album;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addCollection(Collection collection) {
        List<Singer> singers = collection.getSingersCopy();
        List<List<Album>> sortedAlbums = collection.getSortedAlbums();
        boolean flag = true;
        for (int i=0; i<singers.size(); i++) {
            Singer singer = singers.get(i);
            if (addSinger(singer) == -1) {
                flag = false;
            }
            for (int j=0; j<sortedAlbums.get(i).size(); j++) {
                Album album = sortedAlbums.get(i).get(j);
                if (addAlbum(singer.getId(), album) == -1) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    @Override
    public int addSinger(Singer singer) {
        String sql;
        if (singer.getId() != null) {
            sql = "INSERT INTO singer (id, name) " +
                    "VALUES (" + singer.getId() + ", '" + singer.getName() + "')";
        }
        else {
            sql = "INSERT INTO singer (name) " +
                    "VALUES ('" + singer.getName() + "')";
        }
        return execAdd(sql);
    }

    @Override
    public boolean deleteSingerById(int id) {
        String sql = "DELETE FROM singer " +
                "WHERE id = " + id;
        return execUpdateDelete(sql);
    }

    @Override
    public int addAlbum(int singerId, Album album) {
        String sql;
        if (album.getId() != null) {
            sql = "INSERT INTO album (id, singer_id, name, year, genre) " +
                    "VALUES (" + album.getId() + ", " + singerId + ", '" +
                    album.getName() + "', " + album.getYear() + ", '" +
                    album.getGenre() + "')";
        }
        else {
            sql = "INSERT INTO album (singer_id, name, year, genre) " +
                    "VALUES (" + singerId + ", '" + album.getName() + "', " +
                    album.getYear() + ", '" + album.getGenre() + "')";
        }
        return execAdd(sql);
    }

    @Override
    public boolean deleteAlbumById(int id) {
        String sql = "DELETE FROM album " +
                "WHERE id = " + id;
        return execUpdateDelete(sql);
    }

    @Override
    public boolean updateSinger(Singer singer) {
        String sql = "UPDATE singer SET name = '" + singer.getName() +
                "' where id = " + singer.getId();
        return execUpdateDelete(sql);
    }

    @Override
    public boolean updateAlbum(Album album) {
        String sql = "UPDATE album SET name = '" + album.getName() + "', year = " +
                album.getYear() + ", genre = '" + album.getGenre() +  "' " +
                "where id = " + album.getId();
        return execUpdateDelete(sql);
    }

    @Override
    public Integer countAlbumsOfSingerById(int id) {
        Collection collection = getAlbumsOfSingerById(id);
        if (collection == null) {
            return null;
        }
        return collection.countAlbumsOfSingerById(id);
    }

    @Override
    public Collection getAll() {
        Collection collection = getAllSingers();
        if (collection == null) {
            return null;
        }

        String sql = "SELECT * FROM album";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                int albumId = rs.getInt("id");
                int singerId = rs.getInt("singer_id");
                String name = rs.getString("name");
                Integer year = rs.getInt("year");
                String genre = rs.getString("genre");
                Album album = new Album(albumId, collection.getSingerById(singerId), name, year, genre);
                collection.addAlbum(album);
            }

            rs.close();
            return collection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection getAlbumsOfSingerById(int id) {
        String sql = "SELECT * FROM (singer INNER JOIN album on singer.id = album.singer_id) " +
                "WHERE singer_id = " + id;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            Collection collection = new Collection();
            Singer singer = getSingerById(id);
            if (singer == null) {
                return collection;
            }
            collection.addSinger(singer);
            while (rs.next()) {
                int albumId = rs.getInt("album.id");
                String name = rs.getString("album.name");
                Integer year = rs.getInt("year");
                String genre = rs.getString("genre");
                Album album = new Album(albumId, singer, name, year, genre);
                collection.addAlbum(album);
            }

            rs.close();
            return collection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection getAllSingers() {
        String sql = "SELECT * FROM singer";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            Collection collection = new Collection();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Singer singer = new Singer(id, name);
                collection.addSinger(singer);
            }

            rs.close();
            return collection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
