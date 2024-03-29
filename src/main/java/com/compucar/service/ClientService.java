package com.compucar.service;

import com.compucar.model.Client;
import com.compucar.service.exceptions.*;

import java.util.List;

public interface ClientService {

    Client addClient(Client client) throws EntityNullException, DuplicateElementException, RequiredFieldMissingException;

    Client updateClient(Long clientId, Client client) throws IdNullException, EntityNullException, NotFoundException;

    void deleteClient(Long clientId) throws IdNullException;

    Client getClient(Long clientId) throws NotFoundException, IdNullException;

    List<Client> getAllClients();
}
