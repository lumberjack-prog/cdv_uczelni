package com.example.cdv_uczelni.controller;

import com.example.cdv_uczelni.model.University;
import com.example.cdv_uczelni.model.UniversityJson;
import com.example.cdv_uczelni.service.impl.UniversityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/cs/university")
public class UniversityController {
    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Map<String, Set<University>> responseMap = new HashMap<>();
        responseMap.put("universities", universityService.getAll());
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable UUID id, HttpServletResponse response) {
        Optional<University> byId = universityService.getById(id);

        if (byId.isPresent()) {
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        } else {
            Map<String, String> message = new HashMap<>();
            message.put("_message", "University with id: " + id + " was not found!");
            response.setContentType(APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @RequestMapping(value = "/getByName/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@PathVariable String name, HttpServletResponse response) {
        Optional<University> byName = universityService.getByName(name);
        if (byName.isPresent()) {
            return new ResponseEntity<>(byName.get(), HttpStatus.OK);
        } else {
            Map<String, String> message = new HashMap<>();
            message.put("_message", "University with the name: " + name + " was not found!");
            response.setContentType(APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @RequestMapping(value = "/getByMajorType/{majorType}", method = RequestMethod.GET)
    public ResponseEntity<?> getByMajorType(@PathVariable String majorType, HttpServletResponse response) {
        Set<University> byMajorType = universityService.getByMajorType(majorType);
        return new ResponseEntity<>(byMajorType, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveAll", method = RequestMethod.POST)
    public ResponseEntity<?> saveAll(@RequestBody UniversityJson universityJson) {
        return new ResponseEntity<>(universityService.saveAll(universityJson), HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody University university){
        return new ResponseEntity<>(universityService.save(university), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody University university){
        return new ResponseEntity<>(universityService.save(university), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> delete(@PathVariable UUID id){
        return new ResponseEntity<>(universityService.deleteById(id), HttpStatus.OK);
    }
}
