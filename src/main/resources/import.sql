create table games(
       id bigint auto_increment,
       name varchar(255),
       count varchar(255),
       status varchar(255),
       primary key (id)
    );

INSERT INTO games (name, count, status) VALUES ('first', '3', 'open');
INSERT INTO games (name, count, status) VALUES ('second', '5', 'closed');
INSERT INTO games (name, count, status) VALUES ('third', '2', 'closed');