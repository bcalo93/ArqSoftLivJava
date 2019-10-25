package com.compucar.controller;

import com.compucar.aop.AspectExecution;
import com.compucar.dto.ReaderDto;
import com.compucar.model.Reader;
import com.compucar.service.ReaderService;
import com.compucar.service.exceptions.DuplicateElementException;
import com.compucar.service.exceptions.InvalidFieldValueException;
import com.compucar.service.exceptions.NotFoundException;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @AspectExecution
    public List<Reader> listReaders() {
        log.info("list all readers");
        return readerService.listReaders();
    }

    @GetMapping(params = "delta")
    @AspectExecution
    public List<Reader> listReaders(@RequestParam Integer delta) throws InvalidFieldValueException {
        log.info("list all readers with battery less than {}", delta);
        return readerService.listReadersWithBatteryLessThan(delta);
    }

    @GetMapping(value = "/{readerId}")
    @AspectExecution
    public Reader getReader(@PathVariable("readerId") Long id) throws NotFoundException {
        log.info("getReader {}", id);
        Reader reader = readerService.getReader(id);
        log.info("reader {} ", reader);

        return reader;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @AspectExecution
    public Reader saveReader(@RequestBody ReaderDto readerDto) throws RequiredFieldMissingException, DuplicateElementException, NotFoundException {
        log.info("received  {}", readerDto);
        Reader reader = modelMapper.map(readerDto, Reader.class);
        return readerService.addReader(reader);
    }

    @PutMapping
    @AspectExecution
    public void updateReader(@RequestBody ReaderDto readerDto) throws RequiredFieldMissingException, NotFoundException, InvalidFieldValueException {
        log.info("received {}", readerDto);
        Reader reader = modelMapper.map(readerDto, Reader.class);
        readerService.updateReader(reader);
    }

    @DeleteMapping(value = "/{readerId}")
    @AspectExecution
    public void removeReader(@PathVariable("readerId") Long id) throws NotFoundException {
        log.info("delete reader  {}", id);
        readerService.removeReader(id);
    }
}
