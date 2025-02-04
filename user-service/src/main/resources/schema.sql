CREATE TABLE IF NOT EXISTS users (
    idUser SERIAL PRIMARY KEY,
    displayedUsername VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    roleName VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description VARCHAR(1000) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_tags (
                                         user_id BIGINT NOT NULL,
                                         tag VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(idUser) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS products (
    idProduct SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    tag VARCHAR(50) NOT NULL,
    price VARCHAR(50) NOT NULL,
    author_name VARCHAR(50) NOT NULL,
    author_id BIGINT NOT NULL
);