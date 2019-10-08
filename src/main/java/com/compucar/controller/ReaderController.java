package com.compucar.controller;

import com.compucar.model.Reader;
import com.compucar.service.ReaderService;
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
        log.info("list");
        return readerService.listReaders();
    }

    //@ResponseBody
    @GetMapping(value = "/{readerId}")
    public Reader getReader(@PathVariable("readerId") Long id) {
        log.info("getReader {}", id);
        Reader reader = readerService.getReader(id);

        log.info("reader {} ", reader);

        return reader;
    }

    @PostMapping
    public void saveReader(@RequestBody Reader reader) {
        log.info("received  {}", reader);
        readerService.addReader(reader);
    }
}
