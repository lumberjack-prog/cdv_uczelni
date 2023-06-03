package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.Wydzial;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.util.function.Tuple2;

@Repository
public interface WydzialRepository extends CassandraRepository<Wydzial, Tuple2<String, String>> {
}