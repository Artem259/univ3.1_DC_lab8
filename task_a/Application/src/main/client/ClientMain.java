package main.client;

import main.common.*;
import main.common.collection.Collection;
import main.common.collection.CollectionXml;

import java.io.File;

public class ClientMain {
    private final Collection collection;
    private final File xmlFile;
    private final DatabaseClient db;

    public ClientMain() {
        this.collection = new Collection();
        this.xmlFile = new File("src/main/client/files", "xml_file.xml");
        this.db = new DatabaseClient();
    }

    public void addSinger(String name) {
        collection.addSinger(new Singer(collection.getNextSingerId(),name));
    }

    public void addAlbumToPrevSinger(String name, Integer year, String genre) {
        Singer singer = collection.getSingerById(collection.getNextSingerId() - 1);
        collection.addAlbum(new Album(collection.getNextAlbumId(), singer, name, year, genre));
    }

    public void fillCollection() {
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
        if (!db.addCollection(collection)) {
            throw new RuntimeException();
        }
    }

    public void test() {
        CollectionXml collectionXML = new CollectionXml(collection);
        fillCollection();
        fillDatabase(collection);

        collectionXML.toXmlFile(xmlFile);
        collection.clear();
        collectionXML.fromXmlFile(xmlFile);

        System.out.println("\n" + collectionXML.toXmlFormattedString());

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
        System.out.println("\n" + dbCollectionXml.toXmlFormattedString());

        if (!dbCollectionXml.toXmlString().equals(collectionXML.toXmlString())) {
            System.out.println("\n" + collectionXML.toXmlFormattedString());
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        new ClientMain().test();
    }
}
