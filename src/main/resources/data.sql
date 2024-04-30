INSERT INTO users (email, username, password, name, surname, dni, birthdate)
VALUES ('user2@gmail.com', 'user2', '1234', 'usuario', 'usuario', '39.393.935', '1996-05-14');

INSERT INTO role (name)
VALUES ('ADMIN');

INSERT INTO role (name)
VALUES ('USER');

INSERT INTO user_role (id_user, id_role)
VALUES(1,1);



