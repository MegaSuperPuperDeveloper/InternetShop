CREATE TABLE users (
    idUser SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    description VARCHAR(1000)
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role VARCHAR(150) NOT NULL,
    hierarchy INT NOT NULL
);

CREATE TABLE rewards (
    id SERIAL PRIMARY KEY,
    reward VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(idUser) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);