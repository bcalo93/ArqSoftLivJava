package com.compucar.dao;

import com.compucar.model.Client;

import java.util.List;

public interface ClientDao {

    void addClient(Client client);

    void updateClient(Client client);

    void removeClient(Client client);

    Client getClient(Long clientId);

    List<Client> getAllClients();
}
