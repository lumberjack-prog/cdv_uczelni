package com.example.cdv_uczelni.service;

import com.example.cdv_uczelni.model.Uczelnia;
import com.example.cdv_uczelni.model.UczelnieJson;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class RedisService implements IRedisDao {

    @Autowired
    RedisClient redisClient;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Set<Uczelnia> getAll() {
        log.info("Getting all");

        Set<Uczelnia> redisSet = new HashSet<>();

        try (StatefulRedisConnection<String, String> connection = redisClient.connect();) {
            RedisCommands<String, String> syncCommands = connection.sync();
            Set<String> uczelnie = syncCommands.smembers("uczelnie");

            try {
                for (String s : uczelnie) {
                    redisSet.add(objectMapper.readValue(s, Uczelnia.class));
                }
            } catch (JsonProcessingException e) {
                log.error("Failed to get all: {}", e.getMessage());
                throw new RuntimeException(e);
            }

        }
        return redisSet;
    }

    @Override
    public Uczelnia getById(String id) {
        Optional<Uczelnia> first = getAll().stream()
                .filter(uczelnia -> uczelnia.getKey().split(":")[1].equals(id))
                .findFirst();

        if (first.isPresent()) {
            return first.get();
        } else {
            throw new RuntimeException(String.format("Uczelnia z name: %s nie istnije", id));
        }
    }

    @Override
    public Set<Uczelnia> saveAll(UczelnieJson uczelnieMap) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect();) {
            RedisCommands<String, String> syncCommands = connection.sync();

            Set<String> redisSetString = syncCommands.smembers("uczelnie");
            Set<Uczelnia> redisSet = convertSetStringToSetUczelnia(redisSetString);

            if (redisSet.size() > 0 && uczelnieMap.getUczelnie().size() > 0) {
                for (Uczelnia uczelniaJson : uczelnieMap.getUczelnie()) {
                    for (Uczelnia uczelniaRedis : redisSet) {
                        if (uczelniaJson.equals(uczelniaRedis)) {
                            syncCommands.srem("uczelnie", objectMapper.writeValueAsString(uczelniaRedis));
                            if (uczelniaJson.getType() != null) {
                                uczelniaRedis.setType(uczelniaJson.getType());
                            }
                            if (uczelniaJson.getMiasto() != null) {
                                uczelniaRedis.setMiasto(uczelniaJson.getMiasto());
                            }
                            if (uczelniaJson.getScore() != 0) {
                                uczelniaRedis.setScore(uczelniaJson.getScore());
                            }
                            syncCommands.sadd("uczelnie", objectMapper.writeValueAsString(uczelniaRedis));
                        }
                    }
                }
            }

            for (Uczelnia uczelniaJson : uczelnieMap.getUczelnie()) {
                if (!redisSet.contains(uczelniaJson)) {
                    uczelniaJson.setNextKey();
                    redisSet.add(uczelniaJson);
                    syncCommands.sadd("uczelnie", objectMapper.writeValueAsString(uczelniaJson));
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return getAll();
    }

    private Set<Uczelnia>  convertSetStringToSetUczelnia(Set<String> redisSetString) throws JsonProcessingException {
        Set<Uczelnia> redisSet = new HashSet<>();
        for (String s : redisSetString) {
            redisSet.add(objectMapper.readValue(s, Uczelnia.class));
        }
        return redisSet;
    }

    @Override
    public Uczelnia save(Uczelnia uczelnia) {
            try (StatefulRedisConnection<String, String> connection = redisClient.connect();) {
                RedisCommands<String, String> syncCommands = connection.sync();
                Set<String> redisSetString = syncCommands.smembers("uczelnie");
                Set<Uczelnia> redisSet = convertSetStringToSetUczelnia(redisSetString);

                if (!redisSet.contains(uczelnia)) {
                    redisSet.add(uczelnia);
                    uczelnia.setNextKey();
                    syncCommands.sadd("uczelnie", objectMapper.writeValueAsString(uczelnia));
                } else {
                    redisSet.forEach(uczelniaRedis -> {
                        if (uczelniaRedis.equals(uczelnia)) {
                            try {
                                syncCommands.srem("uczelnie", objectMapper.writeValueAsString(uczelniaRedis));
                                if (uczelnia.getType() != null) {
                                    uczelniaRedis.setType(uczelnia.getType());
                                }
                                if (uczelnia.getMiasto() != null) {
                                    uczelniaRedis.setMiasto(uczelnia.getMiasto());
                                }
                                if (uczelnia.getScore() != 0) {
                                    uczelniaRedis.setScore(uczelnia.getScore());
                                }
                                syncCommands.sadd("uczelnie", objectMapper.writeValueAsString(uczelniaRedis));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                return getById(uczelnia.getKey().split(":")[1]);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }
}
