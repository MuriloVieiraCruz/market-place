CREATE TABLE IF NOT EXISTS tb_credit_card (
    card_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    card_holder_name VARCHAR(80) NOT NULL,
    card_cvv VARCHAR(3) NOT NULL,
    card_expiration_date DATE NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_card_user FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
)