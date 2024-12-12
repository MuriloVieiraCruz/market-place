CREATE TABLE IF NOT EXISTS tb_role (
    role_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    role_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tb_user_role (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES tb_role(role_id) ON DELETE CASCADE
);