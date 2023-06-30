package org.kt.domain;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "phone")
public class Phone {
    @Nonnull
    private final String number;


    public Phone(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "number='" + number + '\'' +
                '}';
    }
}

