CREATE TABLE IF NOT EXISTS users (
    idUser SERIAL PRIMARY KEY,
    displayedUsername VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    roleName VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAM NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAM NOT NULL,
    description VARCHAR(1000) NOT NULL
);

CREATE TABLE IF NOT EXISTS tags (
    id SERIAL PRIMARY KEY,
    tag VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_tags (
    user_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, tag_id),
    FOREIGN KEY (user_id) REFERENCES users(idUser) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES roles(id) ON DELETE CASCADE
);