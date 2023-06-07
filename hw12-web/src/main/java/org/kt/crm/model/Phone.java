package org.kt.crm.model;

import jakarta.persistence.*;
import lombok.*;
import org.kt.web.helpers.ExcludeGson;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    @ExcludeGson
    private Client client;

    public Phone(String number) {
        this.id = null;
        this.number = number;
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(Long id, String number, Client client) {
        this.id = id;
        this.number = number;
        this.client = client;
    }
}
