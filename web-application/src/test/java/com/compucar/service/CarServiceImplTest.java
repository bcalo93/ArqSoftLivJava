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
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.Period;
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
    private Environment env;

    @Before
    public void initTest() {
        this.carServiceDao = mock(CarServiceDao.class);
        this.clientDao = mock(ClientDao.class);
        this.mechanicDao = mock(MechanicDao.class);
        this.readerDao = mock(ReaderDao.class);
        this.workshopDao = mock(WorkshopDao.class);
        this.env = mock(Environment.class);
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

        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);
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

        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);
        CarService result = service.getService(carServiceId);

        Assert.assertEquals(carServiceId, result.getId());
        Assert.assertEquals("SF43", result.getCode());

        verify(carServiceDao, times(1)).findById(carServiceId);
    }

    @Test(expected = NotFoundException.class)
    public void getServiceById_nonExistent() throws NotFoundException {
        Long carServiceId = 50L;
        when(carServiceDao.findById(carServiceId)).thenReturn(Optional.empty());

        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);
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
        carServiceClient.setType(ClientType.PERSON);
        carServiceWorkshop.setCode("WP90");
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(300);
        carServiceReader.setWorkshop(carServiceWorkshop);
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
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        CarService result = service.addService(newCarService);
        Assert.assertEquals(newCarService, result);
        Assert.assertEquals(result.getReader().getActualTimeUse(), 180);

        verify(carServiceDao, times(1)).existsByCode(newCarService.getCode());
        verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
        verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
        verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
        verify(carServiceDao, times(1)).save(newCarService);
        verify(env, times(1)).getProperty("reader.minBatteryLife");
    }

    @Test(expected = NotFoundException.class)
    public void addService_nonExistentClient() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();

        carServiceClient.setNumber(1122);
        newCarService.setClient(carServiceClient);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.empty());
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
        }
    }

    @Test(expected = NotFoundException.class)
    public void addService_nonExistentWorkshop() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.empty());
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
        }
    }

    @Test(expected = NotFoundException.class)
    public void addService_nonExistentMechanic() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.empty());
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
        }
    }

    @Test(expected = NotFoundException.class)
    public void addService_nonExistentReader() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.empty());
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        }
    }

    @Test(expected = DuplicateElementException.class)
    public void addService_duplicateCode() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(carServiceDao.existsByCode(newCarService.getCode())).thenReturn(true);
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(carServiceDao, times(1)).existsByCode(newCarService.getCode());
        }
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addService_nullCode() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        }
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addService_nullDate() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        }
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addService_nullClient() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        }
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addService_nullReader() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
        }
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addService_nullWorkshop() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setClient(carServiceClient);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        }
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addService_nullMechanic() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_dateAfterToday() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.now().plus(Period.ofDays(1)));
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_negativeServiceTime() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(-1);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_negativeCost() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(180);
        newCarService.setCost(-100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_unavailableReader() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(90);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(1);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_insufficientReaderBattery() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(0);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(110);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_workshopDifferentThanReaderWorkshop() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();
        Workshop otherWorkshop = new Workshop();

        otherWorkshop.setId(111L);
        otherWorkshop.setCode("RR55");
        carServiceClient.setNumber(1122);
        carServiceWorkshop.setId(999L);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(0);
        carServiceReader.setWorkshop(otherWorkshop);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(10);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_mechanicWithMoreThen5ServicesOnDate() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(0);
        carServiceReader.setWorkshop(carServiceWorkshop);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(10);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        when(carServiceDao.countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class))).thenReturn(6);
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
            verify(carServiceDao, times(1)).countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class));
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_mechanicWithServiceOnDifferentWorkshopOnDate() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(0);
        carServiceReader.setWorkshop(carServiceWorkshop);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(10);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        when(carServiceDao.countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class))).thenReturn(0);
        when(carServiceDao.existsByMechanicAndDateBetweenAndWorkshopNot(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class), isA(Workshop.class))).thenReturn(true);
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
            verify(carServiceDao, times(1)).countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class));
            verify(carServiceDao, times(1)).existsByMechanicAndDateBetweenAndWorkshopNot(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class), isA(Workshop.class));
        }
    }

    @Test(expected = InvalidFieldValueException.class)
    public void addService_clientPersonWithServiceOnDate() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceClient.setType(ClientType.PERSON);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(0);
        carServiceReader.setWorkshop(carServiceWorkshop);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(10);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        when(carServiceDao.countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class))).thenReturn(0);
        when(carServiceDao.existsByMechanicAndDateBetweenAndWorkshopNot(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class), isA(Workshop.class))).thenReturn(false);
        when(carServiceDao.existsByClientAndDateBetween(isA(Client.class), isA(LocalDateTime.class), isA(LocalDateTime.class))).thenReturn(true);
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        try {
            CarService result = service.addService(newCarService);
        } finally {
            verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
            verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
            verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
            verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
            verify(env, times(1)).getProperty("reader.minBatteryLife");
            verify(carServiceDao, times(1)).countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class));
            verify(carServiceDao, times(1)).existsByMechanicAndDateBetweenAndWorkshopNot(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class), isA(Workshop.class));
            verify(carServiceDao, times(1)).existsByClientAndDateBetween(isA(Client.class), isA(LocalDateTime.class), isA(LocalDateTime.class));
        }
    }

    @Test
    public void addService_clientCompanyApplyDiscount() throws DuplicateElementException, InvalidFieldValueException, NotFoundException, RequiredFieldMissingException {
        CarService newCarService = new CarService();
        Client carServiceClient = new Client();
        Workshop carServiceWorkshop = new Workshop();
        Mechanic carServiceMechanic = new Mechanic();
        Reader carServiceReader = new Reader();

        carServiceClient.setNumber(1122);
        carServiceClient.setType(ClientType.COMPANY);
        carServiceWorkshop.setCode("WP90");
        carServiceMechanic.setNumber(2233);
        carServiceReader.setCode("RDR54");
        carServiceReader.setBatteryLife(100);
        carServiceReader.setActualTimeUse(0);
        carServiceReader.setWorkshop(carServiceWorkshop);
        newCarService.setCode("SF43");
        newCarService.setDate(LocalDateTime.of(2019, 9, 9, 10, 0));
        newCarService.setServiceTime(10);
        newCarService.setCost(100.0);
        newCarService.setClient(carServiceClient);
        newCarService.setWorkshop(carServiceWorkshop);
        newCarService.setMechanic(carServiceMechanic);
        newCarService.setReader(carServiceReader);

        when(clientDao.findByNumber(carServiceClient.getNumber())).thenReturn(Optional.of(carServiceClient));
        when(workshopDao.findByCode(carServiceWorkshop.getCode())).thenReturn(Optional.of(carServiceWorkshop));
        when(mechanicDao.findByNumber(carServiceMechanic.getNumber())).thenReturn(Optional.of(carServiceMechanic));
        when(readerDao.findByCode(carServiceReader.getCode())).thenReturn(Optional.of(carServiceReader));
        when(env.getProperty("reader.minBatteryLife")).thenReturn("20");
        when(carServiceDao.countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class))).thenReturn(0);
        when(carServiceDao.existsByMechanicAndDateBetweenAndWorkshopNot(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class), isA(Workshop.class))).thenReturn(false);
        when(carServiceDao.countByClientAndDateBetween(isA(Client.class), isA(LocalDateTime.class), isA(LocalDateTime.class))).thenReturn(6);
        when(carServiceDao.save(isA(CarService.class))).thenReturn(newCarService);
        CarServiceService service = new CarServiceServiceImpl(carServiceDao, clientDao, mechanicDao, readerDao,
                workshopDao, env);

        CarService result = service.addService(newCarService);
        Assert.assertEquals(newCarService.getCost(), 80, 0);

        verify(clientDao, times(1)).findByNumber(carServiceClient.getNumber());
        verify(workshopDao, times(1)).findByCode(carServiceWorkshop.getCode());
        verify(mechanicDao, times(1)).findByNumber(carServiceMechanic.getNumber());
        verify(readerDao, times(1)).findByCode(carServiceReader.getCode());
        verify(env, times(1)).getProperty("reader.minBatteryLife");
        verify(carServiceDao, times(1)).countByMechanicAndDateBetween(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class));
        verify(carServiceDao, times(1)).existsByMechanicAndDateBetweenAndWorkshopNot(isA(Mechanic.class), isA(LocalDateTime.class), isA(LocalDateTime.class), isA(Workshop.class));
        verify(carServiceDao, times(1)).save(newCarService);
    }
}