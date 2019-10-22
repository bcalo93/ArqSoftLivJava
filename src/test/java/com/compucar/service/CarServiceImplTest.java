package com.compucar.service;

import com.compucar.dao.*;
import com.compucar.model.*;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CarServiceImplTest {

    private CarServiceDao carServiceDao;
    private ClientDao clientDao;
    private MechanicDao mechanicDao;
    private ReaderDao readerDao;
    private WorkshopDao workshopDao;

    @Before
    public void initTest() {
        this.carServiceDao = mock(CarServiceDao.class);
        this.clientDao = mock(ClientDao.class);
        this.mechanicDao = mock(MechanicDao.class);
        this.readerDao = mock(ReaderDao.class);
        this.workshopDao = mock(WorkshopDao.class);
    }

    @Test
    public void getAllServices_ok() {
        List<CarService> mockList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CarService carService = new CarService();
            carService.setCode("Serv" + i);
            mockList.add(carService);
        }
        when(carServiceDao.findAll()).thenReturn(mockList);

        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao, workshopDao);
        List<CarService> result = service.listServices();

        Assert.assertEquals(10, result.size());
        Assert.assertEquals(mockList, result);

        verify(carServiceDao, times(1)).findAll();
    }

    @Test
    public void getServiceById_ok() throws NotFoundException {
        Long carServiceId = 50L;
        CarService carService = new CarService();
        carService.setId(carServiceId);
        carService.setCode("SF43");
        when(carServiceDao.findById(carServiceId)).thenReturn(Optional.of(carService));

        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao, workshopDao);
        CarService result = service.getService(carServiceId);

        Assert.assertEquals(carServiceId, result.getId());
        Assert.assertEquals("SF43", result.getCode());

        verify(carServiceDao, times(1)).findById(carServiceId);
    }

    @Test(expected = NotFoundException.class)
    public void getServiceById_nonExistent() throws NotFoundException {
        Long carServiceId = 50L;
        when(carServiceDao.findById(carServiceId)).thenReturn(Optional.empty());

        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao, workshopDao);
        try {
            CarService result = service.getService(carServiceId);
        } finally {
            verify(carServiceDao, times(1)).findById(carServiceId);
        }
    }

    @Test
    public void addService_ok() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Reader carServiceReader = new Reader();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();

        carServiceClient.setNumber(1122);
        carServiceReader.setCode("RDR54");
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(180);
        newCarService.setCost(1800.0);
        newCarService.setClient(carServiceClient);
        newCarService.setReader(carServiceReader);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);

        when(carServiceDao.existsByCode(newCarService.getCode())).thenReturn(false);
        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(carServiceDao.save(isA(CarService.class))).thenReturn(newCarService);
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao, workshopDao);

        CarService result = service.addService(newCarService);
        Assert.assertEquals(newCarService, result);

        verify(carServiceDao, times(1)).existsByCode(newCarService.getCode());
        verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
        verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
        verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
        verify(carServiceDao, times(1)).save(newCarService);
    }
}
