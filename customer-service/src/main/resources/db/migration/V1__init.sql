CREATE TABLE customers(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50) ,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    mobile_number VARCHAR(20) NOT NULL,
    gender VARCHAR(1),
    customer_number VARCHAR(40) NOT NULL,
    country_of_birth VARCHAR(2) NOT NULL,
    country_of_residence VARCHAR(2) NOT NULL,
    segment VARCHAR(15) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
