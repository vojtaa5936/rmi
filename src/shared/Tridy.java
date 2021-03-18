package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Tridy extends Remote {
    List<Trida> getTridy() throws RemoteException;
    Trida getTrida(int id) throws RemoteException;
    boolean writeTrida(Trida trida) throws RemoteException;
}