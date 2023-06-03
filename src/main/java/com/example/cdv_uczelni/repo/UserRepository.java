package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CassandraRepository<User, String> {
    Optional<User> findUserByUsername(String username);
}
