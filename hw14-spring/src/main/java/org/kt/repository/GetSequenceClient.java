package org.kt.repository;

import org.kt.domain.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.SQLException;

@Component
public class GetSequenceClient implements BeforeConvertCallback<Client> {

    private final Logger log = LoggerFactory.getLogger(GetSequenceClient.class);

    private final JdbcTemplate jdbcTemplate;

    public GetSequenceClient(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Client onBeforeConvert(Client client) {
        if (client.getId() == null) {
            log.info("Get the next value from a database sequence and use it as the primary key");

            Long id = jdbcTemplate.query("SELECT nextval('client_SEQ')",
                    rs -> {
                        if (rs.next()) {
                            return rs.getLong(1);
                        } else {
                            throw new SQLException("Unable to retrieve value from sequence client_SEQ.");
                        }
                    });
            client = new Client(id, client.getName(), client.getAddress(), client.getPhones());
        }

        return client;
    }
}
