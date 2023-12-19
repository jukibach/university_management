CREATE TABLE accounts
(
    id         UUID         NOT NULL,
    created_at date,
    updated_at date,
    user_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE INDEX idx_accounts_user_name ON accounts (user_name);
CREATE INDEX idx_accounts_email ON accounts (email);


INSERT INTO accounts (id, created_at, updated_at, user_name, password, email) VALUES ('b079aab5-2b70-4cfc-bbe4-e1e23075198d', '2023-12-19 04:07:44.120121', '2023-12-19 04:07:44.120121', 'sBGBAdryOn', 'nDkBYeISoS', 'RJOFL@example.com');
INSERT INTO accounts (id, created_at, updated_at, user_name, password, email) VALUES ('0f0d96e0-9f6c-4884-aa80-67e9d555fbea', '2023-12-19 04:07:44.120192', '2023-12-19 04:07:44.120192', 'wNcytbdkdC', 'RZeFJikrzy', 'tEbyC@example.com');
INSERT INTO accounts (id, created_at, updated_at, user_name, password, email) VALUES ('aa3a8ebe-3613-4810-909c-1ca582a4d460', '2023-12-19 04:07:44.120234', '2023-12-19 04:07:44.120234', 'SjndEUpxXn', 'OymnopXUcR', 'WZBlz@example.com');
INSERT INTO accounts (id, created_at, updated_at, user_name, password, email) VALUES ('090cff7a-3ae7-4e4b-8746-7bd492ab093d', '2023-12-19 04:07:44.120261', '2023-12-19 04:07:44.120261', 'UJMPcXdliH', 'bRLoJjAphz', 'DmPZB@example.com');
INSERT INTO accounts (id, created_at, updated_at, user_name, password, email) VALUES ('0e0946c5-3b19-49ce-ae48-f1f458bf48ef', '2023-12-19 04:07:44.120307', '2023-12-19 04:07:44.120307', 'sjohCyTrTi', 'TwHSrpgkNp', 'WvJjG@example.com');

