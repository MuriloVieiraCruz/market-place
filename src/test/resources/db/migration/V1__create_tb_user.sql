CREATE TABLE tb_user (
    user_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    user_cpf VARCHAR(11) NOT NULL,
    user_email VARCHAR(100) UNIQUE NOT NULL,
    user_password VARCHAR(100) NOT NULL,
    user_status INT NOT NULL
)