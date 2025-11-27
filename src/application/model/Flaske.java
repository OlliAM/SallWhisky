package application.model;

public class Flaske implements Storable {
    private String navn;
    private String flaskeID;
    private double kapacitetL = 0.7;
    private String beskrivelse;

    //Linkattributter
    Færdigprodukt færdigprodukt;

    Flaske(Færdigprodukt færdigprodukt, int flaskeNr) {
        this.navn = færdigprodukt.getNavn();
        this.flaskeID = færdigprodukt.getProduktNr() + "-" + flaskeNr;
        this.beskrivelse = færdigprodukt.getBeskrivelse();
    }

    public String getFlaskeID() {
        return flaskeID;
    }

    public double getKapacitetL() {
        return kapacitetL;
    }

    public String getNavn() {
        return navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Færdigprodukt getFærdigprodukt() {
        return færdigprodukt;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setFlaskeID(String flaskeID) {
        this.flaskeID = flaskeID;
    }

    public void setKapacitetL(double kapacitetL) {
        this.kapacitetL = kapacitetL;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setFærdigprodukt(Færdigprodukt færdigprodukt) {
        this.færdigprodukt = færdigprodukt;
    }

}
