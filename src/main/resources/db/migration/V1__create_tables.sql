
CREATE TABLE vk_users (
                          vk_user_id BIGINT PRIMARY KEY,
                          first_name VARCHAR(100),
                          last_name VARCHAR(100),
                          dialog_state VARCHAR(50) NOT NULL DEFAULT 'NEW_USER',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          last_seen_at TIMESTAMP
);

CREATE TABLE request_history (
                                 id BIGSERIAL PRIMARY KEY,
                                 vk_user_id BIGINT NOT NULL,
                                 request_text VARCHAR(500) NOT NULL,
                                 currency_code VARCHAR(10),
                                 rate_value DECIMAL(20,8),
                                 request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_user FOREIGN KEY (vk_user_id) REFERENCES vk_users(vk_user_id)
);

CREATE INDEX idx_history_user ON request_history(vk_user_id);
CREATE INDEX idx_history_date ON request_history(request_date);

