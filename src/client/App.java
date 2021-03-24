package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import shared.Tridy;
import shared.Skupina;
import shared.Trida;

public class App {
    public static void main(String[] args) {
        try {
            Tridy tridy = (Tridy) Naming.lookup("rmi://jskola:12345/tridy");

            for (Trida trida : tridy.getTridy()) {
                System.out.println(trida.getOznaceni());
            }
 
            Trida t = new Trida().setOznaceni("4.E").setSkupina(new Skupina("K")).setSkupina(new Skupina("S1"))
                    .setSkupina(new Skupina("S2")).setSkupina(new Skupina("MS2")).setSkupina(new Skupina("MS2"));
            if (tridy.writeTrida(t))
                System.out.println("Zápis ok");
            else
                System.out.println("Zápis not ok");
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
