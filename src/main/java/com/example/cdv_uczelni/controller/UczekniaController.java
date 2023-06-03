package com.example.cdv_uczelni.controller;

import com.example.cdv_uczelni.model.Uczelnia;
import com.example.cdv_uczelni.model.UczelnieJson;
import com.example.cdv_uczelni.service.UczelniaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/cs/uczelnia")
public class UczekniaController {

    @Autowired
    @Qualifier("cassandra")
    private UczelniaService uczelniaService;


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Map<String, Set<Uczelnia>> responseMap = new HashMap<>();
        responseMap.put("uczelnie", uczelniaService.getAll());
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id, HttpServletResponse response) {
        Optional<Uczelnia> byId = uczelniaService.getById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        } else {
            Map<String, String> message = new HashMap<>();
            message.put("_message", "Uczelnie with the key: " + id + " was not found!");
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
    public ResponseEntity<?> getByUczelniaName(@PathVariable String name, HttpServletResponse response) {
        Optional<Uczelnia> byName = uczelniaService.getByName(name);
        if (byName.isPresent()) {
            return new ResponseEntity<>(byName.get(), HttpStatus.OK);
        } else {
            Map<String, String> message = new HashMap<>();
            message.put("_message", "Uczelnie with the name: " + name + " was not found!");
            response.setContentType(APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @RequestMapping(value = "/getByKierunekType/{kierunekType}", method = RequestMethod.GET)
    public ResponseEntity<?> getByKierunekType(@PathVariable String kierunekType, HttpServletResponse response) {
        Set<Uczelnia> byKierunekType = uczelniaService.getByKierunekType(kierunekType);
        return new ResponseEntity<>(byKierunekType, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveAll", method = RequestMethod.POST)
    public ResponseEntity<?> saveAll(@RequestBody UczelnieJson uczelni) {
        return new ResponseEntity<>(uczelniaService.saveAll(uczelni), HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Uczelnia uczelnia){
        return new ResponseEntity<>(uczelniaService.save(uczelnia), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Uczelnia uczelnia){
        return new ResponseEntity<>(uczelniaService.save(uczelnia), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> delete(@PathVariable String id){
        return new ResponseEntity<>(uczelniaService.deleteById(id), HttpStatus.OK);
    }
}
