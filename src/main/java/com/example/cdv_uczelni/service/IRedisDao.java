package com.example.cdv_uczelni.service;

import com.example.cdv_uczelni.model.Uczelnia;
import com.example.cdv_uczelni.model.UczelnieJson;

import java.util.Set;

public interface IRedisDao {

    Set<Uczelnia> getAll();
    Uczelnia getById(String id);

    Set<Uczelnia> saveAll(UczelnieJson uczelni);

    Uczelnia save(Uczelnia poll);
}
