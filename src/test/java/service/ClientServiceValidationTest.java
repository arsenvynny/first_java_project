package service;

import domain.Client;
import org.junit.jupiter.api.Test;
import service.impl.ClientServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientServiceValidationTest {

    @Test
    void createClient_validInput_setsClientFields() {
        Client client = new ClientServiceImpl().createClient("Arsen", "Vynnytskyi");

        assertEquals("Arsen", client.getName());
        assertEquals("Vynnytskyi", client.getSurName());
    }

    @Test
    void createClient_nullOrEmptyName_throwsIllegalArgumentException() {
        var service = new ClientServiceImpl();
        assertThrows(IllegalArgumentException.class,
                () -> service.createClient(null, "Vynnytskyi"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createClient("", "Vynnytskyi"));
    }

    @Test
    void createClient_nullOrEmptySurname_throwsIllegalArgumentException() {
        var service = new ClientServiceImpl();
        assertThrows(IllegalArgumentException.class,
                () -> service.createClient("Arsen", null));
        assertThrows(IllegalArgumentException.class,
                () -> service.createClient("Arsen", ""));
    }
}
