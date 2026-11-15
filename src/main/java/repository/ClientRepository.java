package repository;

import domain.Client;

import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long id);
    int getCount();
}
