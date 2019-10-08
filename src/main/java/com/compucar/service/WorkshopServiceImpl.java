package com.compucar.service;

import com.compucar.dao.ReaderDao;
import com.compucar.dao.WorkshopDao;
import com.compucar.model.Workshop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    private WorkshopDao workshopDao;

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void addWorkshop(Workshop workshop) {
        log.info("adding workshop {} ", workshop);
        Workshop w = workshopDao.save(workshop);
        System.out.println(w);
    }

    @Override
    public void update(Workshop workshop) {
        log.info("updating workshop {} ", workshop);
        workshopDao.save(workshop);
    }

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    public void removeWorkshop(Workshop workshop) {
        log.info("removing workshop {} ", workshop);
        workshopDao.delete(workshop.getId());
    }

    @Override
    //@Cacheable(value = "user")
    @Transactional
    public Workshop getWorkshop(Long id) {
        log.info("getting workshop: {}", id);
        return workshopDao.findById(id);
    }

    @Override
    //@Cacheable("users")
    @Transactional
    public List<Workshop> listWorkshops() {
        log.info("listing workshops ");
        return workshopDao.findAll();
    }
}
