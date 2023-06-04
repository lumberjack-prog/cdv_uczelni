package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@UserDefinedType("faculty")
public class Faculty implements Serializable, Comparable {

    private String name;
    private Set<Major> majors;

    @Override
    public int compareTo(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            name = name.trim();
            ((Faculty) o).name = ((Faculty) o).name.trim();
            return name.compareTo(((Faculty) o).name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty university = (Faculty) o;
        return name.equals(university.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
