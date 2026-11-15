package domain.client;

import domain.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreatingClientTest {

    @Test
    void Client_constructor_setsClientFields() {

        String name = "Arsen";
        String surName = "Vynnytskyi";

        Client client = new Client(name, surName);

        assertEquals(name, client.getName());
        assertEquals(surName, client.getSurName());
    }

    @Test
    void Client_constructorWithNullName_ExceptionThrows() {

        String name = null;
        String surName = "Vynnytskyi";

        assertThrows(IllegalArgumentException.class,
                () -> new Client(name, surName));

        String name2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Client(name2, surName));
    }

    @Test
    void Client_constructorWithNullSurname_ExceptionThrows() {

        String name = "Arsen";
        String surName = null;

        assertThrows(IllegalArgumentException.class,
                () -> new Client(name, surName));

        String surName2 = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Client(name, surName2));
    }
}
