package com.compucar.service;

import com.compucar.dao.ReaderDao;
import com.compucar.dao.WorkshopDao;
import com.compucar.model.Reader;
import com.compucar.model.Workshop;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class WorkshopServiceImplTest {
    private WorkshopDao workshopDao;
    private ReaderDao readerDao;

    @Before
    public void initTest() {
        this.workshopDao = mock(WorkshopDao.class);
        this.readerDao = mock(ReaderDao.class);
    }

    @Test
    public void getAllWorkshops_ok() {
        List<Workshop> mockList = new ArrayList<Workshop>();
        for (int i = 0; i < 10; i++) {
            Workshop workshop = new Workshop();
            workshop.setCode("SF" + i);
            workshop.setName("Elta ller " + i);
            mockList.add(workshop);
        }
        when(workshopDao.findAll()).thenReturn(mockList);

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        List<Workshop> result = service.listWorkshops();

        Assert.assertEquals(10, result.size());
        Assert.assertEquals(mockList, result);

        verify(workshopDao, times(1)).findAll();
    }

    @Test
    public void getWorkshopById_ok() throws NotFoundException {
        Long workshopId = 50L;
        Workshop workshop = new Workshop();
        workshop.setId(workshopId);
        workshop.setCode("SF43");
        workshop.setName("Elta ller");
        when(workshopDao.findById(workshopId)).thenReturn(Optional.of(workshop));

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        Workshop result = service.getWorkshop(workshopId);

        Assert.assertEquals(workshopId, result.getId());
        Assert.assertEquals("SF43", result.getCode());
        Assert.assertEquals("Elta ller", result.getName());

        verify(workshopDao, times(1)).findById(workshopId);
    }

    @Test(expected = NotFoundException.class)
    public void getWorkshopById_nonExistent() throws NotFoundException {
        Long workshopId = 50L;
        when(workshopDao.findById(workshopId)).thenReturn(Optional.empty());

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        Workshop result = service.getWorkshop(workshopId);

        verify(workshopDao, times(1)).findById(workshopId);
    }

    @Test
    public void addWorkshop_ok() throws DuplicateElementException, RequiredFieldMissingException {
        Workshop newWorkshop = new Workshop();
        newWorkshop.setCode("SF43");
        newWorkshop.setName("Unta Ller");
        newWorkshop.setAddress("Ladi Rección");
        newWorkshop.setCity("New york");

        when(workshopDao.existsByCode(newWorkshop.getCode())).thenReturn(false);
        when(workshopDao.save(isA(Workshop.class))).thenReturn(newWorkshop);
        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);

        Workshop result = service.addWorkshop(newWorkshop);
        Assert.assertEquals(newWorkshop, result);

        verify(workshopDao, times(1)).existsByCode(newWorkshop.getCode());
        verify(workshopDao, times(1)).save(newWorkshop);
    }

    @Test(expected = DuplicateElementException.class)
    public void addWorkshop_duplicateCode() throws DuplicateElementException, RequiredFieldMissingException {
        Workshop duplicateCodeWorkshop = new Workshop();
        duplicateCodeWorkshop.setCode("SF43");
        duplicateCodeWorkshop.setName("Unta ller");

        when(workshopDao.existsByCode(duplicateCodeWorkshop.getCode())).thenReturn(true);
        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);

        Workshop result = service.addWorkshop(duplicateCodeWorkshop);

        verify(workshopDao, times(1)).existsByCode(duplicateCodeWorkshop.getCode());
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addWorkshop_nullCode() throws DuplicateElementException, RequiredFieldMissingException {
        Workshop nullCodeWorkshop = new Workshop();
        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);

        Workshop result = service.addWorkshop(nullCodeWorkshop);
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addWorkshop_nullName() throws DuplicateElementException, RequiredFieldMissingException {
        Workshop nullNameWorkshop = new Workshop();
        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);

        nullNameWorkshop.setCode("SF43");
        Workshop result = service.addWorkshop(nullNameWorkshop);
    }

    @Test
    public void updateWorkshop_ok() throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException {
        Long workshopId = 32L;
        Workshop workshop = new Workshop();

        workshop.setId(workshopId);
        workshop.setCode("SF43");
        workshop.setName("Elta ller");

        when(workshopDao.exists(workshopId)).thenReturn(true);
        when(workshopDao.findById(workshopId)).thenReturn(Optional.of(workshop));
        when(workshopDao.save(isA(Workshop.class))).thenReturn(workshop);

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        Workshop result = service.updateWorkshop(workshop);

        Assert.assertEquals(workshopId, result.getId());
        Assert.assertEquals("SF43", result.getCode());
        Assert.assertEquals("Elta ller", result.getName());

        verify(workshopDao, times(1)).exists(workshopId);
        verify(workshopDao, times(1)).findById(workshopId);
        verify(workshopDao, times(1)).save(isA(Workshop.class));
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void updateWorkshop_nullCode() throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException {
        Workshop nullCodeWorkshop = new Workshop();
        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);

        Workshop result = service.updateWorkshop(nullCodeWorkshop);
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void updateWorkshop_nullName() throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException {
        Workshop nullNameWorkshop = new Workshop();
        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);

        nullNameWorkshop.setCode("SF43");
        Workshop result = service.updateWorkshop(nullNameWorkshop);
    }

    @Test(expected = NotFoundException.class)
    public void updateWorkshop_nonExistent() throws NotFoundException, RequiredFieldMissingException, InvalidFieldValueException {
        Long workshopId = 50L;
        Workshop nonExistentWorkshop = new Workshop();
        nonExistentWorkshop.setId(workshopId);
        nonExistentWorkshop.setCode("SF43");
        nonExistentWorkshop.setName("Elta ller");
        when(workshopDao.exists(workshopId)).thenReturn(false);

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        Workshop result = service.updateWorkshop(nonExistentWorkshop);

        verify(workshopDao, times(1)).exists(workshopId);
    }

    @Test(expected = InvalidFieldValueException.class)
    public void updateWorkshop_changeCode() throws NotFoundException, RequiredFieldMissingException, InvalidFieldValueException {
        Long workshopId = 50L;
        Workshop originalWorkshop = new Workshop();
        Workshop updatedWorkshop = new Workshop();

        originalWorkshop.setId(workshopId);
        originalWorkshop.setCode("SF43");
        originalWorkshop.setName("Elta ller");

        updatedWorkshop.setId((workshopId));
        updatedWorkshop.setCode("Other");
        updatedWorkshop.setName("Updated");

        when(workshopDao.exists(workshopId)).thenReturn(true);
        when(workshopDao.findById(workshopId)).thenReturn(Optional.of(originalWorkshop));

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        Workshop result = service.updateWorkshop(updatedWorkshop);

        verify(workshopDao, times(1)).exists(workshopId);
        verify(workshopDao, times(1)).findById(workshopId);
    }

    @Test
    public void deleteWorkshop_noReaders_ok() throws NotFoundException {
        Long workshopId = 32L;

        when(workshopDao.exists(workshopId)).thenReturn(true);
        when(readerDao.findByWorkshopId(workshopId)).thenReturn(Collections.emptyList());
        doNothing().when(workshopDao).delete(workshopId);

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        service.removeWorkshop(workshopId);

        verify(workshopDao, times(1)).exists(workshopId);
        verify(readerDao, times(1)).findByWorkshopId(workshopId);
        verify(workshopDao, times(1)).delete(isA(Long.class));
    }

    @Test
    public void deleteWorkshop_withReaders_ok() throws NotFoundException {
        Long workshopId = 32L;
        Workshop workshopToDelete = new Workshop();
        List<Reader> readersInWorkshop = new ArrayList<>();

        workshopToDelete.setId(workshopId);
        workshopToDelete.setCode("Isa Code");
        workshopToDelete.setName("Isa name");
        for(int i=0; i<3; i++) {
            Reader reader = new Reader();
            reader.setId(Long.valueOf(i));
            reader.setCode("Acode"+i);
            reader.setWorkshop(workshopToDelete);

            readersInWorkshop.add(reader);
        }

        when(workshopDao.exists(workshopId)).thenReturn(true);
        when(readerDao.findByWorkshopId(workshopId)).thenReturn(readersInWorkshop);
        doNothing().when(workshopDao).delete(workshopId);

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        service.removeWorkshop(workshopId);

        verify(workshopDao, times(1)).exists(workshopId);
        verify(readerDao, times(1)).findByWorkshopId(workshopId);
        verify(workshopDao, times(1)).delete(isA(Long.class));
        verify(readerDao, times(3)).save(isA(Reader.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteWorkshop_nonExistent() throws NotFoundException {
        Long workshopId = 32L;

        when(workshopDao.exists(workshopId)).thenReturn(false);

        WorkshopService service = new WorkshopServiceImpl(workshopDao, readerDao);
        service.removeWorkshop(workshopId);

        verify(workshopDao, times(1)).exists(workshopId);
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(workshopDao);
    }
}
