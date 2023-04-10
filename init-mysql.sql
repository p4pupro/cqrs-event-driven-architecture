CREATE DATABASE IF NOT EXISTS sales;
USE sales;
CREATE TABLE IF NOT EXISTS devices (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    mark VARCHAR(60) NOT NULL,
    model VARCHAR(100) NOT NULL,
    color VARCHAR(60) NOT NULL,
    price DECIMAL(6,2) NOT NULL,
    service_id VARCHAR(100),
    creation_date VARCHAR(60),
    updated_date VARCHAR(60)
    );
CREATE USER 'command_user' IDENTIFIED BY 'FNQXvAzNCew6YMkdnDAp79bpT4ub';
GRANT ALL PRIVILEGES ON sales.* TO 'command_user';
