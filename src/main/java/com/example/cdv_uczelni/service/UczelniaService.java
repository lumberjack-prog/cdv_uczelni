package com.example.cdv_uczelni.service;

import com.example.cdv_uczelni.model.Kierunek;
import com.example.cdv_uczelni.model.Uczelnia;
import com.example.cdv_uczelni.model.UczelnieJson;
import com.example.cdv_uczelni.model.Wydzial;
import com.example.cdv_uczelni.repo.UczelniaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("cassandra")
@Slf4j
public class UczelniaService implements IDao<Uczelnia> {

    private final UczelniaRepository uczelniaRepository;

    @Autowired
    public UczelniaService(UczelniaRepository uczelniaRepository) {
        this.uczelniaRepository = uczelniaRepository;
    }

    @Override
    public Set<Uczelnia> getAll() {
        List<Uczelnia> all = uczelniaRepository.findAll();
        return new HashSet<>(all);
    }

    @Override
    public Optional<Uczelnia> getById(String key) {
        return uczelniaRepository.findById(key);
    }

    public Set<Uczelnia> getByKierunekType(String kierunekType) {
        Set<Uczelnia> all = getAll();
        Set<Uczelnia> result = new HashSet<>();
        all.forEach(uczelnia -> {
            Set<Wydzial> wydzialy = uczelnia.getWydzialy();
            wydzialy.forEach(wydzial -> {
                Set<Kierunek> kierunki = wydzial.getKierunki();
                kierunki.forEach(kierunek -> {
                    if (kierunek.getType().equals(kierunekType)) {
                        result.add(uczelnia);
                    }
                });
            });
        });
        return result;
    }

    public boolean deleteById(String key) {
        Optional<Uczelnia> byId = getById(key);

        if (byId.isPresent()) {
            uczelniaRepository.deleteById(key);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Uczelnia> getByName(String name) {
       return uczelniaRepository.findUczelniaByName(name);
    }

    @Override
    public Set<Uczelnia> saveAll(UczelnieJson uczelnie) {
        List<Uczelnia> uczelnie1 = uczelnie.getUczelnie();
        Set<Uczelnia> uczelnieSet = new HashSet<>(uczelnie1);
        for (Uczelnia uczelnia : uczelnieSet) {
            save(uczelnia);
        }
        return uczelnieSet;
    }

    @Override
    public Uczelnia save(Uczelnia uczelnia) {
        return uczelniaRepository.save(uczelnia);
    }
}
