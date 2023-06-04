package com.example.cdv_uczelni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@UserDefinedType("major")
public class Major implements Serializable, Comparable {

    private String name;
    private String type;

    @Override
    public int compareTo(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            name = name.trim();
            this.name = ((Major) o).name.trim();
            return name.compareTo(((Major) o).name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Major uczelnia = (Major) o;
        return name.equals(uczelnia.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
