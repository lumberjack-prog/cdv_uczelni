package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table("universities")
@UserDefinedType("universities")
public class University implements Serializable, Comparable {
    @PrimaryKey
    private UUID id;
    private String name;
    private String type;
    private String city;
    private int score;
    @Column("faculties")
    private List<Faculty> faculties;

    @Override
    public int compareTo(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            this.name = name.trim();
            ((University) o).name = ((University) o).name.trim();
            return name.compareTo(((University) o).name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University university = (University) o;
        return name.equals(university.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
