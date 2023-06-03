package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.Uczelnia;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UczelniaRepository extends CassandraRepository<Uczelnia, String> {
    Optional<Uczelnia> findUczelniaByName(String name);
    Set<Uczelnia> findByWydzialyKierunkiType(String kierunekType);
}
