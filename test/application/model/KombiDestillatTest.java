package application.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class KombiDestillatTest {
    private BundDestillat bundDestillat1, bundDestillat2, bundDestillat3;
    private KombiDestillat kombiDestillat1;
    private Fad fad1;


    @BeforeEach
    void setup() {
        bundDestillat1 = new BundDestillat("Bunddestillat1", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        bundDestillat2 = new BundDestillat("Bunddestillat2", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        bundDestillat3 = new BundDestillat("Bunddestillat3", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        fad1 = new Fad(1, "Egetræ", 10, "Spanien");
        fad1.fyldPåFraDestillat(bundDestillat1, 2, LocalDate.now(), "EH");

        kombiDestillat1 = (KombiDestillat) fad1.fyldPåFraDestillat(bundDestillat3, 2, LocalDate.now(), "EH");
    }
    @Test
    void indeholderDestillat_1Kombi1BundIndeholderSomBundDestillat() {
        //Act & assert
        assertTrue(kombiDestillat1.indeholderDestillat(bundDestillat3));
    }

    @Test
    void indeholderDestillat_1Kombi1BundIndeholderKombiDestillat() {
        //Act & assert
        assertTrue(kombiDestillat1.indeholderDestillat(bundDestillat1));
    }

    @Test
    void indeholderDestillat_1Kombi1BundIndeholderIkke() {
        //Arrange
        BundDestillat bundDestillat4 = new BundDestillat("Bunddestillat4", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);

        //Act & assert
        assertFalse(kombiDestillat1.indeholderDestillat(bundDestillat4));
    }

    @Test
    void indeholderDestillat_2KombiIndeholder() {
        //Arrange
        BundDestillat bundDestillat4 = new BundDestillat("Bunddestillat4", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat5 = new BundDestillat("Bunddestillat5", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        fad2.fyldPåFraDestillat(bundDestillat4, 2, LocalDate.now(), "EH");
        fad2.fyldPåFraDestillat(bundDestillat5, 2, LocalDate.now(), "EH");
        kombiDestillat1 = (KombiDestillat) fad1.fyldPåFraFad(fad2, 2, LocalDate.now(), "EH");

        //Act & assert
        assertTrue(kombiDestillat1.indeholderDestillat(bundDestillat4));
    }

    @Test
    void indeholderDestillat_2KombiIndeholderIkke() {
        //Arrange
        BundDestillat bundDestillat4 = new BundDestillat("Bunddestillat4", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat5 = new BundDestillat("Bunddestillat5", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        BundDestillat bundDestillat6 = new BundDestillat("Bunddestillat6", LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,2),"Byg", "Tørv", "EH", 10,
                40);
        Fad fad2 = new Fad(2, "Egetræ", 10, "Italien");
        fad2.fyldPåFraDestillat(bundDestillat4, 2, LocalDate.now(), "EH");
        fad2.fyldPåFraDestillat(bundDestillat5, 2, LocalDate.now(), "EH");
        kombiDestillat1 = (KombiDestillat) fad1.fyldPåFraFad(fad2, 2, LocalDate.now(), "EH");

        //Act & assert
        assertFalse(kombiDestillat1.indeholderDestillat(bundDestillat6));
    }
}