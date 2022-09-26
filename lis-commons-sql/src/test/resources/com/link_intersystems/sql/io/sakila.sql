create table actor (actor_id smallint not null, first_name varchar(45) not null, last_name varchar(45) not null, last_update timestamp default CURRENT_TIMESTAMP not null, constraint pk_actor primary key (actor_id));
create index idx_actor_last_name on actor (last_name);

INSERT INTO "actor" VALUES
                        (1, 'PENELOPÈ', 'GUINESS', TIMESTAMP '2006-02-15 04:34:33');