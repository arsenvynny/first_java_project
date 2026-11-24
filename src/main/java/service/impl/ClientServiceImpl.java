package service.impl;

import domain.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ClientRepository;
import service.ClientService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    @Override
    public Client create(Client client) {
        validate(client);
        if (client.getId() != null) {
            throw new IllegalArgumentException("A new client must not already have an id");
        }
        return repository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client update(Client client) {
        validate(client);
        if (client.getId() == null || !repository.existsById(client.getId())) {
            throw new IllegalArgumentException("A persisted client must be specified for update");
        }
        return repository.save(client);
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client must be specified");
        }
        if (client.getName() == null || client.getName().isBlank() || client.getName().length() > 30) {
            throw new IllegalArgumentException("Client name must contain 1 to 30 characters");
        }
        if (client.getSurName() == null || client.getSurName().isBlank()
                || client.getSurName().length() > 50) {
            throw new IllegalArgumentException("Client surname must contain 1 to 50 characters");
        }
    }
}
