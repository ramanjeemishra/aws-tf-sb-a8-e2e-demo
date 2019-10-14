CREATE TABLE addresses(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    sequence_number INT(2) NOT NULL,
    customer_id BIGINT(20) NOT NULL,
    addr_type VARCHAR(10) NOT NULL,
    addr_line1 VARCHAR(100) NOT NULL,
    addr_line2 VARCHAR(100) NOT NULL,
    addr_line3 VARCHAR(100)  NULL,
    addr_line4 VARCHAR(100)  NULL,
    country_code VARCHAR(2) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    state VARCHAR(20)  NULL,
    city VARCHAR(20)  NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
