package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table("wydzialy")
public class Wydzial implements Serializable, Comparable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String name;
    private Set<Kierunek> kierunki;

    @Override
    public int compareTo(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            name = name.trim();
            ((Wydzial) o).name = ((Wydzial) o).name.trim();
            return name.compareTo(((Wydzial) o).name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wydzial uczelnia = (Wydzial) o;
        return name.equals(uczelnia.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
