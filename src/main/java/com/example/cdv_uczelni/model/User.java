package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(value = "users")
public class User {

    @PrimaryKey
    private UUID id;
    private String username;
    private String email;
    private String password;
//    @Column("universities")
//    private Set<University> favoriteUniversities;
}
