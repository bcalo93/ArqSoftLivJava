package com.compucar.service;

import com.compucar.dao.ClientDao;
import com.compucar.model.Client;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "clients", allEntries = true)
    public Client addClient(Client client) throws EntityNullException, DuplicateElementException {
        if(client == null) {
            throw new EntityNullException("The client is null.");
        }
        if(this.clientDao.findByNumber(client.getNumber()) != null) {
            throw new DuplicateElementException(String.format("Client with number %s", client.getNumber()));
        }
        return this.clientDao.save(client);
    }

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    public Client updateClient(Long clientId, Client client) throws IdNullException, EntityNullException,
            NotFoundException {
        if(clientId == null) {
            throw new IdNullException("The client id is null.");
        }
        if(client == null) {
            throw new EntityNullException("The client is null.");
        }

        Client original = this.clientDao.findOne(clientId);
        if(original == null) {
            throw new NotFoundException(String.format("Client with id %s", clientId));
        }

        original.update(client);
        return this.clientDao.save(original);
    }

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    public void deleteClient(Long clientId) throws IdNullException {
        if(clientId == null) {
            throw new IdNullException("The client id is null.");
        }

        Client toDelete = this.clientDao.findOne(clientId);
        if(toDelete != null) {
            this.clientDao.delete(toDelete);
        }
    }

    @Override
    @Cacheable(value = "clients")
    public Client getClient(Long clientId) throws IdNullException, NotFoundException {
        if(clientId == null) {
            throw new IdNullException("The client id is null.");
        }
        Client result = this.clientDao.findOne(clientId);
        if(result == null) {
            throw new NotFoundException(String.format("Client with id %s", clientId));
        }
        return result;
    }

    @Override
    @Cacheable(value = "clients")
    public List<Client> getAllClients() {
        return this.clientDao.findAll();
    }
}
