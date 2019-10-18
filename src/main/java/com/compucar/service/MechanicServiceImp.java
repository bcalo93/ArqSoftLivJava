package com.compucar.service;

import com.compucar.dao.MechanicDao;
import com.compucar.model.Mechanic;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.EntityNullException;
import com.compucar.service.exceptions.IdNullException;
import com.compucar.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MechanicServiceImp implements MechanicService {

    @Autowired
    private MechanicDao mechanicDao;

    public MechanicServiceImp(MechanicDao mechanicDao) {
        this.mechanicDao = mechanicDao;
    }

    @Override
    @CacheEvict(value = "mechanics", allEntries = true)
    public Mechanic addMechanic(Mechanic mechanic) throws DuplicateElementException, EntityNullException {
        if(mechanic == null) {
            throw new EntityNullException("The mechanic is null.");
        }
        if(this.mechanicDao.findByCode(mechanic.getCode()).isPresent()) {
            throw new DuplicateElementException(String.format("Mechanic with number %s", mechanic.getCode()));
        }
        return this.mechanicDao.save(mechanic);
    }

    @Override
    @CacheEvict(value = "mechanics", allEntries = true)
    public Mechanic updateMechanic(Long id, Mechanic mechanic) throws NotFoundException, IdNullException, EntityNullException {
        if(id == null) {
            throw new IdNullException("The mechanic id is null.");
        }
        if(mechanic == null) {
            throw new EntityNullException("The mechanic is null.");
        }
        Mechanic toUpdate = this.mechanicDao.findOne(id);
        if(toUpdate == null) {
            throw new NotFoundException(String.format("Mechanic with id %s", id));
        }
        toUpdate.update(mechanic);
        return this.mechanicDao.save(toUpdate);
    }

    @Override
    @CacheEvict(value = "mechanics", allEntries = true)
    public void deleteMechanic(Long id) throws IdNullException {
        if(id == null) {
            throw new IdNullException("The mechanic id is null.");
        }
        Mechanic toDelete = this.mechanicDao.findOne(id);
        if(toDelete != null) {
            this.mechanicDao.delete(toDelete);
        }
    }

    @Override
    @Cacheable(value = "mechanics")
    public Mechanic getMechanic(Long id) throws NotFoundException, IdNullException {
        if(id == null) {
            throw new IdNullException("The mechanic id is null.");
        }
        Mechanic foundMechanic = this.mechanicDao.findOne(id);
        if(foundMechanic == null) {
            throw new NotFoundException(String.format("Mechanic with id %s", id));
        }
        return foundMechanic;
    }

    @Override
    @Cacheable(value = "mechanics")
    public List<Mechanic> getAllMechanic() {
        return this.mechanicDao.findAll();
    }
}
