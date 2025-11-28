package application.model;

/**
 * <h2> ----- Attributer ----- </h2>
 * <br>
 * <p1><b>flaskeID (String):</b><br>
 * Tekst identifikation af det relaterede {@code Flaske} objekt.</p1><br>
 * <br>
 * <p1><b>kakacitetL (double):</b><br>
 * Den totale væskemængde i Liter som dette {@code Flaske} objekt kan indeholde.</p1><br>
 * <br>
 * <h3>-------------------------------------</h3>
 */
public class Flaske implements Storable {
    private String flaskeID;
    private double kapacitetL = 0.7;

    //Linkattributter
    Færdigprodukt færdigprodukt;

    Flaske(Færdigprodukt færdigprodukt, int flaskeNr) {
        this.flaskeID = færdigprodukt.getProduktNr() + "-" + flaskeNr;
    }

    public String getFlaskeID() {
        return flaskeID;
    }

    public double getKapacitetL() {
        return kapacitetL;
    }

    public Færdigprodukt getFærdigprodukt() {
        return færdigprodukt;
    }

    public void setFlaskeID(String flaskeID) {
        this.flaskeID = flaskeID;
    }

    public void setKapacitetL(double kapacitetL) {
        this.kapacitetL = kapacitetL;
    }

    public void setFærdigprodukt(Færdigprodukt færdigprodukt) {
        this.færdigprodukt = færdigprodukt;
    }

}
