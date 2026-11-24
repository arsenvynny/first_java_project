package domain.client;

import domain.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClientTest {
    @Test
    void noArgsConstructorInitializesFieldsToNull() {
        Client client = new Client();

        assertNull(client.getId());
        assertNull(client.getName());
        assertNull(client.getSurName());
    }

    @Test
    void allArgsConstructorAccessorsAndFullNameMethodWork() {
        Client client = new Client(3L, "Grace", "Hopper");

        assertEquals(3L, client.getId());
        assertEquals("Grace", client.getName());
        assertEquals("Hopper", client.getSurName());
        assertEquals("Grace Hopper", client.getFullName());

        client.setId(4L);
        client.setName("Katherine");
        client.setSurName("Johnson");

        assertEquals(4L, client.getId());
        assertEquals("Katherine", client.getName());
        assertEquals("Johnson", client.getSurName());
        assertEquals("Katherine Johnson", client.getFullName());

        client.setName(null);
        assertEquals("null Johnson", client.getFullName());
    }
}
