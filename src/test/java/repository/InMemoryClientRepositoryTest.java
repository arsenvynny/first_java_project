package repository;

import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.impl.InMemoryClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryClientRepositoryTest {

    private InMemoryClientRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryClientRepository();
    }

    @Test
    void save_newClient_assignsIdAndMakesItFindable() {
        Client client = new Client("Arsen", "Vynnytskyi");

        assertSame(client, repository.save(client));
        assertNotNull(client.getId());
        assertSame(client, repository.findById(client.getId()).orElseThrow());
        assertEquals(1, repository.getCount());
    }

    @Test
    void save_unallocatedId_ExceptionThrows() {
        Client client = new Client("Arsen", "Vynnytskyi");
        client.setId(Long.MAX_VALUE);

        assertThrows(IllegalStateException.class, () -> repository.save(client));
    }
}
