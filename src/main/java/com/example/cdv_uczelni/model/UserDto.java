package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserDto implements Serializable {

    private UUID id;
    private String username;
    private String email;
    private Set<University> favoriteUniversities;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
//        this.favoriteUniversities = user.getFavoriteUniversities();
    }
}
