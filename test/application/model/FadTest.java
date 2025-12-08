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
                LocalDate.of(2025,1,2), "Byg", "Tørv", "EH",
                50, 50);

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
    void removeFromHistorik() {
        //Arrange
        LocalDate dato = LocalDate.now();
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        fad.addToHistorik(bundDestillat, dato);

        //Act && assert
        fad.removeFromHistorik(bundDestillat, dato);
        assertTrue(fad.getIndholdshistorik().get(dato).isEmpty());

        fad2.removeFromHistorik(bundDestillat, dato);
        assertTrue(fad.getIndholdshistorik().get(dato).isEmpty());

    }

    @Test
    void fyldPåFraDestillat_korrektPåfyldningAfTomtFad() {
        //Arrange
        LocalDate dato = LocalDate.of(2025,2,3);

        //Act && assert
        assertNull(fad.getFadIndhold());
        Destillat expectedDestillat = fad.fyldPåFraDestillat(bundDestillat, 10, dato, "EH");

        assertEquals(bundDestillat, expectedDestillat);
        assertEquals(fad.getFadIndhold(), expectedDestillat);
        assertEquals(10, fad.getMængdeL());
        assertEquals(40, bundDestillat.getMængdeL());
        assertTrue(fad.getIndholdshistorik().get(dato).contains(expectedDestillat));
    }

    @Test
    void fyldPå_nullDato() {
        //Arrange
        LocalDate dato = LocalDate.now();

        //Act
        fad.fyldPåFraDestillat(bundDestillat, 10, null, "EH");

        //Assert
        assertTrue(fad.getIndholdshistorik().get(dato).contains(bundDestillat));
    }

    @Test
    void fyldPåFraFad_korrektPåfyldningAfTomtFad() {
        //Arrange
        LocalDate dato = LocalDate.of(2025,2,3);
        Fad fad2 = new Fad(2, "Egetræ", 50, "Italien");
        fad.fyldPåFraDestillat(bundDestillat, 10, dato, "EH");

        //Act
        Destillat expectedDestillat = fad2.fyldPåFraFad(fad, 5, dato, "BC");

        //Assert
        assertEquals(fad.getFadIndhold(), expectedDestillat);
        assertEquals(fad2.getFadIndhold(), expectedDestillat);
        assertEquals(5, fad2.getMængdeL());
        assertEquals(5, fad.getMængdeL());
        assertTrue(fad2.getIndholdshistorik().get(dato).contains(expectedDestillat));
    }

    @Test
    void fyldPå_påfyldningsMængdeStørreEndKildeMængde() {
        //Arrange
        BundDestillat bundDestillat2 = new BundDestillat("Navn2", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2), "Byg", "Tørv", "EH",
                5, 50);
        LocalDate dato = LocalDate.of(2025,2,3);

        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> fad.fyldPåFraDestillat(bundDestillat2,
                6, dato, "BC"));

        assertEquals("Destillat/Fad har mindre indhold end den ønskede mængde", exception.getMessage());
    }

    @Test
    void fyldPå_påfyldningsMængdeStørreEndResterendeKapacitet() {
        //Arrange
        LocalDate dato = LocalDate.of(2025,2,3);
        fad.fyldPåFraDestillat(bundDestillat, 5, dato, "EH");


        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> fad.fyldPåFraDestillat(bundDestillat,
                7, dato, "BC"));

        assertEquals("Fadet har ikke plads til den ønskede mængde", exception.getMessage());
    }


    @Test
    void fyldPå_fyldPåFadMedIndhold() {
        //Arrange
        LocalDate dato = LocalDate.of(2025,2,3);
        BundDestillat bundDestillat2 = new BundDestillat("Navn2", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2), "Byg", "Tørv", "EH",
                5, 40);
        fad.fyldPåFraDestillat(bundDestillat, 5, dato, "EH");

        //Act
        Destillat expectedDestillat = fad.fyldPåFraDestillat(bundDestillat2, 5, dato, "BC");

        //Assert
        assertInstanceOf(KombiDestillat.class, expectedDestillat);
        assertNotEquals(bundDestillat, expectedDestillat);
        assertNotEquals(bundDestillat2, expectedDestillat);
        assertEquals(Maltbatch.SINGLE_MALT, expectedDestillat.getMaltbatch());
        assertEquals(dato, expectedDestillat.getFærdigDato());
        assertEquals(bundDestillat.getNavn() + "-" + bundDestillat2.getNavn(), expectedDestillat.getNavn());
        assertEquals("BC", expectedDestillat.getInit());
        assertEquals(45, expectedDestillat.getAlkoholprocent());
    }

    @Test
    void fyldPå_indholdSinglePåfyldningIkkeSingleResultatBlended() {
        //Arrange
        LocalDate dato = LocalDate.of(2025, 2, 3);
        BundDestillat bundDestillat2 = new BundDestillat("Navn2", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Hvede", "Tørv", "EH",
                5, 40);
        fad.fyldPåFraDestillat(bundDestillat, 5, dato, "EH");

        //Act
        Destillat expectedDestillat = fad.fyldPåFraDestillat(bundDestillat2, 5, dato, "BC");

        //Assert
        assertEquals(Maltbatch.BLENDED, expectedDestillat.getMaltbatch());
    }

    @Test
    void fyldPå_indholdIkkeSinglePåfyldningSingleResultatBlended() {
        //Arrange
        LocalDate dato = LocalDate.of(2025, 2, 3);
        BundDestillat bundDestillat2 = new BundDestillat("Navn2", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Hvede", "Tørv", "EH",
                5, 40);
        fad.fyldPåFraDestillat(bundDestillat2, 5, dato, "EH");

        //Act
        Destillat expectedDestillat = fad.fyldPåFraDestillat(bundDestillat, 5, dato, "BC");

        //Assert
        assertEquals(Maltbatch.BLENDED, expectedDestillat.getMaltbatch());
    }

    @Test
    void fyldPå_indholdIkkeSinglePåfyldningIkkeSingleResultatBlended() {
        //Arrange
        LocalDate dato = LocalDate.of(2025, 2, 3);
        BundDestillat bundDestillat2 = new BundDestillat("Navn2", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Hvede", "Tørv", "EH",
                5, 40);
        BundDestillat bundDestillat3 = new BundDestillat("Navn3", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Hvede", "Tørv", "EH",
                5, 40);
        fad.fyldPåFraDestillat(bundDestillat2, 5, dato, "EH");


        //Act
        Destillat expectedDestillat = fad.fyldPåFraDestillat(bundDestillat3, 5, dato, "BC");

        //Assert
        assertEquals(Maltbatch.BLENDED, expectedDestillat.getMaltbatch());
    }
}