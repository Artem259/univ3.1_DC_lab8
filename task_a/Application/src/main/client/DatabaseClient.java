package main.client;

import main.common.*;
import main.common.collection.Collection;
import main.common.collection.CollectionXml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;

public class DatabaseClient implements Database {
    private final BufferedReader in;
    private final PrintWriter out;
    private String[] fields;
    private int responseCode;
    private final char sep;

    public DatabaseClient(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.sep = '#';
    }

    private void sendAndReceive(int requestCode, String requestMessage, int paramsNumber) {
        responseCode = 0;
        String responseMessage;
        out.println(Integer.toString(requestCode) + sep + requestMessage);
        try {
            responseMessage = in.readLine();
            fields = responseMessage.split(String.valueOf(sep));
            responseCode = Integer.parseInt(fields[0]);
            fields = Arrays.copyOfRange(fields, 1, fields.length);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (responseCode != 0) {
            throw new RuntimeException("Unexpected response code.");
        }
        else if (fields.length != paramsNumber) {
            throw new RuntimeException("Unexpected number of parameters in the response.");
        }
    }

    private Integer valueOfInteger(String str) {
        if (Objects.equals(str, "null")) {
            return null;
        }
        return Integer.valueOf(str);
    }

    @Override
    public boolean clear() {
        sendAndReceive(1, "", 1);
        return Boolean.parseBoolean(fields[0]);
    }

    @Override
    public Singer getSingerById(int id) {
        sendAndReceive(2, Integer.toString(id), 1);
        if (Objects.equals(fields[0], "null")) {
            return null;
        }
        CollectionXml collectionXml = new CollectionXml();
        if (!collectionXml.fromXmlString(fields[0])) {
            throw new RuntimeException();
        }
        return collectionXml.getCollection().getSingersCopy().get(0);
    }

    @Override
    public Album getAlbumById(int id) {
        sendAndReceive(3, Integer.toString(id), 1);
        if (Objects.equals(fields[0], "null")) {
            return null;
        }
        CollectionXml collectionXml = new CollectionXml();
        if (!collectionXml.fromXmlString(fields[0])) {
            throw new RuntimeException();
        }
        return collectionXml.getCollection().getAlbumsCopy().get(0);
    }

    @Override
    public boolean addCollection(Collection collection) {
        CollectionXml collectionXml = new CollectionXml(collection);
        sendAndReceive(4, collectionXml.toXmlString(), 1);
        return Boolean.parseBoolean(fields[0]);
    }

    @Override
    public Integer addSinger(Singer singer) {
        CollectionXml collectionXml = new CollectionXml();
        collectionXml.getCollection().addSinger(singer);
        sendAndReceive(5, collectionXml.toXmlString(), 1);
        return valueOfInteger(fields[0]);
    }

    @Override
    public boolean deleteSingerById(int id) {
        sendAndReceive(6, Integer.toString(id), 1);
        return Boolean.parseBoolean(fields[0]);
    }

    @Override
    public Integer addAlbum(int singerId, Album album) {
        CollectionXml collectionXml = new CollectionXml();
        collectionXml.getCollection().addSinger(new Singer(singerId, ""));
        collectionXml.getCollection().addAlbum(album);
        sendAndReceive(7, collectionXml.toXmlString(), 1);
        return valueOfInteger(fields[0]);
    }

    @Override
    public boolean deleteAlbumById(int id) {
        sendAndReceive(8, Integer.toString(id), 1);
        return Boolean.parseBoolean(fields[0]);
    }

    @Override
    public boolean updateSinger(Singer singer) {
        CollectionXml collectionXml = new CollectionXml();
        collectionXml.getCollection().addSinger(singer);
        sendAndReceive(9, collectionXml.toXmlString(), 1);
        return Boolean.parseBoolean(fields[0]);
    }

    @Override
    public boolean updateAlbum(Album album) {
        CollectionXml collectionXml = new CollectionXml();
        collectionXml.getCollection().addAlbum(album);
        sendAndReceive(10, collectionXml.toXmlString(), 1);
        return Boolean.parseBoolean(fields[0]);
    }

    @Override
    public Integer countAlbumsOfSingerById(int id) {
        sendAndReceive(11, Integer.toString(id), 1);
        return valueOfInteger(fields[0]);
    }

    @Override
    public Collection getAll() {
        sendAndReceive(12, "", 1);
        CollectionXml collectionXml = new CollectionXml();
        if (!collectionXml.fromXmlString(fields[0])) {
            throw new RuntimeException();
        }
        return collectionXml.getCollection();
    }

    @Override
    public Collection getAlbumsOfSingerById(int id) {
        sendAndReceive(13, Integer.toString(id), 1);
        CollectionXml collectionXml = new CollectionXml();
        if (!collectionXml.fromXmlString(fields[0])) {
            throw new RuntimeException();
        }
        return collectionXml.getCollection();
    }

    @Override
    public Collection getAllSingers() {
        sendAndReceive(14, "", 1);
        CollectionXml collectionXml = new CollectionXml();
        if (!collectionXml.fromXmlString(fields[0])) {
            throw new RuntimeException();
        }
        return collectionXml.getCollection();
    }
}
