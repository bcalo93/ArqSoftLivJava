package com.compucar.service;

import com.compucar.exception.EntityNotExistException;
import com.compucar.exception.EntityNullException;
import com.compucar.exception.IdNullException;
import com.compucar.model.Client;

import java.util.List;

public interface ClientService {

    Client addClient(Client client) throws EntityNullException;

    Client updateClient(Long clientId, Client client) throws IdNullException, EntityNullException, EntityNotExistException;

    void deleteClient(Long clientId) throws IdNullException;

    Client getClient(Long clientId) throws EntityNotExistException, IdNullException;

    List<Client> getAllClients();
}
