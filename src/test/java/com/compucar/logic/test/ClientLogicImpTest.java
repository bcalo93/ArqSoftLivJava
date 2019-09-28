package com.compucar.logic.test;

import com.compucar.dao.ClientDao;
import com.compucar.model.Client;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ClientLogicImpTest {

    // TODO - This is just a sample for Mockito framework, remove later
    @Test
    public void sampleMockito() {
        ClientDao dao = mock(ClientDao.class);
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        when(dao.getAllClients()).thenReturn(clients);

        Assert.assertEquals(1, dao.getAllClients().size());

        verify(dao, times(1)).getAllClients();
        verifyNoMoreInteractions(dao);
    }
}
