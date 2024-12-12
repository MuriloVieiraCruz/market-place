CREATE TABLE IF NOT EXISTS tb_transaction (
    transaction_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    transaction_total_payment NUMERIC(10,2) NOT NULL,
    transaction_moment TIMESTAMP NOT NULL,
    transaction_payment_method INT NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

CREATE TABLE IF NOT EXISTS tb_item_transaction (
    item_transaction_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    item_transaction_product_name VARCHAR(100) NOT NULL,
    item_transaction_quantity INT NOT NULL,
    item_transaction_price NUMERIC(10,2) NOT NULL,
    transaction_id UUID NOT NULL,
    product_id UUID NOT NULL,
    CONSTRAINT fk_it_transaction FOREIGN KEY (transaction_id) REFERENCES tb_transaction(transaction_id),
    CONSTRAINT fk_it_product FOREIGN KEY (product_id) REFERENCES tb_product(product_id)
);