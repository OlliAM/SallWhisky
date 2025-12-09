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
            fjernFraPlads();
        }

        this.plads = valgtePlads;
        Reol reol = plads.getReol();
        Lager lager = reol.getLager();
        reol.decrementFriePladser();

        if(this instanceof Fad) {
            lager.incrementFade();
        }

        else if(this instanceof Flaske) {
            lager.incrementFlasker();
        }

        plads.setVare(this);
    }

    public void fjernFraPlads() {
        if(plads != null) {
            Reol reol = plads.getReol();
            Lager lager = reol.getLager();
            plads.setVare(null);
            reol.incrementFriePladser();

            if(this instanceof Fad) {
                lager.decrementFade();
            }
            else if(this instanceof Flaske) {
                lager.decrementFlasker();
            }
            plads = null;
        }
    }

    public Plads getPlads() {
        return plads;
    }

    public void setPlads(Plads plads) {
        this.plads = plads;
    }
}
