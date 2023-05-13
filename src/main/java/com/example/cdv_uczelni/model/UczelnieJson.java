package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
@Setter
@Getter
@ToString
@NoArgsConstructor
@RedisHash
public class UczelnieJson implements Serializable {
    private List<Uczelnia> uczelnie;
}
