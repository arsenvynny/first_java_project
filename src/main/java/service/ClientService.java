package service;

import domain.Client;
import dto.ClientDTO;

import java.util.List;

public interface ClientService {
    Client create(ClientDTO clientDTO);
    Client findById(Long id);
    List<Client> findAll();
    Client update(ClientDTO clientDTO);
    void delete(Long id);
}
