package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(12345);
            reg.rebind("tridy", new Tridy());

            System.out.println("Server ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
}
