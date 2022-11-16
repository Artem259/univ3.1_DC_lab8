package main.common.collection;

import main.common.Album;
import main.common.Singer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class CollectionXml {
    private final Collection collection;

    public CollectionXml() {
        this.collection = new Collection();
    }

    public CollectionXml(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public String toXmlString() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert doc != null;
        List<List<Album>> sortedAlbums = collection.getSortedAlbums();

        Element collectionElem = doc.createElement("collection");
        doc.appendChild(collectionElem);
        for (int i=0; i<collection.singers.size(); i++) {
            Singer singer = collection.singers.get(i);
            Element singerElem = doc.createElement("singer");
            singerElem.setAttribute("id", singer.getId().toString());
            singerElem.setAttribute("name", singer.getName());
            collectionElem.appendChild(singerElem);
            for (int j=0; j<sortedAlbums.get(i).size(); j++) {
                Album album = sortedAlbums.get(i).get(j);
                Element albumElem = doc.createElement("album");
                albumElem.setAttribute("id", album.getId().toString());
                albumElem.setAttribute("name", album.getName());
                albumElem.setAttribute("year", album.getYear().toString());
                albumElem.setAttribute("genre", album.getGenre());
                singerElem.appendChild(albumElem);
            }
        }

        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

    public String toXmlFormattedString() {
        StringWriter sw = new StringWriter();
        try {
            String str = toXmlString();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source strSource = new StreamSource(new StringReader(str));
            Result strResult = new StreamResult(sw);
            transformer.transform(strSource, strResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public boolean toXmlFile(File xmlFile) {
        try {
            String str = toXmlString();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source strSource = new StreamSource(new StringReader(str));
            Result fileResult = new StreamResult(xmlFile.getAbsoluteFile());
            transformer.transform(strSource, fileResult);
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean fromXmlDocument(Document doc) {
        collection.clear();
        Element collectionElem = doc.getDocumentElement();
        if (!collectionElem.getTagName().equals("collection")) {
            return false;
        }
        NodeList singesList = collectionElem.getElementsByTagName("singer");
        for (int i=0; i<singesList.getLength(); i++) {
            Element singerElem = (Element) singesList.item(i);
            Integer singerId = Integer.valueOf(singerElem.getAttribute("id"));
            String singerName = singerElem.getAttribute("name");
            Singer singer = new Singer(singerId, singerName);
            collection.addSinger(singer);
            NodeList albumsList = singerElem.getElementsByTagName("album");
            for (int j=0; j<albumsList.getLength(); j++) {
                Element albumElem = (Element) albumsList.item(j);
                Integer albumId = Integer.valueOf(albumElem.getAttribute("id"));
                String albumName = albumElem.getAttribute("name");
                Integer albumYear = Integer.valueOf(albumElem.getAttribute("year"));
                String albumGenre = albumElem.getAttribute("genre");
                Album album = new Album(albumId, singer, albumName, albumYear, albumGenre);
                collection.addAlbum(album);
            }
        }
        return true;
    }

    public boolean fromXmlString(String xmlString) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            doc = db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return fromXmlDocument(doc);
    }

    public boolean fromXmlFile(File xmlFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return fromXmlDocument(doc);
    }

    @Override
    public String toString() {
        return toXmlString();
    }
}
