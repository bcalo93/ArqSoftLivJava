package com.compucar.service;

import com.compucar.dao.ClientDao;
import com.compucar.exception.EntityNotExistException;
import com.compucar.exception.EntityNullException;
import com.compucar.exception.IdNullException;
import com.compucar.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Component
public class ClientServiceImp implements ClientService {

    @Autowired
    private ClientDao clientDao;

    public ClientServiceImp(ClientDao dao) {
        this.clientDao = dao;
    }

    @Override
    public Client addClient(Client client) throws EntityNullException {
        if(client == null) {
            throw new EntityNullException("El cliente es null.");
        }
        return this.clientDao.save(client);
    }

    @Override
    public Client updateClient(Long clientId, Client client) throws IdNullException, EntityNullException,
            EntityNotExistException {
        if(clientId == null) {
            throw new IdNullException("El id del cliente es null.");
        }
        if(client == null) {
            throw new EntityNullException("El cliente es null.");
        }

        Client original = this.clientDao.findOne(clientId);
        if(original == null) {
            throw new EntityNotExistException(String.format("El cliente con id %s no existe.", clientId));
        }

        original.update(client);
        return this.clientDao.save(original);
    }

    @Override
    @CacheEvict("clients")
    public void deleteClient(Long clientId) throws IdNullException {
        if(clientId == null) {
            throw new IdNullException("El id del cliente es null.");
        }

        this.clientDao.delete(clientId);
    }

    @Override
    public Client getClient(Long clientId) throws IdNullException, EntityNotExistException {
        if(clientId == null) {
            throw new IdNullException("El id del cliente es null.");
        }
        Client result = this.clientDao.findOne(clientId);
        if(result == null) {
            throw new EntityNotExistException(String.format("A client with id %s doesn't exist", clientId));
        }
        return result;
    }

    @Override
    @Cacheable("clients")
    public List<Client> getAllClients() {
        return this.clientDao.findAll();
    }
}
