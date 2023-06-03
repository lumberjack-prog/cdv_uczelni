package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserDto implements Serializable {

    private String key;
    private String username;
    private String email;
    private Set<Uczelnia> ulubioneUczelnie;

    public UserDto(User user) {
        this.key = user.getKey();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.ulubioneUczelnie = user.getUlubioneUczelnie();
    }
}
