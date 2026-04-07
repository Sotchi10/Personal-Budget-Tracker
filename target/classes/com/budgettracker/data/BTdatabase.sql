Use budget_tracker;
<<<<<<< HEAD
SHOW TABLES;
=======
-- USE budget_tracker_system

>>>>>>> d26f37d6f74b935e16ac5a84b0024ffeee04f0e2
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

CREATE TABLE record(
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    transaction_type ENUM('INCOME','EXPENSE','USE_SAVING','ADD_SAVING'),
<<<<<<< HEAD
    amount DECIMAL(10,2),
    note TEXT,
    record_date DATE,
=======
    category ENUM('FOOD','CLOTHING','TAX','TRANSPORTATION','ENTERTAINMENT','UTILITIES','HEALTHCARE','TECHNOLOGY','EDUCATION','OTHER'),
>>>>>>> d26f37d6f74b935e16ac5a84b0024ffeee04f0e2
    
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


