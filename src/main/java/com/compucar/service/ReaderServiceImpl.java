package com.compucar.service;

import com.compucar.dao.ReaderDao;
import com.compucar.model.Reader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderDao readerDao;

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void addReader(Reader reader) {
        log.info("adding reader {} ", reader);
        readerDao.save(reader);
    }

    @Override
    public void update(Reader reader) {
        log.info("updating reader {} ", reader);
        readerDao.save(reader);
    }

    @Override
    //@CacheEvict(value = "users", allEntries = true)
    public void removeReader(Reader reader) {
        log.info("removing reader {} ", reader);
        readerDao.delete(reader.getId());
    }

    @Override
    //@Cacheable(value = "user")
    @Transactional
    public Reader getReader(Long id) {
        log.info("getting reader: {}", id);
        return readerDao.findById(id);
    }

    @Override
    //@Cacheable("users")
    @Transactional
    public List<Reader> listReaders() {
        log.info("listing readers ");
        return readerDao.findAll();
    }
}
