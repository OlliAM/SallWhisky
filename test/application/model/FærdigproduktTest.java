package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FærdigproduktTest {
    private Færdigprodukt færdigprodukt;
    private BundDestillat bundDestillat;
    private Fad fad;
    private Map<Destillat, Fad> anvendteDestillater;

    void setUp() {
        fad = new Fad(1, "Egetræ", 30, "Spanien");
        bundDestillat = new BundDestillat("Navn", LocalDate.of(2025, 1, 1),
                "Byg", "Tørv", "EH");


        færdigprodukt = new Færdigprodukt("Whisky", anvendteDestillater, 10, 60,
                55, 2, "Lækkert regnvand", 20,
                "Salls Whisky", null);
    }

    @Test
    void antalMuligeFlasker_MængdeL_0_9_MindreEndKap() {
        // Arrange
        færdigprodukt = new Færdigprodukt("Whisky", anvendteDestillater, 0.9, 60,
                55, 2, "Lækkert regnvand", 20,
                "Salls Whisky", null);

        // Act
        int expected = 0;
        int actual = færdigprodukt.antalMuligeFlasker(1.0);

        // Assert
        assertEquals(expected, actual);

    }

    @Test
    void antalMuligeFlasker_MængdeL_1_LigMedKap() {
        // Arrange
        færdigprodukt = new Færdigprodukt("Whisky", anvendteDestillater, 1, 60,
                55, 2, "Lækkert regnvand", 20,
                "Salls Whisky", null);

        // Act
        int expected = 1;
        int actual = færdigprodukt.antalMuligeFlasker(1.0);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void antalMuligeFlasker_MængdeL_1_9_StørreEndKap() {
        // Arrange
        færdigprodukt = new Færdigprodukt("Whisky", anvendteDestillater, 1.9, 60,
                55, 2, "Lækkert regnvand", 20,
                "Salls Whisky", null);

        // Act
        int expected = 1;
        int actual = færdigprodukt.antalMuligeFlasker(1.0);

        // Assert
        assertEquals(expected, actual);
    }




}
