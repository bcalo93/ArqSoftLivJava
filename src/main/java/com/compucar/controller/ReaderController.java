package com.compucar.controller;

import com.compucar.model.Reader;
import com.compucar.service.ReaderService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping
    public List<Reader> listReaders() {
        log.info("list all readers");
        return readerService.listReaders();
    }

    @GetMapping(value = "/{readerId}")
    public Reader getReader(@PathVariable("readerId") Long id) throws NotFoundException {
        log.info("getReader {}", id);
        Reader reader = readerService.getReader(id);
        log.info("reader {} ", reader);

        return reader;
    }

    @PostMapping
    public void saveReader(@RequestBody Reader reader) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException {
        log.info("received  {}", reader);
        readerService.addReader(reader);
    }

    @PutMapping
    public void updateReader(@RequestBody Reader reader) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException {//throws NotFoundException, RequiredFieldMissingException, DuplicateElementException {
        log.info("received  {}", reader);
        readerService.updateReader(reader);
    }

    @DeleteMapping(value = "/{readerId}")
    public void removeReader(@PathVariable("readerId") Long id) throws NotFoundException {
        log.info("delete reader  {}", id);
        readerService.removeReader(id);
    }
}
