package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.Kierunek;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.util.function.Tuple2;

@Repository
public interface KierunkiRepository extends CassandraRepository<Kierunek, Tuple2<String, String>> {
}
