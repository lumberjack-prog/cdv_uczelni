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

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table("kierunki")
public class Kierunek implements Serializable, Comparable {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String name;
    private String type;

    @Override
    public int compareTo(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            name = name.trim();
            this.name = ((Kierunek) o).name.trim();
            return name.compareTo(((Kierunek) o).name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kierunek uczelnia = (Kierunek) o;
        return name.equals(uczelnia.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
