package service.impl;

import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ClientRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock private ClientRepository repository;
    private ClientServiceImpl service;

    @BeforeEach
    void setUp() { service = new ClientServiceImpl(repository); }

    @Test
    void createAndUpdatePersistValidClients() {
        Client fresh = client(null, "Ada", "Lovelace");
        when(repository.save(fresh)).thenReturn(fresh);
        assertSame(fresh, service.create(fresh));

        Client update = client(5L, "Grace", "Hopper");
        when(repository.existsById(5L)).thenReturn(true);
        when(repository.save(update)).thenReturn(update);
        assertSame(update, service.update(update));
        verify(repository).save(fresh);
        verify(repository).save(update);
    }

    @Test
    void createRejectsPersistedClientAndInvalidFields() {
        assertThrows(IllegalArgumentException.class, () -> service.create(client(1L, "Ada", "Lovelace")));
        assertInvalid(c -> c.setName(null));
        assertInvalid(c -> c.setName(" "));
        assertInvalid(c -> c.setName("n".repeat(31)));
        assertInvalid(c -> c.setSurName(null));
        assertInvalid(c -> c.setSurName(" "));
        assertInvalid(c -> c.setSurName("s".repeat(51)));
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
        verifyNoInteractions(repository);
    }

    @Test
    void updateRequiresExistingPersistedClient() {
        assertThrows(IllegalArgumentException.class, () -> service.update(client(null, "Ada", "Lovelace")));
        when(repository.existsById(4L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.update(client(4L, "Ada", "Lovelace")));
        verify(repository, never()).save(any());
    }

    @Test
    void findAllFindByIdAndDeleteUseRepository() {
        Client client = client(2L, "Ada", "Lovelace");
        when(repository.findById(2L)).thenReturn(Optional.of(client));
        when(repository.findAll()).thenReturn(List.of(client));
        assertSame(client, service.findById(2L));
        assertEquals(List.of(client), service.findAll());
        service.delete(2L);
        verify(repository).delete(client);

        when(repository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(8L));
        assertThrows(IllegalArgumentException.class, () -> service.delete(8L));
    }

    private void assertInvalid(java.util.function.Consumer<Client> mutation) {
        Client client = client(null, "Ada", "Lovelace");
        mutation.accept(client);
        assertThrows(IllegalArgumentException.class, () -> service.create(client));
    }

    private static Client client(Long id, String name, String surname) {
        return new Client(id, name, surname);
    }
}
