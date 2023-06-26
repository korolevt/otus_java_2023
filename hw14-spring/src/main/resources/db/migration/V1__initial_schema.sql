create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id bigint not null primary key,
    name varchar(255)
);

create table address (
    client_id bigint not null references client (id),
    street varchar(255)
);

create table phone (
    id bigserial not null primary key,
    number varchar(255) not null,
    order_column bigint not null,
    client_id bigint not null references client (id)
);