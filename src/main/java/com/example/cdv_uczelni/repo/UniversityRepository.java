package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.University;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UniversityRepository extends CassandraRepository<University, UUID> {
    @AllowFiltering
    Optional<University> findUniversityByName(String name);
}
