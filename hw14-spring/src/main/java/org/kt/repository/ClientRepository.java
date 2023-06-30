package org.kt.repository;

import org.kt.domain.Client;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    // Проверка custom-запроса с сортировкой (аналог findAllByOrderByIdAsc)
    @Query("select id, name, street as address_street, client_id as address_client_id " +
            "from client c left join address a on c.id = a.client_id order by id")
    List<Client> findAll();
}