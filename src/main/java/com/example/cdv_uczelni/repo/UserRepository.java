package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CassandraRepository<User, UUID> {
    @AllowFiltering
    Optional<User> findUserByUsername(String username);
    @AllowFiltering
    Optional<User> findUserByEmail(String username);
}
