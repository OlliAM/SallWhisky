package application.model;

public class Flaske implements Storable {
    private String navn;
    private String flaskeID;
    private double flaskeKapacitetL;
    private String beskrivelse;

    //Linkattributter
    Færdigprodukt færdigprodukt;

    /**
     * Contructor for et Flaske-objekt.
     * @implements Storable
     * @param færdigprodukt
     * @param flaskeKapacitetL
     * @param flaskeNr
     */
    Flaske(Færdigprodukt færdigprodukt, double flaskeKapacitetL, int flaskeNr) {
        this.navn = færdigprodukt.getNavn();
        this.flaskeID = færdigprodukt.getProduktNr() + "-" + flaskeNr;
        this.beskrivelse = færdigprodukt.getBeskrivelse();
        this.flaskeKapacitetL = flaskeKapacitetL;
    }

    public String getFlaskeID() {
        return flaskeID;
    }

    public double getFlaskeKapacitetL() {
        return flaskeKapacitetL;
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

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setFærdigprodukt(Færdigprodukt færdigprodukt) {
        this.færdigprodukt = færdigprodukt;
    }

}
