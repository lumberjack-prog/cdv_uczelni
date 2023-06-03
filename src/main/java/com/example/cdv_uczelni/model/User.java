package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(value = "users")
public class User {
    private static int currentIndex = 2;
    @PrimaryKey
    private String key;
    private String username;
    private String email;
    private String password;
    private Set<Uczelnia> ulubioneUczelnie;

    public String setNextKey() {
        String key = "users:" + User.currentIndex;
        currentIndex = User.currentIndex + 1;
        this.key = key;
        return key;
    }

    public String setKeyForAdmin() {
        String key = "users:1";
        this.key = key;
        return key;
    }
}
