Use budget_tracker;
SHOW TABLES;
CREATE TABLE users(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    age INT,
    email VARCHAR(50) UNIQUE,
    user_password VARCHAR(255),
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
CREATE TABLE category(
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50),
    category_type ENUM('FOOD','CLOTHING','TAX','TRANSPORTATION','ENTERTAINMENT','UTILITIES','HEALTHCARE','TECHNOLOGY','EDUCATION','OTHER')
);
CREATE TABLE record(
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    transaction_type ENUM('INCOME','EXPENSE','USE_SAVING','ADD_SAVING'),
    amount DECIMAL(10,2),
    note TEXT,
    record_date DATE,
    
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

SELECT * FROM Users;
SELECT * FROM Accounts;
SELECT * FROM Record;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE accounts;
TRUNCATE TABLE users;
TRUNCATE TABLE record;
SET FOREIGN_KEY_CHECKS = 1;