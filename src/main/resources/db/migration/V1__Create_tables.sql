CREATE TABLE public.accounts
(
    id         UUID PRIMARY KEY,
    user_name  VARCHAR(256) NOT NULL,
    password   VARCHAR(256) NOT NULL,
    email      VARCHAR(256) NOT NULL,
    created_at DATE,
    updated_at DATE,
    created_by VARCHAR(256),
    updated_by VARCHAR(256)
);
CREATE INDEX idx_accounts_user_name ON public.accounts (user_name);

-- Make sure the uuid-ossp module is available
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Insert sample data
INSERT INTO public.accounts (id, user_name, password, email, created_at, updated_at, created_by, updated_by)
VALUES
    (public.uuid_generate_v4(), 'john_doe', 'johnspassword', 'johndoe@example.com', '2023-01-01', '2023-01-02', 'john_doe', 'john_doe'),
    (public.uuid_generate_v4(), 'jane_doe', 'janespassword', 'janedoe@example.com', '2023-01-03', '2023-01-04', 'john_doe', 'john_doe'),
    (public.uuid_generate_v4(), 'alex_smith', 'alexspassword', 'alexsmith@example.com', '2023-01-05', '2023-01-06', 'john_doe', 'john_doe');


