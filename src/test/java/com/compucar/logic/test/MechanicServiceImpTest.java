package com.compucar.logic.test;

import com.compucar.builder.MechanicBuilder;
import com.compucar.dao.MechanicDao;
import com.compucar.model.Mechanic;
import com.compucar.service.MechanicService;
import com.compucar.service.MechanicServiceImp;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

public class MechanicServiceImpTest {
    private MechanicDao dao;

    @Before
    public void initTest() {
        this.dao = mock(MechanicDao.class);
    }
    @Test
    public void addMechanicOkTest() throws EntityNullException, DuplicateElementException {
        Long expectedId = 20L;
        Calendar expectedDate = new GregorianCalendar(2018, GregorianCalendar.JUNE, 27);
        when(dao.findByNumber(anyInt())).thenReturn(null);
        when(dao.save(isA(Mechanic.class))).thenReturn(
                new MechanicBuilder()
                        .id(expectedId)
                        .name("Test Mechanic")
                        .startDate(expectedDate)
                        .number(100)
                        .phone("21351235")
                        .build()
        );
        MechanicService service = new MechanicServiceImp(dao);

        Mechanic toAdd = new MechanicBuilder()
                .name("Test Mechanic")
                .number(100)
                .startDate(expectedDate)
                .phone("21351235")
                .build();

        Mechanic result = service.addMechanic(toAdd);
        Assert.assertEquals(expectedId, result.getId());
        Assert.assertEquals("Test Mechanic", result.getName());
        Assert.assertEquals(100, result.getNumber());
        Assert.assertEquals("21351235", result.getPhone());
        Assert.assertEquals(expectedDate, result.getStartDate());

        verify(dao, times(1)).findByNumber(100);
        verify(dao, times(1)).save(toAdd);
    }

    @Test(expected = EntityNullException.class)
    public void addMechanicNullTest() throws EntityNullException, DuplicateElementException {
        MechanicService service = new MechanicServiceImp(dao);
        service.addMechanic(null);
    }

    @Test
    public void createDuplicateMechanicTest() throws EntityNullException {
        boolean exceptionThrown = false;
        int existingNumber = 50;
        when(dao.findByNumber(existingNumber)).thenReturn(
                new MechanicBuilder()
                        .name("Test Client Exist")
                        .startDate(new GregorianCalendar(2017, GregorianCalendar.JANUARY, 19))
                        .number(existingNumber)
                        .phone("220003732")
                        .build()
        );

        MechanicService service = new MechanicServiceImp(dao);
        try {
            service.addMechanic(new MechanicBuilder()
                    .name("Test Mechanic")
                    .startDate(new GregorianCalendar(2019, GregorianCalendar.OCTOBER, 10))
                    .number(existingNumber)
                    .phone("200503132")
                    .build()
            );
        } catch(DuplicateElementException de) {
            Assert.assertEquals(String.format("Mechanic with number %s already exists.", existingNumber),
                    de.getMessage());
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        verify(dao, times(1)).findByNumber(existingNumber);
    }

    @Test
    public void updateMechanicOkTest() throws IdNullException, NotFoundException, EntityNullException {
        Long expectedId = 32L;
        Calendar newDate = new GregorianCalendar(2017, Calendar.OCTOBER,31);

        when(dao.findOne(expectedId)).thenReturn(new MechanicBuilder()
                .id(expectedId)
                .name("Test Mechanic Update")
                .startDate(new GregorianCalendar(2017, Calendar.JUNE,20))
                .number(30)
                .phone("200003132")
                .build()
        );

        when(dao.save(isA(Mechanic.class))).thenReturn(new MechanicBuilder()
                .id(expectedId)
                .name("New Mechanic Name")
                .startDate(newDate)
                .number(30)
                .phone("1534567820")
                .build()
        );

        MechanicService service = new MechanicServiceImp(dao);
        Mechanic result = service.updateMechanic(expectedId, new MechanicBuilder()
                .name("New Mechanic Name")
                .startDate(newDate)
                .number(200)
                .phone("1534567820")
                .build()
        );

        Assert.assertEquals(expectedId, result.getId());
        Assert.assertEquals("New Mechanic Name", result.getName());
        Assert.assertEquals(newDate, result.getStartDate());
        Assert.assertEquals(30, result.getNumber());
        Assert.assertEquals("1534567820", result.getPhone());

        verify(dao, times(1)).findOne(expectedId);
        verify(dao, times(1)).save(isA(Mechanic.class));
    }

    @Test
    public void updateMechanicIdNotFoundTest() throws IdNullException, EntityNullException {
        boolean exceptionThrown = false;
        Long expectedId = 123L;
        when(dao.findOne(expectedId)).thenReturn(null);

        MechanicService service = new MechanicServiceImp(dao);
        try {
            service.updateMechanic(expectedId, new Mechanic());

        } catch(NotFoundException ne) {
            Assert.assertEquals(String.format("Mechanic with id %s was not found.",expectedId), ne.getMessage());
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        verify(dao, times(1)).findOne(expectedId);
        verify(dao, times(0)).save(isA(Mechanic.class));
    }

    @Test(expected = EntityNullException.class)
    public void updateMechanicNullTest() throws EntityNullException, NotFoundException, IdNullException {
        MechanicService service = new MechanicServiceImp(dao);
        service.updateMechanic(10L, null);
    }

    @Test(expected = IdNullException.class)
    public void updateIdNullTest() throws EntityNullException, NotFoundException, IdNullException {
        MechanicService service = new MechanicServiceImp(dao);
        service.updateMechanic(null, new Mechanic());
    }

    @Test
    public void deleteMechanicOkTest() throws IdNullException {
        Long expectedId = 300L;
        Mechanic toDelete = new MechanicBuilder()
                .id(expectedId)
                .name("To Delete")
                .startDate(new GregorianCalendar(2019, Calendar.DECEMBER, 20))
                .number(300)
                .phone("1234567890")
                .build();
        when(dao.findOne(expectedId)).thenReturn(toDelete);
        doNothing().when(dao).delete(toDelete);

        MechanicService service = new MechanicServiceImp(dao);
        service.deleteMechanic(expectedId);

        verify(dao, times(1)).findOne(expectedId);
        verify(dao, times(1)).delete(toDelete);
    }

    @Test
    public void deleteMechanicNotExistTest() throws IdNullException {
        Long expectedId = 10L;
        when(dao.findOne(expectedId)).thenReturn(null);

        MechanicService service = new MechanicServiceImp(dao);
        service.deleteMechanic(expectedId);

        verify(dao, times(1)).findOne(expectedId);
    }

    @Test(expected = IdNullException.class)
    public void deleteMechanicNullIdTest() throws IdNullException {
        MechanicService service = new MechanicServiceImp(dao);
        service.deleteMechanic(null);
    }

    @Test
    public void getAllMechanicOkTest() {
        ArrayList<Mechanic> mockList = new ArrayList<Mechanic>();
        for (int i = 1; i <= 10; i++) {
            mockList.add(new MechanicBuilder()
                    .id((long) i)
                    .name(String.format("Client %s", i))
                    .startDate(new GregorianCalendar(2017, Calendar.OCTOBER, i))
                    .number(i)
                    .phone(String.format("1234567890%s", i))
                    .build());
        }
        when(dao.findAll()).thenReturn(mockList);

        MechanicService service = new MechanicServiceImp(dao);
        List<Mechanic> result = service.getAllMechanic();

        Assert.assertEquals(10, result.size());
        Assert.assertEquals(mockList, result);

        verify(dao, times(1)).findAll();
    }

    @Test
    public void getMechanicOkTest() throws NotFoundException, IdNullException {
        Long expectedId = 50L;
        Calendar expectedDate = new GregorianCalendar(2013, Calendar.FEBRUARY,15);
        when(dao.findOne(expectedId)).thenReturn(new MechanicBuilder()
                .id(expectedId)
                .name("Test Mechanic Get")
                .startDate(expectedDate)
                .number(20)
                .phone("987654321")
                .build()
        );

        MechanicService service = new MechanicServiceImp(dao);
        Mechanic result = service.getMechanic(expectedId);

        Assert.assertEquals(expectedId, result.getId());
        Assert.assertEquals("Test Mechanic Get", result.getName());
        Assert.assertEquals(expectedDate, result.getStartDate());
        Assert.assertEquals(20, result.getNumber());
        Assert.assertEquals("987654321", result.getPhone());

        verify(dao, times(1)).findOne(expectedId);
    }

    @Test
    public void getMechanicIdNotExistTest() throws IdNullException {
        boolean exceptionThrown = false;
        when(dao.findOne(20L)).thenReturn(null);
        MechanicService service = new MechanicServiceImp(dao);
        try {
            service.getMechanic(20L);

        } catch(NotFoundException ne) {
            Assert.assertEquals("Mechanic with id 20 was not found.", ne.getMessage());
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
        verify(dao, times(1)).findOne(20L);
    }

    @Test(expected = IdNullException.class)
    public void getMechanicIdNullTest() throws NotFoundException, IdNullException {
        MechanicService service = new MechanicServiceImp(dao);
        service.getMechanic(null);
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(dao);
    }
}
