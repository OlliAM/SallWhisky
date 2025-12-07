package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FadTest {
    private Fad fad;
    private BundDestillat bundDestillat;

    @BeforeEach
    void setUp() {
        fad = new Fad(1, "Egetræ", 10, "Spanien");
        bundDestillat = new BundDestillat("Navn", LocalDate.of(2025,1,1),
                "Byg", "Tørv", "EH");

    }

    @Test
    void addToHistorik_nullDato() {
        //Act && assert
        assertTrue(fad.getIndholdshistorik().isEmpty());

        fad.addToHistorik(bundDestillat, null);
        LocalDate expectedDato = LocalDate.now();
        Drinkable actual = fad.getIndholdshistorik().get(expectedDato).getFirst();

        assertTrue(fad.getIndholdshistorik().containsKey(expectedDato));
        assertEquals(bundDestillat, actual);
    }

    @Test
    void addToHistorik_EksisterendeDrinkablePåDato() {
        //Arrange
        LocalDate dato = LocalDate.now();
        BundDestillat bundDestillat2 = new BundDestillat("Navn2", LocalDate.of(2025,1,2),
                "Hvede", "Tørv", "EH");

        //Act && assert
        fad.addToHistorik(bundDestillat, null);
        assertEquals(1, fad.getIndholdshistorik().get(dato).size());

        fad.addToHistorik(bundDestillat, null);
        assertEquals(1, fad.getIndholdshistorik().get(dato).size());

        fad.addToHistorik(bundDestillat2, null);
        assertEquals(2, fad.getIndholdshistorik().get(dato).size());


    }

    @Test
    void addToHistorik_MINDato() {
        //Arrange

        //Act && assert

    }

    @Test
    void removeFromHistorik() {
    }

    @Test
    void fyldPåFraFad() {
    }

    @Test
    void fyldPåFraDestillat() {
    }
}