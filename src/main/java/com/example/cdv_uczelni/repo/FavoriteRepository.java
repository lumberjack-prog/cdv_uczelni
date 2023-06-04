package com.example.cdv_uczelni.repo;

import com.example.cdv_uczelni.model.Favorite;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FavoriteRepository extends CassandraRepository<Favorite, UUID> {
}
