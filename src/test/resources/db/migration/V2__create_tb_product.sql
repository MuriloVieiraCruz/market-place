CREATE TABLE tb_product (
    product_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    product_artist VARCHAR(200) NOT NULL,
    product_year INT2 NOT NULL,
    product_album VARCHAR(300) NOT NULL,
    product_price NUMERIC(10, 2) NOT NULL,
    product_store VARCHAR(200) NOT NULL,
    product_thumb VARCHAR(400) NOT NULL,
    product_date TIMESTAMP NOT NULL
);