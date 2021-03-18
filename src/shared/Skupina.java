package shared;

public class Skupina extends Polozka{
    private String oznaceni;

    public Skupina(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    @Override
    public Skupina setId(int id) {
        return (Skupina)super.setId(id);
    }
}