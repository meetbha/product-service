CREATE TABLE IF NOT EXISTS products
(
    id uuid PRIMARY KEY,
    created_at timestamp(6) without time zone,
    description character varying(255),
    name character varying(255) UNIQUE NOT NULL,
    price numeric(38,2)
)