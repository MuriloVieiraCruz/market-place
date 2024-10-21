CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tb_product (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    artist VARCHAR(200) NOT NULL,
    year INT NOT NULL,
    album VARCHAR(300) NOT NULL,
    price NUMERIC(2, 2) NOT NULL,
    store VARCHAR(200) NOT NULL,
    thumb VARCHAR(400) NOT NULL,
    date TIMESTAMP NOT NULL
);