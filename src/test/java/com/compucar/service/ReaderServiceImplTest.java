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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ReaderServiceImplTest {

    private ReaderDao readerDao;
    private WorkshopDao workshopDao;

    @Before
    public void initTest() {
        this.readerDao = mock(ReaderDao.class);
        this.workshopDao = mock(WorkshopDao.class);
    }

    @Test
    public void getAllReaders_ok() {
        List<Reader> mockList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reader reader = new Reader();
            reader.setCode("SF" + i);
            mockList.add(reader);
        }
        when(readerDao.findAll()).thenReturn(mockList);

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        List<Reader> result = service.listReaders();

        Assert.assertEquals(10, result.size());
        Assert.assertEquals(mockList, result);

        verify(readerDao, times(1)).findAll();
    }

    @Test
    public void getReaderById_ok() throws NotFoundException {
        Long readerId = 50L;
        Reader reader = new Reader();
        reader.setId(readerId);
        reader.setCode("SF43");
        when(readerDao.findById(readerId)).thenReturn(Optional.of(reader));

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        Reader result = service.getReader(readerId);

        Assert.assertEquals(readerId, result.getId());
        Assert.assertEquals("SF43", result.getCode());

        verify(readerDao, times(1)).findById(readerId);
    }

    @Test(expected = NotFoundException.class)
    public void getReaderById_nonExistent() throws NotFoundException {
        Long readerId = 50L;
        when(readerDao.findById(readerId)).thenReturn(Optional.empty());

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        Reader result = service.getReader(readerId);

        verify(readerDao, times(1)).findById(readerId);
    }

    @Test
    public void addReader_ok() throws RequiredFieldMissingException, NotFoundException, DuplicateElementException {
        Reader newReader = new Reader();
        newReader.setCode("SF43");

        when(readerDao.existsByCode(newReader.getCode())).thenReturn(false);
        when(readerDao.save(isA(Reader.class))).thenReturn(newReader);
        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);

        Reader result = service.addReader(newReader);
        Assert.assertEquals(newReader, result);

        verify(readerDao, times(1)).existsByCode(newReader.getCode());
        verify(readerDao, times(1)).save(newReader);
    }

    @Test
    public void addReader_withWorkshop_ok() throws RequiredFieldMissingException, NotFoundException, DuplicateElementException {
        Workshop aWorkshop = new Workshop();
        Reader newReader = new Reader();

        aWorkshop.setId(20L);
        aWorkshop.setCode("WK99");
        aWorkshop.setName("Wok Yo");
        newReader.setCode("SF43");
        newReader.setWorkshop(aWorkshop);

        when(readerDao.existsByCode(newReader.getCode())).thenReturn(false);
        when(readerDao.save(isA(Reader.class))).thenReturn(newReader);
        when(workshopDao.findByCode(aWorkshop.getCode())).thenReturn(Optional.of(aWorkshop));

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);

        Reader result = service.addReader(newReader);
        Assert.assertEquals(newReader, result);
        Assert.assertEquals(aWorkshop, result.getWorkshop());

        verify(readerDao, times(1)).existsByCode(newReader.getCode());
        verify(readerDao, times(1)).save(newReader);
        verify(workshopDao, times(1)).findByCode(aWorkshop.getCode());
    }

    @Test(expected = DuplicateElementException.class)
    public void addReader_duplicateCode() throws DuplicateElementException, RequiredFieldMissingException, NotFoundException {
        Reader duplicateCodeReader = new Reader();
        duplicateCodeReader.setCode("SF43");

        when(readerDao.existsByCode(duplicateCodeReader.getCode())).thenReturn(true);
        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);

        Reader result = service.addReader(duplicateCodeReader);

        verify(readerDao, times(1)).existsByCode(duplicateCodeReader.getCode());
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addReader_nullCode() throws DuplicateElementException, RequiredFieldMissingException, NotFoundException {
        Reader nullCodeReader = new Reader();
        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);

        Reader result = service.addReader(nullCodeReader);
    }

    @Test(expected = NotFoundException.class)
    public void addReader_nonExistentWorkshop() throws DuplicateElementException, RequiredFieldMissingException, NotFoundException {
        Reader newReader = new Reader();
        Workshop nonExistentWorkshop = new Workshop();

        nonExistentWorkshop.setCode("WK89");
        newReader.setCode("SF43");
        newReader.setWorkshop(nonExistentWorkshop);

        when(readerDao.existsByCode(newReader.getCode())).thenReturn(false);
        when(workshopDao.findByCode(nonExistentWorkshop.getCode())).thenReturn(Optional.empty());
        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);

        try{
            Reader result = service.addReader(newReader);
        }
        finally {
            verify(readerDao, times(1)).existsByCode(newReader.getCode());
            verify(workshopDao, times(1)).findByCode(nonExistentWorkshop.getCode());
        }
    }

    @Test
    public void updateReader_ok() throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException {
        Long readerId = 32L;
        Reader reader = new Reader();

        reader.setId(readerId);
        reader.setCode("SF43");

        when(readerDao.exists(readerId)).thenReturn(true);
        when(readerDao.findById(readerId)).thenReturn(Optional.of(reader));
        when(readerDao.save(isA(Reader.class))).thenReturn(reader);

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        Reader result = service.updateReader(reader);

        Assert.assertEquals(readerId, result.getId());
        Assert.assertEquals("SF43", result.getCode());

        verify(readerDao, times(1)).exists(readerId);
        verify(readerDao, times(1)).findById(readerId);
        verify(readerDao, times(1)).save(isA(Reader.class));
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void updateReader_nullCode() throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException {
        Reader nullCodeReader = new Reader();
        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);

        Reader result = service.updateReader(nullCodeReader);
    }

    @Test(expected = NotFoundException.class)
    public void updateReader_nonExistent() throws NotFoundException, RequiredFieldMissingException, InvalidFieldValueException {
        Long readerId = 50L;
        Reader nonExistentReader = new Reader();
        nonExistentReader.setId(readerId);
        nonExistentReader.setCode("SF43");
        when(readerDao.exists(readerId)).thenReturn(false);

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        Reader result = service.updateReader(nonExistentReader);

        verify(readerDao, times(1)).exists(readerId);
    }

    @Test(expected = InvalidFieldValueException.class)
    public void updateReader_changeCode() throws NotFoundException, RequiredFieldMissingException, InvalidFieldValueException {
        Long readerId = 50L;
        Reader originalReader = new Reader();
        Reader updatedReader = new Reader();

        originalReader.setId(readerId);
        originalReader.setCode("SF43");

        updatedReader.setId((readerId));
        updatedReader.setCode("Other");

        when(readerDao.exists(readerId)).thenReturn(true);
        when(readerDao.findById(readerId)).thenReturn(Optional.of(originalReader));

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        Reader result = service.updateReader(updatedReader);

        verify(readerDao, times(1)).exists(readerId);
        verify(readerDao, times(1)).findById(readerId);
    }

    @Test
    public void deleteReader_ok() throws NotFoundException {
        Long readerId = 32L;

        when(readerDao.exists(readerId)).thenReturn(true);
        doNothing().when(readerDao).delete(readerId);

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        service.removeReader(readerId);

        verify(readerDao, times(1)).exists(readerId);
        verify(readerDao, times(1)).delete(isA(Long.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteReader_nonExistent() throws NotFoundException {
        Long readerId = 32L;

        when(readerDao.exists(readerId)).thenReturn(false);

        ReaderService service = new ReaderServiceImpl(readerDao, workshopDao);
        service.removeReader(readerId);

        verify(readerDao, times(1)).exists(readerId);
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(workshopDao);
    }
}