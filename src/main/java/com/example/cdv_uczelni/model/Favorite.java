package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(value = "favorites")
public class Favorite {
    @PrimaryKey
    private UUID id;
    private String username;
    @Column("favoriteUniversities")
    private Set<UniversityUDT> favoriteUniversities;
}
