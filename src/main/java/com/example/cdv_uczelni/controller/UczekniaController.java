package com.example.cdv_uczelni.controller;

import com.example.cdv_uczelni.model.Uczelnia;
import com.example.cdv_uczelni.model.UczelnieJson;
import com.example.cdv_uczelni.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class UczekniaController {

    private RedisService redisService;

    @Autowired
    public UczekniaController(RedisService redisService) {
        this.redisService = redisService;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Map<String, Set<Uczelnia>> responseMap = new HashMap<>();
        responseMap.put("uczelnie", redisService.getAll());
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id) {
        return new ResponseEntity<>(redisService.getById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/saveAll", method = RequestMethod.POST)
    public ResponseEntity<?> saveAll(@RequestBody UczelnieJson uczelni) {
        return new ResponseEntity<>(redisService.saveAll(uczelni), HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Uczelnia uczelnia){
        return new ResponseEntity<>(redisService.save(uczelnia), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Uczelnia uczelnia){
        return new ResponseEntity<>(redisService.save(uczelnia), HttpStatus.OK);
    }
}
