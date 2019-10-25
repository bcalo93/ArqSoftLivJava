package com.compucar.service;

import com.compucar.dao.ClientDao;
import com.compucar.model.Client;
import com.compucar.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientServiceImp implements ClientService {

    @Autowired
    private ClientDao clientDao;

    public ClientServiceImp(ClientDao dao) {
        this.clientDao = dao;
    }

    @Override
    public Client addClient(Client client) throws EntityNullException, DuplicateElementException, RequiredFieldMissingException {
        if (client == null) {
            throw new EntityNullException("The client is null.");
        }
        if (client.getNumber() == null) {
            throw new RequiredFieldMissingException("Number");
        }
        if (this.clientDao.findByNumber(client.getNumber()).isPresent()) {
            throw new DuplicateElementException(String.format("Client with number %s", client.getNumber()));
        }
        return this.clientDao.save(client);
    }

    @Override
    public Client updateClient(Long clientId, Client client) throws IdNullException, EntityNullException,
            NotFoundException {
        if (clientId == null) {
            throw new IdNullException("The client id is null.");
        }
        if (client == null) {
            throw new EntityNullException("The client is null.");
        }

        Client original = this.clientDao.findOne(clientId);
        if (original == null) {
            throw new NotFoundException(String.format("Client with id %s", clientId));
        }

        original.update(client);
        return this.clientDao.save(original);
    }

    @Override
    public void deleteClient(Long clientId) throws IdNullException {
        if (clientId == null) {
            throw new IdNullException("The client id is null.");
        }

        Client toDelete = this.clientDao.findOne(clientId);
        if (toDelete != null) {
            this.clientDao.delete(toDelete);
        }
    }

    @Override
    public Client getClient(Long clientId) throws IdNullException, NotFoundException {
        if (clientId == null) {
            throw new IdNullException("The client id is null.");
        }
        Client result = this.clientDao.findOne(clientId);
        if (result == null) {
            throw new NotFoundException(String.format("Client with id %s", clientId));
        }
        return result;
    }

    @Override
    public List<Client> getAllClients() {
        return this.clientDao.findAll();
    }
}
