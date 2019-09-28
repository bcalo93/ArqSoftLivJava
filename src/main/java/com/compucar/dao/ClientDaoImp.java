package com.compucar.dao;

import com.compucar.model.Client;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Component
public class ClientDaoImp implements ClientDao {
    @Override
    public void addClient(Client client) {
        throw new NotImplementedException();
    }

    @Override
    public void updateClient(Client client) {
        throw new NotImplementedException();
    }

    @Override
    public void removeClient(Client client) {
        throw new NotImplementedException();
    }

    @Override
    public Client getClient(Long clientId) {
        throw new NotImplementedException();
    }

    @Override
    public List<Client> getAllClients() {
        throw new NotImplementedException();
    }
}
