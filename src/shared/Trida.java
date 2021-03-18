package shared;


import java.util.Iterator;
import java.util.Map;

import java.util.TreeMap;

public class Trida extends Polozka{
    private int id;
    private Map<String, Skupina> skupiny;
    private String oznaceni;

    public Trida() {
        skupiny = new TreeMap<>();
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public Trida setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
        return this;
    }

    @Override
    public Trida setId(int id) {   
        return (Trida)super.setId(id);
    }

    public int getId() {
        return id;
    }

    public Skupina vytvoritSkupinu(String oznaceni) {
        if (skupiny.containsKey(oznaceni)) {
            return null;
        }

        Skupina skupina = new Skupina(oznaceni);
        skupiny.put(oznaceni, skupina);

        return skupina;
    }

    public Trida setSkupina(Skupina skupina) {
        skupiny.put(skupina.getOznaceni(), skupina);
        return this;
    }

    public Iterator<Skupina> skupiny() {
        return skupiny.values().iterator();
    }
}