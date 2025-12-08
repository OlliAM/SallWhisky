package application.model;

/**
 * <p1>Dette Interface tilføjer polymorfi til individuelle subklasser,
 * hvilket giver mulighed for at lagre både {@code Fad} og {@code Flaske} objekter i samme array format </p1>
 */
public abstract class Storable {
    private Plads plads;

    public void gemPåPlads(Plads valgtePlads) {
        if(valgtePlads.getVare() != null) {
            throw new IllegalArgumentException("Den valgte plads er allerede optaget");
        }

        if(plads != null) {
            plads.setVare(null);
        }
        this.plads = valgtePlads;
        plads.setVare(this);
    }

    public Plads getPlads() {
        return plads;
    }

    public void setPlads(Plads plads) {
        this.plads = plads;
    }
}
