package application.model;

public class Flaske implements Storable {
    private String flaskeID;
    private double flaskeKapacitetL;

    //Linkattributter
    Færdigprodukt færdigprodukt;

    /**
     * Contructor for et Flaske-objekt.
     * @param færdigprodukt
     * @param flaskeKapacitetL
     * @param flaskeNr
     * @implements Storable
     */
    Flaske(Færdigprodukt færdigprodukt, double flaskeKapacitetL, int flaskeNr) {
        this.flaskeID = færdigprodukt.getProduktNr() + "-" + flaskeNr;
        this.flaskeKapacitetL = flaskeKapacitetL;
    }

    public String getFlaskeID() {
        return flaskeID;
    }

    public double getFlaskeKapacitetL() {
        return flaskeKapacitetL;
    }

    public Færdigprodukt getFærdigprodukt() {
        return færdigprodukt;
    }

    public void setFlaskeID(String flaskeID) {
        this.flaskeID = flaskeID;
    }

    public void setFærdigprodukt(Færdigprodukt færdigprodukt) {
        this.færdigprodukt = færdigprodukt;
    }

}
