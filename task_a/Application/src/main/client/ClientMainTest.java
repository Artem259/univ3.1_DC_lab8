package main.client;

import main.common.*;
import main.common.collection.Collection;
import main.common.collection.CollectionXml;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientMainTest {
    private final Collection collection;
    private final File xmlFile;
    private DatabaseClient db;

    public ClientMainTest() {
        this.collection = new Collection();
        this.xmlFile = new File("src/main/client/files", "xml_file.xml");
        this.db = null;
    }

    public void addSinger(String name) {
        collection.addSinger(new Singer(collection.getNextSingerId(),name));
    }

    public void addAlbumToPrevSinger(String name, Integer year, String genre) {
        Singer singer = collection.getSingerById(collection.getNextSingerId() - 1);
        collection.addAlbum(new Album(collection.getNextAlbumId(), singer, name, year, genre));
    }

    public void fillCollection() {
        collection.clear();
        addSinger("singer_1");
        addAlbumToPrevSinger("album_1_1", 2011, "Metal1");
        addAlbumToPrevSinger("album_1_2", 2012, "Pop1");
        addAlbumToPrevSinger("album_1_3", 2013, "Rock1");

        addSinger("singer_2");
        addAlbumToPrevSinger("album_2_1", 2021, "Metal2");
        addAlbumToPrevSinger("album_2_2", 2022, "Pop2");
        addAlbumToPrevSinger("album_2_3", 2023, "Rock2");

        addSinger("singer_3");

        addSinger("singer_4");
        addAlbumToPrevSinger("album_4_1", 2041, "Metal4");
        addAlbumToPrevSinger("album_4_2", 2042, "Pop4");
    }

    public void fillDatabase(Collection collection) {
        db.clear();
        for (Singer singer : collection.getSingersCopy()) {
            db.addSinger(singer);
        }
        for (Album album : collection.getAlbumsCopy()) {
            db.addAlbum(album.getSinger().getId(), album);
        }
    }

    private void refill() {
        fillCollection();
        fillDatabase(collection);
    }

    private void printCollection() {
        CollectionXml collectionXML = new CollectionXml(collection);
        System.out.println("\n" + collectionXML.toXmlFormattedString());
    }

    public void test1() {
        CollectionXml collectionXML = new CollectionXml(collection);

        collectionXML.toXmlFile(xmlFile);
        collection.clear();
        collectionXML.fromXmlFile(xmlFile);

        Singer singer = new Singer(100, "singer_100");
        Album album = new Album(100, singer, "album_100_100", 2100, "Metal100");

        // ----------------------------------

        collection.addSinger(singer);
        db.addSinger(singer);

        collection.deleteAlbumById(1);
        db.deleteAlbumById(1);

        collection.deleteSingerById(2);
        db.deleteSingerById(2);

        collection.addAlbum(album);
        db.addAlbum(singer.getId(), album);

        // ----------------------------------

        CollectionXml dbCollectionXml = new CollectionXml(db.getAll());

        if (!dbCollectionXml.toXmlString().equals(collectionXML.toXmlString())) {
            throw new RuntimeException();
        }
    }

    public void test2() {
        Singer singer = db.getSingerById(4);
        if (!Objects.equals(singer, new Singer(4, "singer_4"))) {
            throw new RuntimeException();
        }

        Album album = db.getAlbumById(6);
        singer = new Singer(2, "singer_2");
        if (!Objects.equals(album, new Album(6, singer, "album_2_3", 2023, "Rock2"))) {
            throw new RuntimeException();
        }

        if (db.countAlbumsOfSingerById(2) != 3) {
            throw new RuntimeException();
        }
        if (db.countAlbumsOfSingerById(3) != 0) {
            throw new RuntimeException();
        }
        if (db.countAlbumsOfSingerById(5) != null) {
            throw new RuntimeException();
        }

        Collection c = db.getAlbumsOfSingerById(4);
        if (!Objects.equals(c.getAlbumsCopy(), collection.getAlbumsOfSingerById(4))) {
            throw new RuntimeException();
        }
        c = db.getAlbumsOfSingerById(3);
        if (!c.getAlbumsCopy().isEmpty()) {
            throw new RuntimeException();
        }

        c = db.getAllSingers();
        if (!Objects.equals(c.getSingersCopy(), collection.getSingersCopy())) {
            throw new RuntimeException();
        }
    }

    public void start(String ip, int port) {
        try (Socket socket = new Socket(ip, port)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            this.db = new DatabaseClient(in, out);

            refill();
            printCollection();
            test1();

            refill();
            test2();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new ClientMainTest().start("localhost", 6666);
    }
}
