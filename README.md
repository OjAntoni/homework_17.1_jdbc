# homework_17.1_jdbc
This homework is on working with sql scripts in hava and JDBC. If you want to test this class you need to create such tables in sql: 

create table addresses
(
    address_id serial not null
        constraint adresses_pk
            primary key,
    street     varchar
);

alter table addresses
    owner to postgres;

create unique index adresses_adress_id_uindex
    on addresses (address_id);
    
create table phones
(
    phone_id serial not null
        constraint phones_pk
            primary key,
    number   varchar
);

alter table phones
    owner to postgres;

create unique index phones_phone_id_uindex
    on phones (phone_id);

create table users
(
    id       serial not null
        constraint users_pk
            primary key,
    name     varchar,
    surname  varchar,
    password varchar
);

alter table users
    owner to postgres;

create unique index users_id_uindex
    on users (id);

create table users_addresses
(
    user_id    integer,
    address_id integer
);

alter table users_addresses
    owner to postgres;

create table users_phones
(
    user_id  integer,
    phone_id integer
);

alter table users_phones
    owner to postgres;
