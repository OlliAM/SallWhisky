package application.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BundDestillatTest {

    @Test
    void setFærdigDato() {
        //Arrange
        BundDestillat bundDestillat = new BundDestillat("Bunddestillat", LocalDate.of(2025, 1, 1),
                "Byg", "Tørv", "EH");
        LocalDate færdigDato = LocalDate.of(2024,12,1);

        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> bundDestillat.setFærdigDato(færdigDato));

        //Assert
        assertEquals("færdig dato er før startdato", exception.getMessage());
    }
}