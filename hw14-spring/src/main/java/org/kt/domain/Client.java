package org.kt.domain;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Table(name = "client")
public class Client {
    @Id
    @Nonnull
    private final Long id;

    @Nonnull
    private final String name;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "order_column")
    private final List<Phone> phones;

    public Client(String name, Address address, List<Phone> phones) {
        this(null, name, address, phones);
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
