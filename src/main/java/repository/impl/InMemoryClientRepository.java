package repository.impl;

import domain.Client;
import org.springframework.stereotype.Repository;
import repository.ClientRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryClientRepository implements ClientRepository {
    private static long nextId = 1;
    private final Map<Long, Client> clients = new HashMap<>();

    @Override
    public Client save(Client client) {
        if (client.getId() != null) {
            if (client.getId() >= nextId) {
                throw new IllegalStateException("Invalid client id");
            }
            clients.put(client.getId(), client);
            return client;
        }

        client.setId(nextId++);
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public int getCount() {
        return clients.size();
    }
}
