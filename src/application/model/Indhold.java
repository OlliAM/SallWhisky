package application.model;

public class Indhold implements Drinkable{
    private String navn;

    public Indhold(String navn) {
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}
