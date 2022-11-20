package main.common;

import main.common.collection.Collection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Database extends Remote {

    boolean clear() throws RemoteException;

    Singer getSingerById(int id) throws RemoteException;

    Album getAlbumById(int id) throws RemoteException;

    boolean addCollection(Collection collection) throws RemoteException;

    // 1
    Integer addSinger(Singer singer) throws RemoteException;

    // 2
    boolean deleteSingerById(int id) throws RemoteException;

    // 3
    Integer addAlbum(int singerId, Album album) throws RemoteException;

    // 4
    boolean deleteAlbumById(int id) throws RemoteException;

    // 5
    boolean updateSinger(Singer singer) throws RemoteException;

    boolean updateAlbum(Album album) throws RemoteException;

    // 6
    Integer countAlbumsOfSingerById(int id) throws RemoteException;

    // 7
    Collection getAll() throws RemoteException;

    // 8
    Collection getAlbumsOfSingerById(int id) throws RemoteException;

    // 9
    Collection getAllSingers() throws RemoteException;
}
