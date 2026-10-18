CREATE TABLE users (
    id                  BIGSERIAL PRIMARY KEY,
    email               VARCHAR(255) NOT NULL,
    password            VARCHAR(255) NOT NULL,
    nickname            VARCHAR(30)  NOT NULL,
    bio                 VARCHAR(160),
    profile_image_url   VARCHAR(500),
    activation_code     VARCHAR(36),
    activated           BOOLEAN      NOT NULL DEFAULT FALSE,
    status              VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_users_email ON users (email);
