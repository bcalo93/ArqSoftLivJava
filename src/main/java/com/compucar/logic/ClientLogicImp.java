package com.compucar.logic;

import com.compucar.dao.ClientDao;
import com.compucar.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Component
public class ClientLogicImp implements ClientLogic {

    @Autowired
    private ClientDao clientDao;

    @Override
    public Client addClient(Client client) {
        throw new NotImplementedException();
    }

    @Override
    public Client updateClient(Long clientId, Client client) {
        throw new NotImplementedException();
    }

    @Override
    @CacheEvict("clients")
    public void deleteClient(Long clientId) {
        throw new NotImplementedException();
    }

    @Override
    public Client getClient(Long clientId) {
        throw new NotImplementedException();
    }

    @Override
    @Cacheable("clients")
    public List<Client> getAllClients() {
        throw new NotImplementedException();
    }
}
