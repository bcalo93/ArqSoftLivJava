package com.compucar.logic;

import com.compucar.exception.EntityNullException;
import com.compucar.model.Client;

import java.util.List;

public interface ClientLogic {

    Client addClient(Client client) throws EntityNullException;

    Client updateClient(Long clientId, Client client);

    void deleteClient(Long clientId);

    Client getClient(Long clientId);

    List<Client> getAllClients();
}
