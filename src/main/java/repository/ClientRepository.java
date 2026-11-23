package repository;

import domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client save(Client client);
    Optional<Client> findById(Long id);

}
