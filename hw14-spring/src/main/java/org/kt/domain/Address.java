package org.kt.domain;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "address")
public class Address {

    @Nonnull
    private final String street;

    public Address(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                ", street='" + street + '\'' +
                '}';
    }
}
