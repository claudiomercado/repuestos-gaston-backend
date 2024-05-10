INSERT INTO users (email, username, password, name, surname, dni, birthdate) VALUES ('admin@gmail.com', 'admin', 'admin', 'Gastón', 'Mercado', '39.393.935', '1996-05-14');

INSERT INTO category (name) VALUES ('Lubricantes');

INSERT INTO category (name) VALUES ('Accesorios');

INSERT INTO category (name) VALUES ('Neumáticos');

INSERT INTO category (name) VALUES ('Electrónica');

INSERT INTO role (name) VALUES ('ADMIN');

INSERT INTO role (name) VALUES ('USER');

INSERT INTO user_role (id_user, id_role) VALUES(1,1);