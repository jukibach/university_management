CREATE SCHEMA IF NOT EXISTS account;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS account.accounts
(
    id           BIGSERIAL PRIMARY KEY,
    user_name    VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    is_activated BOOLEAN      NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP,
    created_by   VARCHAR(255) NOT NULL,
    updated_by   VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_accounts_user_name ON account.accounts (user_name);

CREATE TABLE IF NOT EXISTS account.login_history
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) UNIQUE,
    ip_address  VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP,
    account_id  BIGINT,
    created_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255) NOT NULL,
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES account.accounts (id)
);

CREATE TABLE IF NOT EXISTS account.permissions
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,
    created_by  VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_permission_name ON account.permissions (name);

CREATE TABLE IF NOT EXISTS account.roles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,
    created_by  VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_role_name ON account.roles (name);

CREATE TABLE IF NOT EXISTS account.role_account
(
    id         BIGSERIAL PRIMARY KEY,
    role_id    BIGSERIAL REFERENCES account.roles (id),
    account_id BIGSERIAL REFERENCES account.accounts (id)
);

CREATE TABLE IF NOT EXISTS account.role_permission
(
    id            BIGSERIAL PRIMARY KEY,
    role_id       BIGSERIAL REFERENCES account.roles (id),
    permission_id BIGSERIAL REFERENCES account.permissions (id)
);

-- Insert sample data
INSERT INTO account.accounts (user_name, password, created_at, created_by, is_activated)
VALUES ('admin', '$2a$12$Vr9c8qtydVi39u4HAHXDGePAHShspu.o1sfZdFt5VQ2fZ2fJIb3MW',
        CURRENT_TIMESTAMP,
        'admin', TRUE),
       ('user', '$2a$12$aXiUY2khzWepfiOU3ZejJ.GvQu83JC3m8fzfTOaEQfQTjSfthyQWy',
        CURRENT_TIMESTAMP,
        'user', TRUE),
       ('dungnc', '$2a$12$fuXP4tzkIpNpqRIvPC59VesFh/6ojA/gUEtrJmK/YDPvc9Y7D1yYK',
        CURRENT_TIMESTAMP, 'dungnc', TRUE),
       ('jane_doe', '$2a$12$dn.RZxzOM8fxyjerPc30/ufMe.FbXYbI35OyGGRSPzfIN464Y1mi2',
        CURRENT_TIMESTAMP,
        'dungnc', TRUE),
       ('alex_smith', '$2a$12$R.AMxKQntUYWOAQaRl0RDOLcdPW9OUHPSBkG64cVo6LCqtbNqYDhC',
        CURRENT_TIMESTAMP,
        'dungnc', TRUE),
       ('dathq', '$2a$12$GhIDOaYoPrmv5f/bdacbmuR4zs7yFcyfMC15mkJnfJMuVWXO1ZHHW', CURRENT_TIMESTAMP,
        'dungnc', TRUE)
ON CONFLICT DO NOTHING;

INSERT INTO account.roles (name, description, created_at, created_by)
VALUES ('ROLE_ADMIN', 'Administrator Role', CURRENT_TIMESTAMP, 'dungnc'),
       ('ROLE_USER', 'User Role', CURRENT_TIMESTAMP, 'dungnc');

INSERT INTO account.permissions (name, description, created_at, created_by)
VALUES ('Read', 'Read Permission', CURRENT_TIMESTAMP, 'dungnc'),
       ('Write', 'Write Permission', CURRENT_TIMESTAMP, 'dungnc');

INSERT INTO account.role_account (role_id, account_id)
SELECT r.id, a.id
FROM account.roles r,
     account.accounts a
WHERE r.name = 'ROLE_ADMIN'
  AND a.user_name = 'dungnc';
INSERT INTO account.role_account (role_id, account_id)
SELECT r.id, a.id
FROM account.roles r,
     account.accounts a
WHERE r.name = 'ROLE_ADMIN'
  AND a.user_name = 'admin';
INSERT INTO account.role_account (role_id, account_id)
SELECT r.id, a.id
FROM account.roles r,
     account.accounts a
WHERE r.name = 'ROLE_USER'
  AND a.user_name = 'alex_smith';
INSERT INTO account.role_account (role_id, account_id)
SELECT r.id, a.id
FROM account.roles r,
     account.accounts a
WHERE r.name = 'ROLE_USER'
  AND a.user_name = 'user';
INSERT INTO account.role_account (role_id, account_id)
SELECT r.id, a.id
FROM account.roles r,
     account.accounts a
WHERE r.name = 'ROLE_USER'
  AND a.user_name = 'jane_doe';
INSERT INTO account.role_account (role_id, account_id)
SELECT r.id, a.id
FROM account.roles r,
     account.accounts a
WHERE r.name = 'ROLE_ADMIN'
  AND a.user_name = 'dathq';

-- Similar approach for role_permission
INSERT INTO account.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM account.roles r,
     account.permissions p
WHERE r.name = 'ROLE_ADMIN'
  AND p.name = 'Read';
INSERT INTO account.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM account.roles r,
     account.permissions p
WHERE r.name = 'ROLE_ADMIN'
  AND p.name = 'Write';
INSERT INTO account.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM account.roles r,
     account.permissions p
WHERE r.name = 'ROLE_USER'
  AND p.name = 'Read';