package com.compucar.service;

import com.compucar.dao.ReaderDao;
import com.compucar.dao.WorkshopDao;
import com.compucar.model.Reader;
import com.compucar.model.Workshop;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderDao readerDao;

    @Autowired
    private WorkshopDao workshopDao;

    @Override
    @Cacheable(value = "readers")
    public List<Reader> listReaders() {
        log.info("listing readers ");
        return readerDao.findAll();
    }

    @Override
    @Cacheable(value = "readers")
    public Reader getReader(Long id) throws NotFoundException {
        log.info("getting reader: {}", id);
        return readerDao.findById(id).orElseThrow(() -> new NotFoundException("Reader with id " + id));
    }

    @Override
    @CacheEvict(value = "readers", allEntries = true)
    public Reader addReader(Reader reader) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException {
        log.info("adding reader {} ", reader);
        validateReaderAdd(reader);
        if(reader.getWorkshop() != null) {
            String workshopCode = reader.getWorkshop().getCode();
            Workshop workshop = workshopDao.findByCode(workshopCode).orElseThrow(() -> new NotFoundException("Workshop with code " + workshopCode));
            reader.setWorkshop(workshop);
        }
        return readerDao.save(reader);
    }

    @Override
    @CacheEvict(value = "readers", allEntries = true)
    public void updateReader(Reader reader) throws RequiredFieldMissingException, NotFoundException, DuplicateElementException {
        log.info("updating reader {} ", reader);
        validateReaderUpdate(reader);
        if(reader.getWorkshop() != null) {
            String workshopCode = reader.getWorkshop().getCode();
            Workshop workshop = workshopDao.findByCode(workshopCode).orElseThrow(() -> new NotFoundException("Workshop with code " + workshopCode));
            reader.setWorkshop(workshop);
        }
        readerDao.save(reader);
    }

    @Override
    @CacheEvict(value = "readers", allEntries = true)
    public void removeReader(Long id) throws NotFoundException {
        log.info("removing reader {} ", id);
        if(!readerDao.exists(id)) {
            throw new NotFoundException("Reader with id " + id);
        }
        readerDao.delete(id);
    }

    private void validateReaderAdd(Reader reader) throws RequiredFieldMissingException, DuplicateElementException {
        log.info("validating reader {} ", reader);
        validateRequiredFields(reader);

        if(reader.getId() != null && readerDao.exists(reader.getId())) {
            log.info("duplicate reader id {} ", reader.getId());
            throw new DuplicateElementException("Reader with id " + reader.getId());
        }
        if(reader.getCode() != null && readerDao.existsByCode(reader.getCode())) {
            log.info("duplicate reader code {} ", reader.getCode());
            throw new DuplicateElementException("Reader with code " + reader.getCode());
        }
    }

    private void validateReaderUpdate(Reader reader) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException {
        log.info("validating reader {} ", reader);
        validateRequiredFields(reader);

        if(reader.getId() == null || !readerDao.exists(reader.getId())) {
            log.info("reader with id {} not found", reader.getId());
            throw new NotFoundException("Reader with id " + reader.getId());
        }
        Optional<Reader> readerLookupByCode = readerDao.findByCode(reader.getCode());
        if(readerLookupByCode.isPresent() && readerLookupByCode.get().getId() != reader.getId()) {
            log.info("duplicate reader code {} ", reader.getCode());
            throw new DuplicateElementException("Reader with code " + reader.getCode());
        }
    }

    private void validateRequiredFields(Reader reader) throws RequiredFieldMissingException {
        if(reader.getCode() == null || reader.getCode().trim().isEmpty()) {
            log.info("reader code missing");
            throw new RequiredFieldMissingException("Code");
        }
    }
}
