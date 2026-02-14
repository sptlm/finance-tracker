-- CREATE DATABASE financetracker;

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50)
);

CREATE TABLE currencies (
                            id SERIAL PRIMARY KEY,
                            code VARCHAR(3) UNIQUE NOT NULL,
                            name VARCHAR(50) NOT NULL,
                            symbol VARCHAR(5) NOT NULL,
                            exchange_rate_to_rub DECIMAL(10, 4) NOT NULL
);

CREATE TABLE accounts (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          currency_id INTEGER NOT NULL REFERENCES currencies(id) ON DELETE RESTRICT,
                          name VARCHAR(100) NOT NULL,
                          initial_balance DECIMAL(15, 2) DEFAULT 0.00,
                          current_balance DECIMAL(15, 2) DEFAULT 0.00
);

CREATE TABLE categories (
                            id SERIAL PRIMARY KEY,
                            user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            name VARCHAR(50) NOT NULL,
                            type VARCHAR(10) NOT NULL,
                            color VARCHAR(7),
                            icon VARCHAR(50),
                            CONSTRAINT check_type CHECK (type IN ('INCOME', 'EXPENSE')),
                            UNIQUE(user_id, name, type)
);


CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              account_id INTEGER NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
                              category_id INTEGER NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
                              amount DECIMAL(15, 2) NOT NULL,
                              type VARCHAR(10) NOT NULL,
                              description TEXT,
                              transaction_date DATE NOT NULL DEFAULT current_date,
                              CONSTRAINT check_amount CHECK (amount > 0),
                              CONSTRAINT check_transaction_type CHECK (type IN ('INCOME', 'EXPENSE'))
);

CREATE TABLE tags (
                      id SERIAL PRIMARY KEY,
                      user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                      name VARCHAR(50) NOT NULL,
                      color VARCHAR(7),
                      UNIQUE (user_id, name)
);



CREATE TABLE transaction_tags (
                                  transaction_id INTEGER NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
                                  tag_id INTEGER NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
                                  PRIMARY KEY (transaction_id, tag_id)
);


CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_currency_id ON accounts(currency_id);
CREATE INDEX idx_categories_user_id ON categories(user_id);
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_category_id ON transactions(category_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transaction_tags_tag_id ON transaction_tags(tag_id);
CREATE INDEX idx_tags_user_id ON tags(user_id);

-- Триггер для обновления баланса счета при добавлении транзакции
CREATE OR REPLACE FUNCTION update_account_balance()
    RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        IF (NEW.type = 'INCOME') THEN
UPDATE accounts SET current_balance = current_balance + NEW.amount
WHERE id = NEW.account_id;
ELSE
UPDATE accounts SET current_balance = current_balance - NEW.amount
WHERE id = NEW.account_id;
END IF;
    ELSIF (TG_OP = 'DELETE') THEN
        IF (OLD.type = 'INCOME') THEN
UPDATE accounts SET current_balance = current_balance - OLD.amount
WHERE id = OLD.account_id;
ELSE
UPDATE accounts SET current_balance = current_balance + OLD.amount
WHERE id = OLD.account_id;
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_balance
    AFTER INSERT OR DELETE ON transactions
    FOR EACH ROW EXECUTE FUNCTION update_account_balance();

