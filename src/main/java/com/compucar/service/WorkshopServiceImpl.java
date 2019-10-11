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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    private WorkshopDao workshopDao;

    @Autowired
    private ReaderDao readerDao;

    @Override
    //@Cacheable("users")
    @Transactional
    public List<Workshop> listWorkshops() {
        log.info("listing workshops ");
        return workshopDao.findAll();
    }

    @Override
    //@Cacheable(value = "user")
    @Transactional
    public Workshop getWorkshop(Long id) throws NotFoundException {
        log.info("getting workshop: {}", id);
        return workshopDao.findById(id).orElseThrow(() -> new NotFoundException("Workshop with id " + id));
    }

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void addWorkshop(Workshop workshop) throws DuplicateElementException, RequiredFieldMissingException {
        log.info("adding workshop {} ", workshop);
        validateWorkshopAdd(workshop);
        workshopDao.save(workshop);
    }

    @Override
    public void updateWorkshop(Workshop workshop) throws NotFoundException, RequiredFieldMissingException, DuplicateElementException {
        log.info("updating workshop {} ", workshop);
        validateWorkshopUpdate(workshop);
        workshopDao.save(workshop);
    }

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    public void removeWorkshop(Long id) throws NotFoundException {
        log.info("removing workshop {} ", id);
        if(!workshopDao.exists(id)) {
            throw new NotFoundException("Workshop with id " + id);
        }
        removeWorkshopFromReaders(id);
        workshopDao.delete(id);
    }

    private void removeWorkshopFromReaders(Long workshopId) {
        List<Reader> readersInWorkshop = readerDao.findByWorkshopId(workshopId);
        readersInWorkshop.forEach(reader -> {
            reader.setWorkshop(null);
            readerDao.save(reader);
        });
    }

    private void validateWorkshopAdd(Workshop workshop) throws RequiredFieldMissingException, DuplicateElementException {
        log.info("validating workshop {} ", workshop);
        validateRequiredFields(workshop);

        if(workshop.getId() != null && workshopDao.exists(workshop.getId())) {
            log.info("duplicate workshop id {} ", workshop.getId());
            throw new DuplicateElementException("Workshop with id " + workshop.getId());
        }
        if(workshop.getCode() != null && workshopDao.existsByCode(workshop.getCode())) {
            log.info("duplicate workshop code {} ", workshop.getCode());
            throw new DuplicateElementException("Workshop with code " + workshop.getCode());
        }
    }

    private void validateWorkshopUpdate(Workshop workshop) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException {
        log.info("validating workshop {} ", workshop);
        validateRequiredFields(workshop);

        if(workshop.getId() == null || !workshopDao.exists(workshop.getId())) {
            log.info("workshop with id {} not found", workshop.getId());
            throw new NotFoundException("Workshop with id " + workshop.getId());
        }
        Optional<Workshop> workshopLookupByCode = workshopDao.findByCode(workshop.getCode());
        if(workshopLookupByCode.isPresent() && workshopLookupByCode.get().getId() != workshop.getId()) {
            log.info("duplicate workshop code {} ", workshop.getCode());
            throw new DuplicateElementException("Workshop with code " + workshop.getCode());
        }
    }

    private void validateRequiredFields(Workshop workshop) throws RequiredFieldMissingException {
        if(workshop.getCode() == null || workshop.getCode().trim().isEmpty()) {
            log.info("workshop code missing");
            throw new RequiredFieldMissingException("Code");
        }
        if(workshop.getName() == null || workshop.getName().trim().isEmpty()) {
            log.info("workshop name missing");
            throw new RequiredFieldMissingException("Name");
        }
    }
}
