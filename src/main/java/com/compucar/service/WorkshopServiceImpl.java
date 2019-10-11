package com.compucar.service;

import com.compucar.dao.WorkshopDao;
import com.compucar.model.Workshop;
import com.compucar.service.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    private WorkshopDao workshopDao;

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
    public void addWorkshop(Workshop workshop) {
        log.info("adding workshop {} ", workshop);
        workshopDao.save(workshop);
    }

    @Override
    public void updateWorkshop(Workshop workshop) throws NotFoundException {
        log.info("updating workshop {} ", workshop);
        if(workshop.getId() == null || !workshopDao.exists(workshop.getId())) {
            throw new NotFoundException("Workshop with id " + workshop.getId());
        }
        workshopDao.save(workshop);
    }

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    public void removeWorkshop(Long id) throws NotFoundException {
        log.info("removing workshop {} ", id);
        try {
            workshopDao.delete(id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Workshop with id " + id);
        }
    }
}
