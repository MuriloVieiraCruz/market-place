CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tb_product (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_artist VARCHAR(200) NOT NULL,
    product_year INT NOT NULL,
    product_album VARCHAR(300) NOT NULL,
    product_price NUMERIC(2, 2) NOT NULL,
    product_store VARCHAR(200) NOT NULL,
    product_thumb VARCHAR(400) NOT NULL,
    product_date TIMESTAMP NOT NULL
);