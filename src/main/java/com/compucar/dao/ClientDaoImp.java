package com.compucar.dao;

import com.compucar.model.Client;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Component
public class ClientDaoImp implements ClientDao {

    @Autowired
    private SessionFactory sessionFactory;

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
