CREATE SCHEMA IF NOT EXISTS account;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE account.accounts
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    activated  BOOLEAN      NOT NULL,
    created_at DATE         NOT NULL,
    updated_at DATE,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255)
);

CREATE INDEX idx_accounts_user_name ON account.accounts (user_name);

CREATE TABLE account.permissions
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  DATE         NOT NULL,
    updated_at  DATE,
    created_by  VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255)
);

CREATE INDEX idx_permission_name ON account.permissions (name);

CREATE TABLE account.roles
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  DATE         NOT NULL,
    updated_at  DATE,
    created_by  VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255)
);

CREATE INDEX idx_role_name ON account.roles (name);

CREATE TABLE account.role_account
(
    id         SERIAL PRIMARY KEY,
    role_id    UUID REFERENCES account.roles (id),
    account_id UUID REFERENCES account.accounts (id)
);

CREATE TABLE account.role_permission
(
    id            SERIAL PRIMARY KEY,
    role_id       UUID REFERENCES account.roles (id),
    permission_id UUID REFERENCES account.permissions (id)
);

-- Make sure the uuid-ossp module is available

-- Insert sample data
INSERT INTO account.accounts (user_name, password, email, created_at, created_by, activated)
VALUES ('dungnc', '$2a$12$fuXP4tzkIpNpqRIvPC59VesFh/6ojA/gUEtrJmK/YDPvc9Y7D1yYK', 'dungnc69.420@gmail.com', '2023-01-02',
        'john_doe', TRUE),
       ('jane_doe', '$2a$12$dn.RZxzOM8fxyjerPc30/ufMe.FbXYbI35OyGGRSPzfIN464Y1mi2', 'janedoe@example.com', '2023-01-04',
        'john_doe', TRUE),
       ('alex_smith', '$2a$12$R.AMxKQntUYWOAQaRl0RDOLcdPW9OUHPSBkG64cVo6LCqtbNqYDhC', 'alexsmith@example.com', '2023-01-06',
        'john_doe', TRUE),
       ('dathq', '$2a$12$GhIDOaYoPrmv5f/bdacbmuR4zs7yFcyfMC15mkJnfJMuVWXO1ZHHW', 'dathq10@fpt.com', '2023-01-06',
        'dungnc', TRUE);

INSERT INTO account.roles (name, description, created_at, created_by)
VALUES ('ROLE_ADMIN', 'Administrator Role', CURRENT_DATE, 'john_doe'),
       ('ROLE_USER', 'User Role', CURRENT_DATE, 'john_doe');

INSERT INTO account.permissions (name, description, created_at, created_by)
VALUES ('Read', 'Read Permission', CURRENT_DATE, 'john_doe'),
       ('Write', 'Write Permission', CURRENT_DATE, 'john_doe');

INSERT INTO account.role_account (role_id, account_id) SELECT r.id, a.id
FROM account.roles r, account.accounts a
WHERE r.name = 'ROLE_ADMIN' AND a.user_name = 'john_doe';
INSERT INTO account.role_account (role_id, account_id) SELECT r.id, a.id
FROM account.roles r, account.accounts a
WHERE r.name = 'ROLE_USER' AND a.user_name = 'alex_smith';
INSERT INTO account.role_account (role_id, account_id) SELECT r.id, a.id
FROM account.roles r, account.accounts a
WHERE r.name = 'ROLE_USER' AND a.user_name = 'jane_doe';
INSERT INTO account.role_account (role_id, account_id) SELECT r.id, a.id
FROM account.roles r, account.accounts a
WHERE r.name = 'ROLE_ADMIN' AND a.user_name = 'dathq';

-- Similar approach for role_permission
INSERT INTO account.role_permission (role_id, permission_id) SELECT r.id, p.id
FROM account.roles r, account.permissions p
WHERE r.name = 'ROLE_ADMIN' AND p.name = 'Read';
INSERT INTO account.role_permission (role_id, permission_id) SELECT r.id, p.id
FROM account.roles r, account.permissions p
WHERE r.name = 'ROLE_ADMIN' AND p.name = 'Write';
INSERT INTO account.role_permission (role_id, permission_id) SELECT r.id, p.id
FROM account.roles r, account.permissions p
WHERE r.name = 'ROLE_USER' AND p.name = 'Read';