Use budget_tracker;
-- USE budget_tracker_system

CREATE TABLE users(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    age INT,
    email VARCHAR(50) UNIQUE,
    user_password VARCHAR(255),
    passkey VARCHAR(4) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE accounts(
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    balance DECIMAL(10,2) DEFAULT 0,
    saving_balance DECIMAL(10,2) DEFAULT 0,
    limit_balance DECIMAL(10,2) DEFAULT 0,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- CREATE TABLE category(
--     category_id INT AUTO_INCREMENT PRIMARY KEY,
--     category_name VARCHAR(50),
--     category_type ENUM('FOOD','CLOTHING','TAX','TRANSPORTATION','ENTERTAINMENT','UTILITIES','HEALTHCARE','TECHNOLOGY','EDUCATION','OTHER')
-- );
-- DROP TABLE category;

-- CREATE TABLE record(
--     record_id INT AUTO_INCREMENT PRIMARY KEY,
--     note TEXT,
--     account_id INT NOT NULL,
--     amount DECIMAL(10,2),
--     record_date DATE,
--     transaction_type ENUM('INCOME','EXPENSE','USE_SAVING','ADD_SAVING'),
    
--     FOREIGN KEY (account_id) REFERENCES accounts(account_id)
-- );
-- ALTER TABLE record
-- ADD COLUMN category ENUM('FOOD','CLOTHING','TAX','TRANSPORTATION','ENTERTAINMENT','UTILITIES','HEALTHCARE','TECHNOLOGY','EDUCATION','OTHER');

CREATE TABLE record(
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    note TEXT,
    account_id INT NOT NULL,
    amount DECIMAL(10,2),
    record_date DATE,
    transaction_type ENUM('INCOME','EXPENSE','USE_SAVING','ADD_SAVING'),
    category ENUM('FOOD','CLOTHING','TAX','TRANSPORTATION','ENTERTAINMENT','UTILITIES','HEALTHCARE','TECHNOLOGY','EDUCATION','OTHER'),
    
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

CREATE TABLE wishlists(
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    item_name VARCHAR(50),
    item_price DECIMAL(10,2),
    saved_amount DECIMAL(10,2),
    
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
