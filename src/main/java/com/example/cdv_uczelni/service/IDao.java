package com.example.cdv_uczelni.service;

import com.example.cdv_uczelni.model.UczelnieJson;

import java.util.Optional;
import java.util.Set;

public interface IDao<T> {

    Set<T> getAll();
    Optional<T> getById(String id);

    Set<T> saveAll(UczelnieJson uczelni);

    T save(T uczelnia);
}
