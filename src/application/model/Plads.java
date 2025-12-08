package application.model;

public class Plads {
    private int pladsNr;

    //Linkattributter
    private Reol reol;
    private Storable vare;

    public Plads(Reol reol, int pladsNr) {
        this.pladsNr = pladsNr;
        this.reol = reol;
    }

    public Reol getReol() {
        return reol;
    }

    public Storable getVare() {
        return vare;
    }

    public void setVare(Storable vare) {
        this.vare = vare;
    }

    public int getPladsNr() {
        return pladsNr;
    }

    @Override
    public String toString() {
        return reol.toString() + ", plads " + pladsNr;
    }
}
