CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_premium_user BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    description VARCHAR(2000)
);

CREATE TABLE roles (
    id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id, role),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE rewards (
    id BIGINT NOT NULL,
    reward VARCHAR(255) NOT NULL,
    PRIMARY KEY (id, reward),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);