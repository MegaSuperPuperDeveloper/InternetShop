CREATE TABLE users (
    idUser SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAM NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAM NOT NULL,
    description VARCHAR(1000) NOT NULL
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role VARCHAR(150) NOT NULL,
    hierarchy INT NOT NULL
);

CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    tag VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(idUser) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE user_tags (
    user_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, tag_id),
    FOREIGN KEY (user_id) REFERENCES users(idUser) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES roles(id) ON DELETE CASCADE
);