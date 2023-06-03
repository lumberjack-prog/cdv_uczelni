package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RedisHash
@Table("uczelnie")
public class Uczelnia implements Serializable, Comparable {
    private static int currentIndex = 0;
    @PrimaryKey
    private String key;
    private String name;
    private String type;
    private String miasto;
    private int score;
    private Set<Wydzial> wydzialy;


    public String setNextKey() {
        String key = "uczelnie:" + Uczelnia.currentIndex;
        currentIndex = Uczelnia.currentIndex + 1;
        this.key = key;
        return key;
    }

    @Override
    public int compareTo(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            this.name = name.trim();
            ((Uczelnia) o).name = ((Uczelnia) o).name.trim();
            return name.compareTo(((Uczelnia) o).name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uczelnia uczelnia = (Uczelnia) o;
        return name.equals(uczelnia.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
